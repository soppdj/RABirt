# MASTER PROMPT: PENGEMBANGAN APLIKASI RABIRT V.1.0 (IBU RUMAH TANGGUH)

> **Dokumen Panduan AI Master**: Simpan dan gunakan seluruh isi prompt di dalam blok kode berikut untuk menginstruksikan AI (Antigravity, Claude, ChatGPT, Cursor, GitHub Copilot, dsb.) membangun atau meregenerasi aplikasi **Rabirt v.1.0** dari nol secara 100% identik dan bebas error.

```markdown
Buatkan sebuah aplikasi mobile-first modern, elegan, dan interaktif bernama "Rabirt v.1.0 (Ibu Rumah Tangguh)" — aplikasi manajemen anggaran dan pencatatan kas rumah tangga islami/modern yang lengkap dalam satu berkas HTML (`index.html`) siap bungkus Capacitor/Android APK.

Aplikasi harus dibangun menggunakan stack teknologi:
1. **HTML5 & Vanilla JavaScript (ES6+)**: Bersih, modular, reactive, dan bebas library bundler berat.
2. **Tailwind CSS (via CDN)**: Desain responsif mobile-first, palet warna elegan bertema Emerald/Teal zamrud (`#3C7268`), Slate, Rose, Amber, dan Indigo.
3. **Font Awesome 6 & Custom SVG Icons**: Ikonografi modern, termasuk ikon resmi WhatsApp, koin, dompet, dan navigasi.
4. **Supabase JS Client v2 (via CDN)**: Arsitektur Local-First (100% bekerja offline menggunakan LocalStorage dan auto-sync saat online).
5. **html2pdf.js / jsPDF (via CDN)**: Ekspor dokumen evaluasi kas bulanan ke format PDF resolusi tinggi A4.

Berikut adalah spesifikasi lengkap, alur antarmuka pengguna (UI Flow), arsitektur logo, logika bisnis (business logic), dan sistem langganan yang WAJIB dipenuhi:

---

### 1. ARSITEKTUR DUAL-LOGO & IDENTITAS VISUAL
Aplikasi menggunakan dua logo berbeda untuk dua fungsi spesifik:
1. **Logo 1 (In-App Header Logo)**:
   - Digunakan pada bilah navigasi atas di dalam aplikasi (*in-app header*) di samping tulisan *"RABirt by Ibu Rumah Tangguh"*.
   - Gambar: Bunga lotus hijau, lingkaran putih, dompet biru, dan panah oranye yang melesat ke atas (`header-logo.png`).
   - Penempatan: Berada di dalam badge lingkaran putih bersih berlatar belakang `#FFFFFF` dengan bayangan halus (*box shadow*) agar kontras dan menyatu dengan navbar hijau zamrud.
   - Proporsi: Ukuran logo diperbesar dan posisinya diturunkan sedikit agar simetris dengan teks judul aplikasi.
2. **Logo 2 (Shortcut Display HP / App Launcher Icon)**:
   - Digunakan sebagai ikon shortcut aplikasi yang tampil di layar utama (*home screen*) ponsel Android (`Rabirt-v.1.0.apk`).
   - Gambar: Bunga lotus hijau, lingkaran putih, dompet biru di tengah, dan panah oranye melesat ke atas dengan aksen zigzag merah (sesuai gambar resmi yang diunggah).
   - Implementasi: Dibuat ke dalam seluruh resolusi Android mipmap (`mdpi`, `hdpi`, `xhdpi`, `xxhdpi`, `xxxhdpi`) untuk format standar `ic_launcher.png`, `ic_launcher_round.png`, serta `ic_launcher_foreground.png` dengan background `#FFFFFF`.

---

### 2. ALUR WELCOMING & REGISTRASI PENGGUNA (NO-PASSWORD, NO-GUEST, NO-LOGOUT)
Terapkan siklus hidup antarmuka pengguna (*UI Lifecycle*) yang ramah untuk ibu rumah tangga:
1. **Siklus Hidup (Lifecycle Flow)**:
   - **Pertama kali instal / belum daftar**:
     Aplikasi dibuka → Disambut **Welcoming Page Fullscreen** (klik/tap mana saja di layar) → Halaman **Buat Akun Bunda** (Daftar) → Menekan "Daftar Sekarang" → Masuk ke **Main UI** → Auto-redirect ke **Template Alokasi Persentase Dana** (Setup Modal, hanya sekali) → Pengguna menutup aplikasi.
   - **Membuka aplikasi kembali (sudah terdaftar)**:
     Pengguna membuka aplikasi → Disambut **Welcoming Page** → Auto-redirect (~2 detik) atau tap → Langsung masuk ke **Main UI** (TANPA Register, TANPA Setup Modal).
