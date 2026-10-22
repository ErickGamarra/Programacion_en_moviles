package com.gamarra.huellitasvet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun HoraChip(hora: String, seleccionado: Boolean, ocupado: Boolean, onClick: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    val forma = RoundedCornerShape(12.dp)
    val fondo = when {
        ocupado -> colores.surfaceVariant
        seleccionado -> colores.primary
        else -> colores.surface
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(74.dp)
            .clip(forma)
            .background(fondo)
            .border(1.dp, if (seleccionado) colores.primary else colores.outlineVariant, forma)
            .clickable(enabled = !ocupado, onClick = onClick)
            .padding(vertical = 10.dp)
    ) {
        Text(
            hora,
            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
            textDecoration = if (ocupado) TextDecoration.LineThrough else null,
            color = when {
                ocupado -> colores.onSurfaceVariant.copy(alpha = 0.5f)
                seleccionado -> colores.onPrimary
                else -> colores.onSurface
            }
        )
    }
}
