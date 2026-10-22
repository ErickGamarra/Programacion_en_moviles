package com.gamarra.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GraficoBarras(datos: List<DatoGrafico>, modifier: Modifier = Modifier) {
    val maximo = datos.maxOfOrNull { it.valor }?.takeIf { it > 0f } ?: 1f
    val pista = MaterialTheme.colorScheme.surfaceVariant
    Column(modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (datos.isEmpty()) {
            Text("Sin datos", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        datos.forEach { d ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    d.etiqueta,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(110.dp)
                )
                Canvas(Modifier.weight(1f).height(18.dp)) {
                    val radio = CornerRadius(size.height / 2)
                    drawRoundRect(pista, cornerRadius = radio)
                    drawRoundRect(d.color, size = Size(size.width * d.valor / maximo, size.height), cornerRadius = radio)
                }
                Text(
                    d.valor.toInt().toString(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp).width(24.dp)
                )
            }
        }
    }
}
