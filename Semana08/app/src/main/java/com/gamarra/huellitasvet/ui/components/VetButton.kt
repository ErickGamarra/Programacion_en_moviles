package com.gamarra.huellitasvet.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun VetButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
    contorno: Boolean = false
) {
    val forma = RoundedCornerShape(14.dp)
    val base = modifier.fillMaxWidth().height(52.dp)
    if (contorno) {
        OutlinedButton(onClick = onClick, enabled = habilitado, shape = forma, modifier = base) {
            Text(texto, fontWeight = FontWeight.SemiBold)
        }
    } else {
        Button(onClick = onClick, enabled = habilitado, shape = forma, modifier = base) {
            Text(texto, fontWeight = FontWeight.SemiBold)
        }
    }
}
