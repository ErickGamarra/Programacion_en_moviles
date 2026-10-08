package com.saludplus.citas.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaEnConstruccion(
    titulo: String,
    acciones: List<Pair<String, () -> Unit>> = emptyList(),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Text(text = titulo, style = MaterialTheme.typography.titleLarge)
        Text(text = "Pantalla en construccion", style = MaterialTheme.typography.bodyLarge)
        acciones.forEach { (texto, onClick) ->
            Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
                Text(texto)
            }
        }
    }
}
