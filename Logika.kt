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

fun hitungPenjualan(entri: PenjualanIkan): HasilPenjualan {
    val harga = when (entri.mutu) {
        "Kurang segar" -> entri.hargaPerKg * 0.75
        else -> entri.hargaPerKg
    }
    return HasilPenjualan(harga, entri.beratKg * harga)
}

fun hitungRingkasan(daftar: List<PenjualanIkan>): Ringkasan {
    val nilai = daftar.map { it to hitungPenjualan(it).nilaiPenjualan }
    val beratPerJenis = daftar.groupBy { it.jenis }
        .mapValues { (_, item) -> item.sumOf { it.beratKg } }
    val nilaiPerJenis = nilai.groupBy { it.first.jenis }
        .mapValues { (_, item) -> item.sumOf { it.second } }
    val totalBerat = daftar.sumOf { it.beratKg }
    val kotor = nilai.sumOf { it.second }
    val jenisTerbesar = nilaiPerJenis.maxByOrNull { it.value }?.key ?: "-"
    val retribusi = kotor * 0.05
    val pengepul = kotor * 0.10
    return Ringkasan(totalBerat, kotor, retribusi, pengepul,
        kotor - retribusi - pengepul, beratPerJenis, nilaiPerJenis, jenisTerbesar)
}

fun validasiPenjualan(
    jenis: String, beratTeks: String, hargaTeks: String, mutu: String
): String? {
    val berat = beratTeks.replace(',', '.').toDoubleOrNull()
        ?: return "Berat harus berupa angka."
    val harga = hargaTeks.replace(',', '.').toDoubleOrNull()
        ?: return "Harga harus berupa angka."
    if (jenis !in listOf("Tongkol", "Tuna", "Kerapu", "Cumi")) return "Pilih jenis ikan."
    if (berat <= 0.0 || berat > 500.0) return "Berat harus lebih dari 0 dan maksimal 500 kg."
    if (harga <= 0.0) return "Harga per kg harus lebih dari 0."
    if (mutu !in listOf("Segar", "Kurang segar")) return "Pilih mutu ikan."
    return null
}
