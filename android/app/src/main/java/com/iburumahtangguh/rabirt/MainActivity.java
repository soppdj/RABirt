package com.iburumahtangguh.rabirt;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;

import com.getcapacitor.Bridge;
import com.getcapacitor.BridgeActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainActivity extends BridgeActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupDownloaderBridge();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupDownloaderBridge();
    }

    private void setupDownloaderBridge() {
        try {
            Bridge bridge = getBridge();
            if (bridge != null && bridge.getWebView() != null) {
                WebView webView = bridge.getWebView();
                webView.addJavascriptInterface(new AndroidDownloader(this), "AndroidDownloader");
            }
        } catch (Exception e) {
            Log.e("RABirt", "Gagal mendaftarkan AndroidDownloader bridge", e);
        }
    }

    public static class AndroidDownloader {
        private final Context context;

        public AndroidDownloader(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void openExternalUrl(String url) {
            try {
                if (url == null || url.isEmpty()) return;
                
                // Pastikan diarahkan ke web browser terlebih dahulu (CATEGORY_BROWSABLE)
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                try {
                    context.startActivity(browserIntent);
                } catch (Exception e) {
                    Intent fallbackIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    fallbackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(fallbackIntent);
                }
            } catch (Exception e) {
                Log.e("Rabirt", "Gagal membuka URL: " + url, e);
                showToastOnUI("Membuka browser... Jika tidak otomatis, hubungi WA Admin: 085150782500");
            }
        }

        @JavascriptInterface
        public void saveBase64File(String base64Data, String filename, String mimeType) {
            try {
                if (base64Data == null || base64Data.isEmpty()) {
                    showToastOnUI("Data file kosong.");
                    return;
                }

                String cleanBase64 = base64Data;
                if (cleanBase64.contains(",")) {
                    cleanBase64 = cleanBase64.substring(cleanBase64.indexOf(",") + 1);
                }

                byte[] fileBytes = Base64.decode(cleanBase64, Base64.DEFAULT);

                Uri mediaStoreUri = null;
                File physicalFile = null;

                // 1. Simpan ke Public Download/RABirt melalui MediaStore (Android 10+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    try {
                        ContentResolver resolver = context.getContentResolver();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/RABirt");

                        mediaStoreUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
                        if (mediaStoreUri != null) {
                            try (OutputStream os = resolver.openOutputStream(mediaStoreUri)) {
                                if (os != null) {
                                    os.write(fileBytes);
                                    os.flush();
                                }
                            }
                        }
                    } catch (Exception eMedia) {
                        Log.w("Rabirt", "MediaStore write error (fallback to file): " + eMedia.getMessage());
                    }
                }

                // 2. Simpan juga ke berkas fisik publik jika memungkinkan
                try {
                    File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File rabirtDir = new File(downloadDir, "RABirt");
                    if (!rabirtDir.exists()) {
                        rabirtDir.mkdirs();
                    }
                    File publicFile = new File(rabirtDir, filename);
                    try (FileOutputStream fos = new FileOutputStream(publicFile)) {
                        fos.write(fileBytes);
                        fos.flush();
                        physicalFile = publicFile;
                    }
                } catch (Exception ePub) {
                    Log.w("Rabirt", "Public raw file write restricted by scoped storage: " + ePub.getMessage());
                }

                // 3. Simpan di folder external aplikasi yang selalu diizinkan (jaminan akses FileProvider 100%)
                if (physicalFile == null || !physicalFile.exists()) {
                    try {
                        File appExtDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                        if (appExtDir != null) {
                            if (!appExtDir.exists()) appExtDir.mkdirs();
                            File safeFile = new File(appExtDir, filename);
                            try (FileOutputStream fos = new FileOutputStream(safeFile)) {
                                fos.write(fileBytes);
                                fos.flush();
                                physicalFile = safeFile;
                            }
                        }
                    } catch (Exception eApp) {
                        Log.e("Rabirt", "App-specific write error: " + eApp.getMessage());
                    }
                }

                // Beritahu MediaScanner jika ada berkas fisik
                if (physicalFile != null && physicalFile.exists()) {
                    try {
                        MediaScannerConnection.scanFile(
                                context,
                                new String[]{physicalFile.getAbsolutePath()},
                                new String[]{mimeType},
                                null
                        );
                    } catch (Exception ignored) {}
                }

                final String storagePathDisplay = "Penyimpanan Internal > Download > RABirt > " + filename;
                final File finalFile = physicalFile;
                final Uri finalUri = mediaStoreUri;

                // Tampilkan Dialog Informasi Lokasi Penyimpanan & Notifikasi Sistem
                if (context instanceof BridgeActivity) {
                    ((BridgeActivity) context).runOnUiThread(() -> {
                        // 1. Toast
                        Toast.makeText(context, "Laporan tersimpan di: " + storagePathDisplay, Toast.LENGTH_LONG).show();

                        // 2. AlertDialog Informasi Lokasi Penyimpanan
                        try {
                            new AlertDialog.Builder(context)
                                    .setTitle("📁 Berkas Berhasil Disimpan!")
                                    .setMessage("Laporan evaluasi anggaran telah berhasil disimpan ke HP Bunda:\n\n📂 Lokasi Penyimpanan:\n" + storagePathDisplay + "\n\nDokumen dapat dibuka kapan saja melalui File Manager atau aplikasi PDF di HP Bunda.")
                                    .setPositiveButton("Buka Dokumen", (dialog, which) -> openPdfDocument(finalFile, finalUri, mimeType))
                                    .setNegativeButton("Tutup", null)
                                    .show();
                        } catch (Exception eDialog) {
                            Log.w("Rabirt", "AlertDialog show error: " + eDialog.getMessage());
                        }

                        // 3. System Notification di status bar
                        showSystemDownloadNotification(filename, storagePathDisplay, finalFile, finalUri, mimeType);
                    });
                }

                // 4. Langsung Buka File PDF Secara Otomatis
                openPdfDocument(finalFile, finalUri, mimeType);

            } catch (Exception e) {
                Log.e("Rabirt", "Error saat menyimpan/membuka berkas: " + e.getMessage(), e);
                showToastOnUI("Gagal menyimpan berkas: " + e.getMessage());
            }
        }

        private void showSystemDownloadNotification(String filename, String pathDisplay, File file, Uri uri, String mimeType) {
            try {
                String channelId = "rabirt_download_channel";
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager == null) return;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId, "Unduhan Laporan Rabirt", NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription("Notifikasi unduhan berkas laporan Rabirt");
                    notificationManager.createNotificationChannel(channel);
                }

                Uri openUri = null;
                if (file != null && file.exists()) {
                    openUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
                } else if (uri != null) {
                    openUri = uri;
                }

                PendingIntent pendingIntent = null;
                if (openUri != null) {
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                    viewIntent.setDataAndType(openUri, mimeType);
                    viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    int flags = PendingIntent.FLAG_UPDATE_CURRENT;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        flags |= PendingIntent.FLAG_IMMUTABLE;
                    }
                    pendingIntent = PendingIntent.getActivity(context, 101, viewIntent, flags);
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(android.R.drawable.stat_sys_download_done)
                        .setContentTitle("Laporan Rabirt Tersimpan")
                        .setContentText("Disimpan di: " + pathDisplay)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Laporan berhasil disimpan di:\n" + pathDisplay + "\nKetuk untuk langsung membuka dokumen."))
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                if (pendingIntent != null) {
                    builder.setContentIntent(pendingIntent);
                }

                notificationManager.notify((int) System.currentTimeMillis(), builder.build());
            } catch (Exception e) {
                Log.w("Rabirt", "Gagal menampilkan system notification: " + e.getMessage());
            }
        }

        private void openPdfDocument(File file, Uri mediaStoreUri, String mimeType) {
            try {
                Uri openUri = null;
                if (file != null && file.exists()) {
                    openUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
                } else if (mediaStoreUri != null) {
                    openUri = mediaStoreUri;
                }

                if (openUri == null) {
                    showToastOnUI("File tersimpan di folder Download/RABirt.");
                    return;
                }

                Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                viewIntent.setDataAndType(openUri, mimeType);
                viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                Intent chooser = Intent.createChooser(viewIntent, "Buka Laporan PDF dengan...");
                chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(chooser);

            } catch (Exception ex) {
                Log.w("Rabirt", "Tidak dapat membuka aplikasi pembaca PDF otomatis: " + ex.getMessage());
                showToastOnUI("File tersimpan di folder Download/RABirt.");
            }
        }

        private void showToastOnUI(final String message) {
            if (context instanceof BridgeActivity) {
                ((BridgeActivity) context).runOnUiThread(() -> Toast.makeText(context, message, Toast.LENGTH_LONG).show());
            }
        }
    }
}
