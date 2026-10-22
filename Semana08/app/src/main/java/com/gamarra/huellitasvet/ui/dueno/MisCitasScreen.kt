package com.gamarra.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamarra.huellitasvet.data.local.relation.CitaConDetalle
import com.gamarra.huellitasvet.ui.components.CitaCard
import com.gamarra.huellitasvet.ui.components.fechaLarga
import com.gamarra.huellitasvet.viewmodel.MisCitasViewModel

@Composable
fun MisCitasScreen(vm: MisCitasViewModel, onPedirCita: () -> Unit, onReprogramar: (Int) -> Unit, modifier: Modifier = Modifier) {
    val proximas by vm.proximas.collectAsState()
    val historial by vm.historial.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    var pestana by remember { mutableIntStateOf(0) }
    var porCancelar by remember { mutableStateOf<CitaConDetalle?>(null) }
    val colores = MaterialTheme.colorScheme
    val lista = if (pestana == 0) proximas else historial

    Box(modifier.fillMaxSize().background(colores.background)) {
        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Text("Mis citas", fontSize = 28.sp, fontWeight = FontWeight.Bold) }
            item {
                TabRow(selectedTabIndex = pestana, containerColor = colores.background) {
                    Tab(selected = pestana == 0, onClick = { pestana = 0 }, text = { Text("Próximas (${proximas.size})") })
                    Tab(selected = pestana == 1, onClick = { pestana = 1 }, text = { Text("Historial") })
                }
            }
            if (pestana == 0) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colores.primaryContainer, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Icon(Icons.Outlined.Info, contentDescription = null, tint = colores.primary, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Puedes cancelar o reprogramar hasta 2 horas antes", color = colores.onPrimaryContainer, fontSize = 14.sp)
                    }
                }
            }
            mensaje?.let { item { Text(it, color = colores.error, fontSize = 14.sp) } }
            if (lista.isEmpty()) {
                item {
                    Text(
                        if (pestana == 0) "No tienes citas próximas" else "Aún no hay citas en tu historial",
                        color = colores.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
            items(lista, key = { it.cita.id }) { c ->
                CitaCard(c) {
                    if (pestana == 0 && vm.puedeCancelar(c)) {
                        Row(Modifier.align(Alignment.End)) {
                            TextButton(onClick = { onReprogramar(c.cita.id) }) { Text("Reprogramar") }
                            TextButton(onClick = { porCancelar = c }) { Text("Cancelar", color = colores.error) }
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = onPedirCita,
            containerColor = colores.primary,
            contentColor = colores.onPrimary,
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Pedir cita")
        }
    }

    porCancelar?.let { c ->
        AlertDialog(
            onDismissRequest = { porCancelar = null },
            title = { Text("Cancelar cita") },
            text = {
                Text("¿Cancelar ${c.servicio.nombre} de ${c.mascota.mascota.nombre} el ${fechaLarga(c.cita.fecha)} a las ${c.cita.hora}?")
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.cancelar(c)
                    porCancelar = null
                }) { Text("Sí, cancelar", color = colores.error) }
            },
            dismissButton = { TextButton(onClick = { porCancelar = null }) { Text("Volver") } }
        )
    }
}