2. **Halaman Welcoming (Welcoming Page) - FULLSCREEN TEAL (sesuai Gambar 1)**:
   - Seluruh layar (`#3C7268` Pine Green) adalah **touchable/clickable** — klik mana saja berpindah ke Register (belum daftar) atau Main UI (sudah daftar).
   - Tidak ada bottom white card. Seluruh halaman full hijau zamrud.
   - Maskot bundar besar (240px) di bagian atas tengah dengan border putih tipis transparan.
   - Judul emas sangat besar: **RAB**_irt_ (RAB regular, _irt_ italic) warna `#EDB232`, ukuran ~88px.
   - Subtitle: `by Ibu Rumah Tangguh` (putih 88% opacity).
   - Footer info di bagian paling bawah layar (opacity 60%):
     ```
     Aplikasi keuangan resmi untuk komunitas
     Ibu Rumah Tangguh (IRT) Bandung
     RABirt v1.0 2026
     powered by FatherLab
     ```
   - Untuk user terdaftar: tampilkan hint teks _"Membuka aplikasi secara otomatis... 🌸"_ dan auto-redirect 2,2 detik.
   - Untuk user belum daftar: layar kosong (tanpa tombol), cukup tap untuk ke Register.
3. **Halaman Buat Akun Bunda (Register Only) - SESUAI GAMBAR 2**:
   - Background putih bersih.
   - Header lengkung hijau zamrud (`#2D5B52`) dengan indikator titik `•••` dan tombol silang `✕` untuk kembali ke Welcoming Page.
   - Tipografi header:
     ```
     Assalamu'alaikum,     ← italic, opacity 85%, font-size 19px
     Silahkan              ← regular, 30px
     Buat Akun             ← bold 800, 38px
     Bunda!
     ```
   - Tiga (3) kolom input berdesain kapsul/pill dengan border hijau:
     1. 👤 `Nama Lengkap` (ikon orang SVG).
     2. ✉️ `Email` (ikon amplop SVG).
     3. 💬 `No. WA` dengan **Ikon Resmi WhatsApp** SVG (menggantikan ikon gembok).
   - Checkbox persetujuan: `[✓] I agree to the Terms & Privacy`.
   - Tombol aksi: `"Daftar Sekarang"` (dark teal rounded, full-width).
   - **ATURAN WAJIB DIHAPUS**:
     * Hapus total mode login (cukup daftar saja).
     * Hapus kolom isian password.
     * Hapus tulisan pemisah `"or"`.
     * Hapus opsi tautan `"Sudah punya akun....?"`.
     * Hapus tombol `"Keluar"` (Logout) di bagian modal profil Bunda.

---

### 3. FITUR ADJUST & CROP FOTO PROFIL INTERAKTIF
1. Pada modal Profil Pengguna, Bunda dapat mengunggah foto profil dari galeri HP.
2. Sediakan tombol **"Atur Posisi"** yang otomatis muncul setelah foto dipilih/tersimpan.
3. Menekan tombol membuka modal **Adjust Foto Profil**:
   - Area pratinjau lingkaran (*circular crop viewport*).
   - Slider zoom interaktif beserta tombol zoom-in (`+`) dan zoom-out (`-`).
   - Fitur geser/pan (drag) dengan mouse maupun sentuhan jari (*touch screen*) agar foto dapat diposisikan pas di dalam bingkai lingkaran.
   - Tombol "Terapkan Foto" menyimpan hasil penyesuaian ke `rabirt_user_avatar` dan berkas mentah ke `rabirt_raw_avatar`.

---

### 4. KARTU SALDO UTAMA DINAMIS: "SISA DANA BULAN (YANG DIPILIH)"
1. **Judul Kartu Utama Dinamis**:
   - Mengubah judul statis menjadi dinamis mengikuti bulan yang sedang aktif dipilih pada dropdown:
     `SISA DANA BULAN [NAMA_BULAN]` (Contoh: `SISA DANA BULAN MARET` atau `SISA DANA BULAN (YANG DIPILIH)`).
2. **Tampilan & Komponen Periode**:
   - Kartu saldo bergradasi Emerald/Teal mewah ke Cyan lembut.
   - Nominal saldo sisa kas ditampilkan sangat besar, tebal, dan jelas (`Rp xx.xxx.xxx`).
   - Kotak dropdown terintegrasi di dalam kartu untuk memilih Bulan (Januari s.d. Desember) dan Tahun (hingga 2030).
