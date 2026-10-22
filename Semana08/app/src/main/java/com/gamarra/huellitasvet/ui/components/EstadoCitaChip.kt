package com.gamarra.huellitasvet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamarra.huellitasvet.model.EstadoCita
import com.gamarra.huellitasvet.ui.theme.EstadoAtendida
import com.gamarra.huellitasvet.ui.theme.EstadoCancelada
import com.gamarra.huellitasvet.ui.theme.EstadoConfirmada
import com.gamarra.huellitasvet.ui.theme.EstadoPendiente

@Composable
fun EstadoCitaChip(estado: String, modifier: Modifier = Modifier) {
    val color = when (estado) {
        EstadoCita.PENDIENTE.name -> EstadoPendiente
        EstadoCita.CONFIRMADA.name -> EstadoConfirmada
        EstadoCita.ATENDIDA.name -> EstadoAtendida
        else -> EstadoCancelada
    }
    Text(
        estado.lowercase().replaceFirstChar { it.uppercase() },
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
