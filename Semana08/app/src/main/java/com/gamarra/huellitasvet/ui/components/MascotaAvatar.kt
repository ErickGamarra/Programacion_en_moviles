package com.gamarra.huellitasvet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gamarra.huellitasvet.model.Especie

private val Ojo = Color(0xFF1F2937)
private val Rosado = Color(0xFFF9A8B8)

@Composable
fun MascotaAvatar(especie: String, modifier: Modifier = Modifier, tamano: Dp = 56.dp) {
    val tipo = runCatching { Especie.valueOf(especie) }.getOrDefault(Especie.OTRO)
    Canvas(modifier.size(tamano)) {
        val fondo = when (tipo) {
            Especie.PERRO -> Color(0xFFFDE7C8)
            Especie.GATO -> Color(0xFFFFE4D1)
            Especie.OTRO -> Color(0xFFE5E7EB)
        }
        drawCircle(fondo)
        when (tipo) {
            Especie.PERRO -> perro()
            Especie.GATO -> gato()
            Especie.OTRO -> conejo()
        }
    }
}

private fun DrawScope.ojos(y: Float, separacion: Float, radio: Float) {
    val c = size.width / 2
    listOf(c - separacion, c + separacion).forEach { x ->
        drawCircle(Ojo, radio, Offset(x, y))
        drawCircle(Color.White, radio * 0.35f, Offset(x + radio * 0.3f, y - radio * 0.3f))
    }
}

private fun DrawScope.perro() {
    val s = size.width
    val c = Offset(s / 2, s * 0.55f)
    val oreja = Color(0xFF8B5A2B)
    drawOval(oreja, Offset(s * 0.14f, s * 0.26f), Size(s * 0.2f, s * 0.38f))
    drawOval(oreja, Offset(s * 0.66f, s * 0.26f), Size(s * 0.2f, s * 0.38f))
    drawCircle(Color(0xFFC8915A), s * 0.28f, c)
    drawOval(Color(0xFFF1D3B0), Offset(s * 0.36f, s * 0.58f), Size(s * 0.28f, s * 0.2f))
    ojos(s * 0.5f, s * 0.1f, s * 0.04f)
    drawOval(Ojo, Offset(s * 0.45f, s * 0.6f), Size(s * 0.1f, s * 0.07f))
    drawOval(Rosado, Offset(s * 0.47f, s * 0.7f), Size(s * 0.06f, s * 0.07f))
}

private fun DrawScope.gato() {
    val s = size.width
    val cara = Color(0xFFF4A261)
    listOf(1f, -1f).forEach { lado ->
        val x = s / 2 - lado * s * 0.2f
        drawPath(Path().apply {
            moveTo(x - lado * s * 0.1f, s * 0.42f)
            lineTo(x - lado * s * 0.06f, s * 0.16f)
            lineTo(x + lado * s * 0.12f, s * 0.34f)
            close()
        }, cara)
        drawPath(Path().apply {
            moveTo(x - lado * s * 0.06f, s * 0.38f)
            lineTo(x - lado * s * 0.045f, s * 0.24f)
            lineTo(x + lado * s * 0.05f, s * 0.34f)
            close()
        }, Rosado)
    }
    drawCircle(cara, s * 0.28f, Offset(s / 2, s * 0.56f))
    ojos(s * 0.52f, s * 0.11f, s * 0.045f)
    drawPath(Path().apply {
        moveTo(s * 0.46f, s * 0.62f)
        lineTo(s * 0.54f, s * 0.62f)
        lineTo(s * 0.5f, s * 0.67f)
        close()
    }, Rosado)
    listOf(-1f, 1f).forEach { lado ->
        listOf(0.64f, 0.7f).forEach { y ->
            drawLine(
                Ojo.copy(alpha = 0.5f),
                Offset(s / 2 + lado * s * 0.1f, s * 0.66f),
                Offset(s / 2 + lado * s * 0.3f, s * y),
                strokeWidth = s * 0.012f
            )
        }
    }
}

private fun DrawScope.conejo() {
    val s = size.width
    val cara = Color(0xFFD1D5DB)
    listOf(0.32f, 0.54f).forEach { x ->
        drawOval(cara, Offset(s * x, s * 0.06f), Size(s * 0.14f, s * 0.4f))
        drawOval(Rosado, Offset(s * (x + 0.035f), s * 0.11f), Size(s * 0.07f, s * 0.28f))
    }
    drawCircle(cara, s * 0.27f, Offset(s / 2, s * 0.6f))
    ojos(s * 0.56f, s * 0.1f, s * 0.04f)
    drawOval(Rosado, Offset(s * 0.46f, s * 0.64f), Size(s * 0.08f, s * 0.05f))
    drawRect(Color.White, Offset(s * 0.47f, s * 0.7f), Size(s * 0.06f, s * 0.06f))
}
