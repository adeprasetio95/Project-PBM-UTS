# UTS PBM 2026 — Kasus 6: Pencatatan Penjualan Ikan di TPI

## Identitas kelompok
- Nomor kelompok: [isi nomor kelompok]
- Anggota, NIM, akun GitHub: [lengkapi seluruh anggota]
- Repositori: [tempel URL repositori]
- Video YouTube: [tempel URL video]

## Fitur
- Pencatatan tongkol, tuna, kerapu, dan cumi.
- Validasi berat > 0 hingga 500 kg serta harga positif.
- Mutu kurang segar menurunkan harga 25%.
- Retribusi TPI 5% dan bagian pengepul 10% dari pendapatan kotor.
- Ringkasan dan penghapusan transaksi.

## Menjalankan
1. Buka folder proyek di Android Studio versi stabil yang mendukung Android Gradle Plugin 8.7.3.
2. Tunggu Gradle Sync.
3. Jalankan aplikasi pada emulator atau perangkat Android.

## Struktur kode
- `Logika.kt`: data class, validasi, perhitungan dan ringkasan.
- `MainActivity.kt`: state, formulir, daftar, kartu transaksi, ringkasan.
- `strings.xml`: nama aplikasi.
- `drawable/ic_fish.xml`: ikon ikan vektor.

## Catatan
Data hanya disimpan di state selama aplikasi berjalan, sesuai cakupan UTS. Lengkapi identitas, kontribusi commit tiap anggota, screenshot, dan tautan video sebelum pengumpulan.
