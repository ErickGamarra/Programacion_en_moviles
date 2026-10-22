package com.gamarra.huellitasvet.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamarra.huellitasvet.data.local.relation.CitaConDetalle

@Composable
fun CitaCard(
    detalle: CitaConDetalle,
    modifier: Modifier = Modifier,
    acciones: (@Composable ColumnScope.() -> Unit)? = null
) {
    val colores = MaterialTheme.colorScheme
    val cita = detalle.cita
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = colores.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                MascotaAvatar(detalle.mascota.mascota.especie, tamano = 52.dp)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(detalle.servicio.nombre, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Text(detalle.mascota.mascota.nombre, color = colores.onSurfaceVariant, fontSize = 14.sp)
                    Text(
                        "${fechaLarga(cita.fecha)} · ${cita.hora}",
                        color = colores.primary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    EstadoCitaChip(cita.estado)
                    Text(
                        precioTexto(cita.precio),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
            acciones?.invoke(this)
        }
    }
}
