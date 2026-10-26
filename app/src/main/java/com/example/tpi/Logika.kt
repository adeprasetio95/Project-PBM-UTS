package com.example.tpi

data class PenjualanIkan(
    val id: Int,
    val jenis: String,
    val beratKg: Double,
    val hargaPerKg: Double,
    val mutu: String
)

data class HasilPenjualan(
    val hargaDisesuaikan: Double,
    val nilaiPenjualan: Double
)

data class Ringkasan(
    val totalBerat: Double,
    val pendapatanKotor: Double,
    val retribusi: Double,
    val bagianPengepul: Double,
    val pendapatanBersih: Double,
    val beratPerJenis: Map<String, Double>,
    val nilaiPerJenis: Map<String, Double>,
    val jenisTerbesar: String
)

enum class KesalahanValidasi {
    JENIS, BERAT_ANGKA, HARGA_ANGKA, BERAT_RENTANG, HARGA_RENTANG, MUTU
}

fun hitungPenjualan(entri: PenjualanIkan): HasilPenjualan {
    val hargaDisesuaikan = when (entri.mutu) {
        "Kurang segar" -> entri.hargaPerKg * 0.75
        else -> entri.hargaPerKg
    }
    return HasilPenjualan(
        hargaDisesuaikan = hargaDisesuaikan,
        nilaiPenjualan = entri.beratKg * hargaDisesuaikan
    )
}

fun hitungRingkasan(daftar: List<PenjualanIkan>): Ringkasan {
    val hasilPerEntri = daftar.map { entri -> entri to hitungPenjualan(entri) }
    val beratPerJenis = daftar.groupBy { it.jenis }
        .mapValues { (_, entri) -> entri.sumOf { it.beratKg } }
    val nilaiPerJenis = hasilPerEntri.groupBy { it.first.jenis }
        .mapValues { (_, hasil) -> hasil.sumOf { it.second.nilaiPenjualan } }
    val totalBerat = daftar.sumOf { it.beratKg }
    val pendapatanKotor = hasilPerEntri.sumOf { it.second.nilaiPenjualan }
    val retribusi = pendapatanKotor * 0.05
    val bagianPengepul = pendapatanKotor * 0.10
    val jenisTerbesar = nilaiPerJenis.maxByOrNull { it.value }?.key ?: "Datanya Masih Kosong"
    return Ringkasan(
        totalBerat = totalBerat,
        pendapatanKotor = pendapatanKotor,
        retribusi = retribusi,
        bagianPengepul = bagianPengepul,
        pendapatanBersih = pendapatanKotor - retribusi - bagianPengepul,
        beratPerJenis = beratPerJenis,
        nilaiPerJenis = nilaiPerJenis,
        jenisTerbesar = jenisTerbesar
    )
}

fun validasiPenjualan(
    jenis: String,
    beratTeks: String,
    hargaTeks: String,
    mutu: String
): KesalahanValidasi? {
    val berat = beratTeks.trim().replace(',', '.').toDoubleOrNull()
        ?: return KesalahanValidasi.BERAT_ANGKA
    val harga = hargaTeks.trim().replace(',', '.').toDoubleOrNull()
        ?: return KesalahanValidasi.HARGA_ANGKA
    if (jenis !in listOf("Tongkol", "Tuna", "Kerapu", "Cumi")) return KesalahanValidasi.JENIS
    if (berat <= 0.0 || berat > 500.0) return KesalahanValidasi.BERAT_RENTANG
    if (harga <= 0.0) return KesalahanValidasi.HARGA_RENTANG
    if (mutu !in listOf("Segar", "Kurang segar")) return KesalahanValidasi.MUTU
    return null
}

fun KesalahanValidasi.pesan(): String = when (this) {
    KesalahanValidasi.JENIS -> "Jenis ikan tidak valid."
    KesalahanValidasi.BERAT_ANGKA -> "Berat harus berupa angka."
    KesalahanValidasi.HARGA_ANGKA -> "Harga harus berupa angka."
    KesalahanValidasi.BERAT_RENTANG -> "Berat harus lebih dari 0 dan maksimal 500 kg."
    KesalahanValidasi.HARGA_RENTANG -> "Harga harus lebih dari 0."
    KesalahanValidasi.MUTU -> "Mutu tidak valid."
}