3. **Pembersihan Teks Usang**:
   - Menghapus tulisan `"Format 20/......"`.
   - Menghapus tulisan `"mendukung .........."`.

---

### 5. ARSITEKTUR ANGGARAN 6 ZONA & LOGIKA BISNIS KRUSIAL
Aplikasi membagi alokasi kas bulanan ke dalam 6 Zona Keuangan:
1. **Pemasukan**:
   - Total seluruh pemasukan keluarga (Gaji Pokok, Nafkah, Usaha).
   - Memiliki mekanisme akumulasi otomatis dari catatan Rezeki Tak Terduga (LOP).
2. **Ziswaf (Zakat, Infaq, Sedekah, Wakaf)**:
   - Alokasi kebaikan dan sosial. Default rasio template adalah 20% dari Pemasukan.
3. **Investasi / Tabungan Masa Depan**:
   - Alokasi tabungan, dana darurat, dan investasi. Default rasio template adalah 30% dari Pemasukan.
4. **Pengeluaran / Kebutuhan Pokok**:
   - Alokasi belanja dapur, operasional, tagihan bulanan. Default rasio template adalah 50% dari Pemasukan.
5. **LOP (Law of Projection / Rezeki Tak Terduga)**:
   - Wadah pencatatan rezeki tak terduga yang datang tiba-tiba.
   - **ATURAN LOGIKA KRUSIAL**: Setiap nominal transaksi yang dicatat pada kategori LOP **OTOMATIS DITAMBAHKAN** ke total Zona Pemasukan.
6. **Mari Syukuri (Zona Bersyukur)**:
   - Ruang pencatatan rasa syukur dan sedekah syukur atas nikmat yang diterima.
   - **ATURAN LOGIKA KRUSIAL**: Nominal di Zona Bersyukur **TIDAK BOLEH DITAMBAHKAN** ke Zona Pemasukan agar kas utama tidak terdistorsi (murni pencatatan spiritual/evaluasi rasa syukur).

---

### 6. BOTTOM NAVIGATION BAR & SIMPLIFIKASI TOMBOL "SIMPAN"
1. **5 Menu Bilah Navigasi Bawah**:
   - `Utama`: Kartu Sisa Dana Kas dan 6 Kartu Zona Anggaran.
   - `Riwayat`: Arsip riwayat laporan keuangan bulanan yang tersimpan.
   - `Preview`: Pratinjau lembar evaluasi resmi bulanan sebelum dicetak/diunduh.
   - `Transaksi`: Buku kas transaksi harian lengkap dengan ringkasan 3 kolam dana, filter chip warna per zona, dan fitur edit/hapus transaksi.
   - `Template`: Modal penyesuaian persentase alokasi anggaran (20/30/50).
2. **Floating Action Button (FAB)**:
   - Tombol bulat hijau dengan ikon `+` di sudut kanan bawah (`fixed bottom-20 right-5 z-40`) untuk membuka modal Catat Transaksi secara cepat dari tab Utama dan Transaksi.
3. **Penyederhanaan Tombol Aksi Modal**:
   - Tombol submit/eksekusi pada modal transaksi kas disederhanakan secara konsisten dan ringkas menjadi:
     `💾 Simpan`
   - (Tidak menggunakan teks panjang seperti "Simpan Pemasukan Pokok" atau "Simpan Zona Bersyukur" agar tidak terpotong di layar HP).

---

### 7. SISTEM SUBSCRIPTION & TOKEN 10 KARAKTER (36 TOKEN + 1 MASTER LIFETIME)
1. **Trial Gratis 1 Hari (24 Jam)**:
   - Setiap pengguna baru yang mendaftar (tap "Daftar Sekarang") langsung mendapatkan masa aktif trial **1 hari (24 jam)**.
   - **Tombol "Daftar Sekarang" adalah trigger aktivasi trial mode.**
   - Setelah 24 jam kedaluwarsa, aplikasi otomatis mengunci tampilan dengan modal *Subscription Lock* yang menyediakan form aktivasi token dan tombol pembelian ke WhatsApp Admin.
   - Semua fitur terkunci **kecuali modal Profil** tempat memasukkan token (dapat diakses langsung via tombol *"Buka Profil Bunda (Input Token)"* di modal lock).
   - Tautan Resmi Perpanjangan/Pembelian Token Langganan:
     `https://wa.link/bfp48a` (tersambung langsung ke WhatsApp Admin).
   - Tampilan Label Paket Bersih & Ringkas (tanpa teks dalam kurung yang terpotong):
     * `Trial 1 Hari`
     * `Paket 1 Bulan`
     * `Paket 3 Bulan`
     * `Paket 6 Bulan`
     * `Master Admin`
