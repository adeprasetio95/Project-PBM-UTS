package com.example.tpi

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/** Database SQLite lokal. Data tetap tersimpan walaupun aplikasi ditutup. */
class PenyimpananDatabase(context: Context) :
    SQLiteOpenHelper(context, NAMA_DATABASE, null, VERSI_DATABASE) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE $TABEL_PENJUALAN (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                jenis TEXT NOT NULL,
                berat_kg REAL NOT NULL,
                harga_per_kg REAL NOT NULL,
                mutu TEXT NOT NULL
            )""".trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Jika skema berubah di versi mendatang, migrasi database ditambahkan di sini.
    }

    fun tambah(entri: PenjualanIkan): PenjualanIkan {
        val values = ContentValues().apply {
            put("jenis", entri.jenis)
            put("berat_kg", entri.beratKg)
            put("harga_per_kg", entri.hargaPerKg)
            put("mutu", entri.mutu)
        }
        val idBaru = writableDatabase.insertOrThrow(TABEL_PENJUALAN, null, values).toInt()
        return entri.copy(id = idBaru)
    }

    fun ambilSemua(): List<PenjualanIkan> {
        val hasil = mutableListOf<PenjualanIkan>()
        readableDatabase.query(
            TABEL_PENJUALAN,
            arrayOf("id", "jenis", "berat_kg", "harga_per_kg", "mutu"),
            null, null, null, null, "id DESC"
        ).use { cursor ->
            while (cursor.moveToNext()) {
                hasil.add(
                    PenjualanIkan(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        jenis = cursor.getString(cursor.getColumnIndexOrThrow("jenis")),
                        beratKg = cursor.getDouble(cursor.getColumnIndexOrThrow("berat_kg")),
                        hargaPerKg = cursor.getDouble(cursor.getColumnIndexOrThrow("harga_per_kg")),
                        mutu = cursor.getString(cursor.getColumnIndexOrThrow("mutu"))
                    )
                )
            }
        }
        return hasil
    }

    fun hapus(id: Int): Boolean =
        writableDatabase.delete(TABEL_PENJUALAN, "id = ?", arrayOf(id.toString())) > 0

    companion object {
        private const val NAMA_DATABASE = "tpi_penjualan.db"
        private const val VERSI_DATABASE = 1
        private const val TABEL_PENJUALAN = "penjualan_ikan"
    }
}
