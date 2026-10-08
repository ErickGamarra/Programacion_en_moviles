package com.saludplus.citas.ui.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CitaExitosaScreen(onVerMisCitas: () -> Unit, onIrAInicio: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Icon(
            Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(96.dp)
        )
        Text("¡Cita agendada!", style = MaterialTheme.typography.headlineSmall)
        Text("Tu cita fue registrada correctamente.", style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onVerMisCitas, modifier = Modifier.fillMaxWidth()) {
            Text("Ver mis citas")
        }
        OutlinedButton(onClick = onIrAInicio, modifier = Modifier.fillMaxWidth()) {
            Text("Ir al inicio")
        }
    }
}
