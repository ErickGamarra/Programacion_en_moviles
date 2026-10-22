package com.gamarra.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GraficoLineas(datos: List<DatoGrafico>, modifier: Modifier = Modifier, alto: Dp = 180.dp) {
    if (datos.size < 2) {
        Text("Se necesitan al menos 2 atenciones con peso", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = modifier)
        return
    }
    val medidor = rememberTextMeasurer()
    val colorTexto = MaterialTheme.colorScheme.onSurfaceVariant
    val colorGuia = MaterialTheme.colorScheme.outlineVariant
    val estiloEtiqueta = TextStyle(fontSize = 11.sp, color = colorTexto)
    val estiloValor = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = datos.first().color)

    Canvas(modifier.fillMaxWidth().height(alto)) {
        val margenX = 28.dp.toPx()
        val arriba = 26.dp.toPx()
        val abajo = size.height - 26.dp.toPx()
        val minimo = datos.minOf { it.valor }
        val maximo = datos.maxOf { it.valor }
        val rango = (maximo - minimo).takeIf { it > 0f } ?: 1f
        val paso = (size.width - margenX * 2) / (datos.size - 1)
        val puntos = datos.mapIndexed { i, d ->
            val y = if (maximo == minimo) (arriba + abajo) / 2 else abajo - (d.valor - minimo) / rango * (abajo - arriba)
            Offset(margenX + paso * i, y)
        }

        drawLine(colorGuia, Offset(margenX, abajo), Offset(size.width - margenX, abajo), strokeWidth = 1.dp.toPx())

        val area = Path().apply {
            moveTo(puntos.first().x, abajo)
            puntos.forEach { lineTo(it.x, it.y) }
            lineTo(puntos.last().x, abajo)
            close()
        }
        drawPath(area, Brush.verticalGradient(listOf(datos.first().color.copy(alpha = 0.25f), datos.first().color.copy(alpha = 0f)), arriba, abajo))

        val linea = Path().apply {
            moveTo(puntos.first().x, puntos.first().y)
            puntos.drop(1).forEach { lineTo(it.x, it.y) }
        }
        drawPath(linea, datos.first().color, style = Stroke(width = 3.dp.toPx()))

        datos.forEachIndexed { i, d ->
            val p = puntos[i]
            drawCircle(d.color, 6.dp.toPx(), p)
            drawCircle(Color.White, 3.dp.toPx(), p)
            val valor = medidor.measure("%.1f".format(d.valor), estiloValor)
            drawText(valor, topLeft = Offset(p.x - valor.size.width / 2f, p.y - valor.size.height - 6.dp.toPx()))
            val etiqueta = medidor.measure(d.etiqueta, estiloEtiqueta)
            drawText(etiqueta, topLeft = Offset(p.x - etiqueta.size.width / 2f, abajo + 6.dp.toPx()))
        }
    }
}
