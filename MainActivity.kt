package com.example.tpi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { LayarTPI() } }
    }
}

private fun rupiah(n: Double): String =
    NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(n)

@Composable
fun LayarTPI() {
    var jenis by rememberSaveable { mutableStateOf("Tongkol") }
    var berat by rememberSaveable { mutableStateOf("") }
    var harga by rememberSaveable { mutableStateOf("") }
    var mutu by rememberSaveable { mutableStateOf("Segar") }
    var error by remember { mutableStateOf<String?>(null) }
    var idBerikut by remember { mutableIntStateOf(1) }
    var daftar by remember { mutableStateOf(listOf<PenjualanIkan>()) }
    val ringkasan = remember(daftar) { hitungRingkasan(daftar) }
    val valid = validasiPenjualan(jenis, berat, harga, mutu) == null
    var itemAkanDihapus by remember { mutableStateOf<PenjualanIkan?>(null) }

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Image(painterResource(R.drawable.ic_fish), contentDescription = "Ikon ikan", modifier = Modifier.size(44.dp))
            Column {
                Text("TPI • Penjualan Ikan", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Pencatatan transaksi dan pendapatan harian", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Spacer(Modifier.height(16.dp))
        FormulirPenjualan(
            jenis = jenis,
            onJenisChange = { jenis = it; error = null },
            berat = berat,
            onBeratChange = { berat = it; error = null },
            harga = harga,
            onHargaChange = { harga = it; error = null },
            mutu = mutu,
            onMutuChange = { mutu = it; error = null },
            error = error,
            valid = valid,
            onTambah = {
                val pesan = validasiPenjualan(jenis, berat, harga, mutu)
                if (pesan != null) error = pesan else {
                    daftar = daftar + PenjualanIkan(idBerikut, jenis,
                        berat.replace(',', '.').toDouble(), harga.replace(',', '.').toDouble(), mutu)
                    idBerikut++
                    berat = ""; harga = ""; mutu = "Segar"; jenis = "Tongkol"; error = null
                }
            }
        )
        Spacer(Modifier.height(12.dp))
        Text("Ringkasan harian", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                BarisRingkasan("Total berat", "%.2f kg".format(ringkasan.totalBerat))
                BarisRingkasan("Pendapatan kotor", rupiah(ringkasan.pendapatanKotor))
                BarisRingkasan("Retribusi TPI (5%)", rupiah(ringkasan.retribusi))
                BarisRingkasan("Bagian pengepul (10%)", rupiah(ringkasan.bagianPengepul))
                HorizontalDivider()
                BarisRingkasan("Pendapatan bersih", rupiah(ringkasan.pendapatanBersih), true)
                BarisRingkasan("Nilai terbesar", ringkasan.jenisTerbesar)
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("Daftar penjualan (${daftar.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        if (daftar.isEmpty()) {
            Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Text("Belum ada penjualan. Tambahkan data pertama di atas.")
            }
        } else {
            LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(daftar, key = { it.id }) { entri -> KartuPenjualan(entri, onHapus = {
                    itemAkanDihapus = entri }
                }) }
            }
        }
    }
if (itemAkanDihapus != null) {
    AlertDialog(
        onDismissRequest = { itemAkanDihapus = null },
        title = { Text("Konfirmasi Hapus") },
        text = { Text("Apakah Anda yakin ingin menghapus data ${itemAkanDihapus?.jenis}?") },
        confirmButton = {
            TextButton(onClick = {
                daftar = daftar.filterNot { it.id == itemAkanDihapus?.id }
                itemAkanDihapus = null
            }) { Text("Hapus") }
        },
        dismissButton = {
            TextButton(onClick = { itemAkanDihapus = null }) { Text("Batal") }
        }
    )
}
}

@Composable
fun BarisRingkasan(label: String, nilai: String, tebal: Boolean = false) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontWeight = if (tebal) FontWeight.Bold else FontWeight.Normal)
        Text(nilai, fontWeight = if (tebal) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun FormulirPenjualan(
    jenis: String,
    onJenisChange: (String) -> Unit,
    berat: String,
    onBeratChange: (String) -> Unit,
    harga: String,
    onHargaChange: (String) -> Unit,
    mutu: String,
    onMutuChange: (String) -> Unit,
    error: String?,
    valid: Boolean,
    onTambah: () -> Unit
) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Tambah penjualan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("Jenis ikan")
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("Tongkol", "Tuna", "Kerapu", "Cumi").forEach { item ->
                    FilterChip(selected = jenis == item, onClick = { onJenisChange(item) },
                        label = { Text(item) })
                }
            }
            OutlinedTextField(value = berat, onValueChange = { onBeratChange(it) },
                label = { Text("Berat (kg)") }, modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true)
            OutlinedTextField(value = harga, onValueChange = { onHargaChange(it) },
                label = { Text("Harga per kg (Rp)") }, modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
            Text("Mutu ikan")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Segar", "Kurang segar").forEach { item ->
                    FilterChip(selected = mutu == item, onClick = { onMutuChange(item) },
                        label = { Text(item) })
                }
            }
            if (mutu == "Kurang segar") Text("Harga otomatis dikurangi 25%.", color = MaterialTheme.colorScheme.error)
            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            Button(onClick = onTambah, enabled = valid, modifier = Modifier.fillMaxWidth()) { Text("Tambah penjualan") }
        }
    }
}
