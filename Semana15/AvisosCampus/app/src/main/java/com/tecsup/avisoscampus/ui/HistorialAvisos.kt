package com.tecsup.avisoscampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.model.Aviso
import com.tecsup.avisoscampus.model.Origen

// Solo dibuja la lista que recibe: no conoce el ViewModel
@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    Tarjeta("Historial") {
        if (avisos.isEmpty()) {
            Text(
                "Sin avisos todavía",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            // Column con forEach: la pantalla ya tiene scroll y son pocas filas
            avisos.take(20).forEach { aviso ->
                FilaAviso(aviso)
            }
        }
    }
}

@Composable
private fun FilaAviso(aviso: Aviso) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        EtiquetaOrigen(aviso.origen)
        Column(modifier = Modifier.weight(1f)) {
            Text(aviso.titulo, fontWeight = FontWeight.Bold)
            Text(
                aviso.texto,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            aviso.hora,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EtiquetaOrigen(origen: Origen) {
    val (fondo, texto) = when (origen) {
        Origen.BOTON -> Color(0xFF1E6FD9) to Color.White
        Origen.RECORDATORIO -> Color(0xFFF2B01E) to Color(0xFF3D2C00)
        Origen.MODO_AVION -> Color(0xFF7E57C2) to Color.White
    }
    Text(
        origen.texto,
        color = texto,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .background(fondo, RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    )
}
