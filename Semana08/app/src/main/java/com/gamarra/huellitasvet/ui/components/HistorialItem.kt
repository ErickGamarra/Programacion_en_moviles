package com.gamarra.huellitasvet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
fun HistorialItem(atencion: CitaConDetalle, esUltimo: Boolean, modifier: Modifier = Modifier) {
    val colores = MaterialTheme.colorScheme
    val cita = atencion.cita
    Row(modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        Column(Modifier.width(24.dp).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(18.dp))
            Box(Modifier.size(12.dp).background(colores.primary, CircleShape))
            if (!esUltimo) {
                Box(Modifier.width(2.dp).weight(1f).background(colores.primaryContainer))
            }
        }
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colores.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.weight(1f).padding(start = 8.dp, bottom = 12.dp)
        ) {
            Column(Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(atencion.servicio.nombre, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                    Text(fechaLarga(cita.fecha), color = colores.onSurfaceVariant, fontSize = 13.sp)
                }
                cita.diagnostico?.let { Text(it, modifier = Modifier.padding(top = 6.dp)) }
                cita.tratamiento?.let {
                    Text("Tratamiento: $it", color = colores.onSurfaceVariant, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
                }
                cita.pesoRegistrado?.let {
                    Text(
                        "Peso: ${pesoTexto(it)}",
                        color = colores.primary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
        }
    }
}