2. **Daftar Token Rahasia 10 Karakter** (Kombinasi Huruf Besar, Huruf Kecil, Angka, & Karakter Spesial):
   - **24 Token Bulanan (Masa Aktif 30 Hari)**:
     `%YV5yT8$*f`, `!aiBsn4wp?`, `Ygu%xK&R3g`, `*Pg4hgHcbR`, `jz7U*7kZ9A`, `+&P6jippNu`, `*Ffcx86tm!`, `@5ubGPpY2p`, `nzV5A$&YCA`, `?$Mf&f5e42`, `6n@67T8CZV`, `Dv9!hvpF?8`, `@nu%XTX2&S`, `xk?qS3vQ8U`, `F*rTeR7fQJ`, `2i6?3Qv%BR`, `9UUVP?wMxx`, `+g!b5kTzJw`, `Zy4d?pZJLK`, `G#Ux7qx?7J`, `is$UJD6rrr`, `4%dfSD?GV3`, `Nx5u+2jE!z`, `?zzT&6je*N`.
   - **8 Token Khusus 3 Bulan (Masa Aktif 90 Hari)**:
     `3M#xK9$qWz`, `vB8*pT!4Lm`, `9J&yR2%sFa`, `dZ7$wE+8Nk`, `4Q!sH5#vTg`, `rN6%kM*9Pq`, `2W@xL7?bVj`, `8Y$mC3!tFx`.
   - **4 Token Khusus 6 Bulan (Masa Aktif 180 Hari)**:
     `gLL8ph$@$p`, `5W+xs8nG9?`, `6uGY#3cbd$`, `a4vV!7yXh2`.
   - **1 Token Master Lifetime Khusus Admin (Akses Selamanya Tanpa Batas Waktu)**:
     `FL#Adm99!X`.
3. **Mekanisme Validasi Token & Kunci Perangkat (Device-Level Lock)**:
   - **Kunci Perangkat**: Setiap token yang telah terpakai otomatis dicatat ke dalam penyimpanan lokal permanen (`rabirt_device_used_tokens`) sehingga **tidak dapat digunakan kembali di perangkat yang sama**.
   - Input token tidak membedakan spasi tak sengaja (auto-trim).
4. **Setup Modal (Alokasi Persentase Dana)**:
   - Hanya ditampilkan SEKALI saat pertama kali registrasi → ke main UI → auto-open setup modal.
   - Pada buka ulang aplikasi: welcoming page → main UI (TIDAK ada setup modal).
   - Flag disimpan: `localStorage.setItem('rabirt_first_setup_done', 'true')`.
   - **Label Bersih**: Menghapus kata `Kolam (%)` sehingga label menjadi **`Alokasi Persentase`**.
   - **Presisi Input Horisontal**: Kolom input persentase ZISWAF, Investasi, dan Pengeluaran (20%, 30%, 50%) ditata sejajar secara horisontal 100% lurus di semua resolusi layar ponsel dengan tinggi label standar (`min-height: 32px`) dan penataan kartu flexbox.
   - **Pembersihan Konfigurasi Supabase**: Tombol *"Konfigurasi Supabase"* dan modal pengaturannya dihapus dari antarmuka pengguna agar tampilan tetap bersih, ramah pengguna awam, dan bebas kebingungan teknis.

5. **Pengingat Sedekah Subuh (Pop-up Layar Ponsel Interaktif)**:
   - Banner statis atas serta tombol `+ Catat di Zona Bersyukur` telah dihapus sepenuhnya.
   - Digantikan dengan **Pop-up Modal Layar Penuh Ramah Pengguna** (`#modalSedekahSubuhPopUp`) yang muncul otomatis pada jam Subuh (04:00 - 06:30).
   - Dilengkapi kutipan hadits keutamaan doa malaikat di waktu subuh, animasi ikon fajar/matahari terbit, dan tombol penutup doa *"Aamiin, Tutup"*.

---

### 8. DOKUMEN LAPORAN EVALUASI ANGGARAN KAS (PRATINJAU & PDF)
Lembar evaluasi anggaran kas bulanan (Preview & Ekspor PDF) memiliki tata letak formal, elegan, dan rapi:
1. **Pembersihan Teks**:
   - Menghapus tulisan `"by Ibu Rumah Tangguh"` pada bagian footer dokumen. Footer tampil bersih hanya menampilkan tulisan `app powered by FatherLab` di sebelah kanan.
   - Menghapus tulisan `"by Ibu Rumah Tangguh"` di bawah judul utama laporan *"Laporan Evaluasi Anggaran Kas"*.
