# UTS PBM 2026 — Kasus 6: Pencatatan Penjualan Ikan di TPI

Fitur
- Pencatatan tongkol, tuna, kerapu, dan cumi.
- Validasi berat > 0 hingga 500 kg serta harga positif.
- Mutu kurang segar menurunkan harga 25%.
- Retribusi TPI 5% dan bagian pengepul 10% dari pendapatan kotor.
- Ringkasan dan penghapusan transaksi.

Struktur kode
- `Logika.kt`: data class, validasi, perhitungan dan ringkasan.
- `MainActivity.kt`: state, formulir, daftar, kartu transaksi, ringkasan.
- `strings.xml`: nama aplikasi.
- `drawable/ic_fish.xml`: ikon ikan vektor.

