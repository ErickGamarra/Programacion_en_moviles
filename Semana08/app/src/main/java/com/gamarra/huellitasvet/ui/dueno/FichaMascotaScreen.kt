package com.gamarra.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamarra.huellitasvet.data.local.entity.Mascota
import com.gamarra.huellitasvet.ui.components.HistorialItem
import com.gamarra.huellitasvet.ui.components.MascotaAvatar
import com.gamarra.huellitasvet.ui.components.VetButton
import com.gamarra.huellitasvet.ui.components.edadTexto
import com.gamarra.huellitasvet.ui.components.nombreEspecie
import com.gamarra.huellitasvet.ui.components.nombreSexo
import com.gamarra.huellitasvet.ui.components.pesoTexto
import com.gamarra.huellitasvet.viewmodel.MascotaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FichaMascotaScreen(
    vm: MascotaViewModel,
    mascotaId: Int,
    onVolver: () -> Unit,
    onEditar: (() -> Unit)? = null,
    onPedirCita: (() -> Unit)? = null
) {
    val mascota by remember(mascotaId) { vm.mascota(mascotaId) }.collectAsState()
    val historial by remember(mascotaId) { vm.historial(mascotaId) }.collectAsState()
    var pestana by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ficha") },
                navigationIcon = {
                    IconButton(onClick = onVolver) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        val m = mascota ?: return@Scaffold
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Cabecera(m) }
            if (onPedirCita != null && onEditar != null) {
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        VetButton("Pedir cita", onPedirCita, Modifier.weight(1f))
                        VetButton("Editar", onEditar, Modifier.weight(1f), contorno = true)
                    }
                }
            }
            item {
                TabRow(selectedTabIndex = pestana, containerColor = MaterialTheme.colorScheme.background) {
                    Tab(selected = pestana == 0, onClick = { pestana = 0 }, text = { Text("Datos") })
                    Tab(selected = pestana == 1, onClick = { pestana = 1 }, text = { Text("Historial clínico") })
                }
            }
            if (pestana == 0) {
                item { Datos(m) }
            } else if (historial.isEmpty()) {
                item { Text("Aún no tiene atenciones", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            } else {
                itemsIndexed(historial, key = { _, it -> it.cita.id }) { i, atencion ->
                    HistorialItem(atencion, esUltimo = i == historial.lastIndex)
                }
            }
        }
    }
}

@Composable
private fun Cabecera(m: Mascota) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        MascotaAvatar(m.especie, tamano = 88.dp)
        Column(Modifier.padding(start = 16.dp)) {
            Text(m.nombre, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text("${nombreEspecie(m.especie)} · ${m.raza}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                "${edadTexto(m.anioNacimiento)} · ${pesoTexto(m.peso)}",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun Datos(m: Mascota) {
    val filas = listOf(
        "Especie" to nombreEspecie(m.especie),
        "Raza" to m.raza.ifBlank { "—" },
        "Sexo" to nombreSexo(m.sexo),
        "Año de nacimiento" to m.anioNacimiento.toString(),
        "Edad" to edadTexto(m.anioNacimiento),
        "Peso actual" to pesoTexto(m.peso)
    )
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(horizontal = 16.dp)) {
            filas.forEachIndexed { i, (etiqueta, valor) ->
                Row(Modifier.fillMaxWidth().padding(vertical = 14.dp)) {
                    Text(etiqueta, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                    Text(valor, fontWeight = FontWeight.Medium)
                }
                if (i < filas.lastIndex) HorizontalDivider()
            }
        }
    }
}
