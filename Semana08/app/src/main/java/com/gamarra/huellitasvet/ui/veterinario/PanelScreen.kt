package com.gamarra.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamarra.huellitasvet.ui.components.graficos.GraficoBarras
import com.gamarra.huellitasvet.ui.components.graficos.GraficoDona
import com.gamarra.huellitasvet.ui.components.precioTexto
import com.gamarra.huellitasvet.ui.theme.EstadoConfirmada
import com.gamarra.huellitasvet.ui.theme.EstadoPendiente
import com.gamarra.huellitasvet.viewmodel.PanelViewModel
import com.gamarra.huellitasvet.viewmodel.SesionViewModel

@Composable
fun PanelScreen(sesion: SesionViewModel, vm: PanelViewModel, onVerPendientes: () -> Unit, modifier: Modifier = Modifier) {
    val usuario by sesion.usuarioActual.collectAsState()
    val citasHoy by vm.citasHoy.collectAsState()
    val pendientes by vm.pendientes.collectAsState()
    val atendidas by vm.atendidas.collectAsState()
    val ingresos by vm.ingresosMes.collectAsState()
    val porEstado by vm.porEstado.collectAsState()
    val porServicio by vm.porServicio.collectAsState()
    val colores = MaterialTheme.colorScheme

    LazyColumn(
        modifier = modifier.fillMaxSize().background(colores.background),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Panel de la clínica", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Hola, ${usuario?.nombre ?: ""}", color = colores.onSurfaceVariant)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Contador("Citas hoy", citasHoy, colores.primary, Modifier.weight(1f))
                Contador("Pendientes", pendientes, EstadoPendiente, Modifier.weight(1f))
                Contador("Atendidas", atendidas, EstadoConfirmada, Modifier.weight(1f))
            }
        }
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colores.primary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("Ingresos del mes", color = colores.onPrimary.copy(alpha = 0.8f))
                    Text(precioTexto(ingresos), color = colores.onPrimary, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    Text("Citas atendidas desde el día 1", color = colores.onPrimary.copy(alpha = 0.8f), fontSize = 13.sp)
                }
            }
        }
        if (pendientes > 0) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = EstadoPendiente.copy(alpha = 0.12f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.NotificationsActive, contentDescription = null, tint = EstadoPendiente)
                        Spacer(Modifier.width(12.dp))
                        Text(
                            if (pendientes == 1) "1 cita por confirmar" else "$pendientes citas por confirmar",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = onVerPendientes) { Text("Ver", color = EstadoPendiente, fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }
        item {
            TarjetaGrafico("Citas por estado") { GraficoDona(porEstado) }
        }
        item {
            TarjetaGrafico("Citas por servicio") { GraficoBarras(porServicio) }
        }
    }
}

@Composable
private fun Contador(titulo: String, valor: Int, color: Color, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
        modifier = modifier
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(valor.toString(), color = color, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text(titulo, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun TarjetaGrafico(titulo: String, contenido: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(titulo, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
            contenido()
        }
    }
}