2. **Identitas Akun Dinamis ("Bunda ...(nama akun)")**:
   - Sub-badge di samping badge pill brand `[Ibu Rumah Tangguh]` yang sebelumnya bertuliskan *"Laporan Kas Rumah Tangga"* diganti menjadi nama dinamis akun pengguna:
     `Bunda ...(nama akun)` (Contoh: `Bunda Idn Soplanto` sesuai nama akun yang didaftarkan).
3. **Grafik Batang Isometrik 3D 6 Zona Berdampingan**:
   - Menampilkan 6 pilar batang isometrik 3D berwarna berdampingan:
     1. 🟢 **Pemasukan** (`#10B981`)
     2. 🟣 **LOP (Rezeki Tak Terduga)** (`#8B5CF6`)
     3. 🩵 **Zona Bersyukur** (`#0D9488`)
     4. 🔵 **ZISWAF** (`#3B82F6`)
     5. 🟠 **Investasi** (`#F97316`)
     6. 🔴 **Pengeluaran** (`#EF4444`)
4. **Tabel Alokasi Zona Anggaran Kas**:
   - Di bawah baris Pemasukan, langsung disisipkan baris LOP dan Zona Bersyukur:
     * **Pemasukan**: Realisasi Pemasukan Pokok.
     * **LOP**: Target `100%`, Alokasi Dana, Terpakai `Rp 0`, Realisasi `100%`, Sisa Saldo.
     * **Zona Bersyukur**: Target `100%`, Alokasi Dana, Terpakai `Rp 0`, Realisasi `100%`, Sisa Saldo.
     * **ZISWAF**: Target alokasi 20%, dana terpakai, realisasi, sisa.
     * **Investasi**: Target alokasi 30%, dana terpakai, realisasi, sisa.
     * **Pengeluaran**: Target alokasi 50%, dana terpakai, realisasi, sisa.

---

### 9. JEMBATAN NATIVE ANDROID BRIDGE (DOWNLOAD KE HP, LOKASI NOTIFIKASI & WEB-FIRST WA)
1. **Penyimpanan Dokumen PDF ke HP & Akses Allow Penuh**:
   - Diberikan izin penyimpanan resmi di Android Manifest: `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE`, `POST_NOTIFICATIONS`, serta `android:requestLegacyExternalStorage="true"`.
   - Menggunakan `MediaStore.Downloads` (Android 10+) dan berkas fisik publik di folder **`Download/RABirt/`** perangkat Android.
   - Dilengkapi fallback direktori aplikasi terproteksi sehingga berkas 100% selalu tersimpan dan dapat diakses FileProvider tanpa pernah mengalami error *Permission Denied (EACCES)*.
   - **Notifikasi Lokasi Berkas**: Memberikan transparansi lokasi penyimpanan kepada Bunda:
     * In-App Dialog / Modal: Menampilkan lokasi jelas `Penyimpanan Internal > Download > RABirt > [nama_file.pdf]`.
     * Android Native Alert Dialog dengan tombol langsung *"Buka Dokumen"*.
     * Android System Status Bar Notification di tirai notifikasi ponsel pengguna.
2. **Penghubung WhatsApp Web-First (Anti-Blokir Perangkat)**:
   - Mengatasi kendala sebagian ponsel pintar (Xiaomi, Oppo, Vivo, dsb.) yang memblokir tautan langsung atau *deep link*.
   - Mengarahkan terlebih dahulu ke halaman web perantara (`wa-redirect.html` / browser eksternal via `CATEGORY_BROWSABLE`), lalu otomatis menghubungkan ke WhatsApp:
     * Tautan resmi: `https://wa.link/bfp48a`
     * Alternatif Direct API: `https://api.whatsapp.com/send?phone=6285150782500...`
     * Dilengkapi fitur satu kali klik untuk menyalin nomor WhatsApp Admin (`0851-5078-2500`).

---

### 10. KEAMANAN & PENYIMPANAN LOCAL-FIRST
- Seluruh data akun (`rabirt_accounts`), sesi aktif (`rabirt_active_session`), transaksi (`rabirt_transactions`), alokasi (`rabirt_settings`), dan langganan (`rabirt_subscription_info`) tersimpan secara instan di `localStorage`.
- Kompatibel penuh dengan skema Supabase PostgreSQL (`profiles`, `user_settings`, `transactions`) jika internet terhubung.
```
