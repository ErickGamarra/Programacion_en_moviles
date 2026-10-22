package com.gamarra.huellitasvet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun FechaChip(fecha: LocalDate, seleccionado: Boolean, onClick: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    val forma = RoundedCornerShape(16.dp)
    val dia = if (fecha == LocalDate.now()) "Hoy"
    else fecha.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("es-PE")).replaceFirstChar { it.uppercase() }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(60.dp)
            .clip(forma)
            .background(if (seleccionado) colores.primary else colores.surface)
            .border(1.dp, if (seleccionado) colores.primary else colores.outlineVariant, forma)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp)
    ) {
        Text(dia, fontSize = 13.sp, color = if (seleccionado) colores.onPrimary else colores.onSurfaceVariant)
        Text(
            fecha.dayOfMonth.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (seleccionado) colores.onPrimary else colores.onSurface
        )
    }
}
