package com.gamarra.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamarra.huellitasvet.data.local.relation.CitaConDetalle
import com.gamarra.huellitasvet.model.EstadoCita
import com.gamarra.huellitasvet.ui.components.EstadoCitaChip
import com.gamarra.huellitasvet.ui.components.FechaChip
import com.gamarra.huellitasvet.ui.components.MascotaAvatar
import com.gamarra.huellitasvet.ui.components.fechaLarga
import com.gamarra.huellitasvet.viewmodel.AgendaViewModel

@Composable
fun AgendaScreen(vm: AgendaViewModel, onAtender: (Int) -> Unit, modifier: Modifier = Modifier) {
    val fecha by vm.fechaSeleccionada.collectAsState()
    val citas by vm.citas.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val colores = MaterialTheme.colorScheme

    LazyColumn(
        modifier = modifier.fillMaxSize().background(colores.background),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Agenda", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(fechaLarga(fecha.toString()), color = colores.onSurfaceVariant)
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(vm.fechas) { f -> FechaChip(f, seleccionado = f == fecha) { vm.elegirFecha(f) } }
            }
        }
        mensaje?.let { item { Text(it, color = colores.error, fontSize = 14.sp) } }
        if (citas.isEmpty()) {
            item { Text("No hay citas para este día", color = colores.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp)) }
        }
        items(citas, key = { it.cita.id }) { c ->
            FilaAgenda(c, onConfirmar = { vm.confirmar(c) }, onAtender = { onAtender(c.cita.id) }, onCancelar = { vm.cancelar(c) })
        }
    }
}

@Composable
private fun FilaAgenda(c: CitaConDetalle, onConfirmar: () -> Unit, onAtender: () -> Unit, onCancelar: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    val estado = c.cita.estado
    Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        Column(Modifier.width(52.dp).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(c.cita.hora, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(top = 14.dp))
            Box(Modifier.padding(top = 6.dp).width(2.dp).weight(1f).background(colores.primaryContainer))
        }
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colores.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.weight(1f).padding(start = 8.dp)
        ) {
            Column(Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MascotaAvatar(c.mascota.mascota.especie, tamano = 44.dp)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(c.servicio.nombre, fontWeight = FontWeight.SemiBold)
                        Text(
                            "${c.mascota.mascota.nombre} · ${c.mascota.dueno.nombre}",
                            color = colores.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                    }
                    EstadoCitaChip(estado)
                }
                if (estado == EstadoCita.PENDIENTE.name || estado == EstadoCita.CONFIRMADA.name) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        TextButton(onClick = onCancelar) { Text("Cancelar", color = colores.error) }
                        Spacer(Modifier.width(4.dp))
                        if (estado == EstadoCita.PENDIENTE.name) {
                            Button(onClick = onConfirmar, shape = RoundedCornerShape(12.dp)) { Text("Confirmar") }
                        } else {
                            Button(onClick = onAtender, shape = RoundedCornerShape(12.dp)) { Text("Atender") }
                        }
                    }
                }
            }
        }
    }
}
