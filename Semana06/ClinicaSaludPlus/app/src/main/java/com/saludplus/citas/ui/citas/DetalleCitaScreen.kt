package com.saludplus.citas.ui.citas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior

@Composable
fun DetalleCitaScreen(citaId: Int, onVolver: () -> Unit, onCitaCancelada: () -> Unit) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = cita?.let { Repositorio.obtenerMedico(it.medicoId) }
    val especialidad = cita?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(topBar = { BarraSuperior("Detalle de cita", onVolver) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (cita == null) {
                Text("La cita no existe", style = MaterialTheme.typography.bodyLarge)
            } else {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Especialidad: ${especialidad?.nombre ?: "-"}", style = MaterialTheme.typography.bodyLarge)
                        Text("Médico: ${medico?.nombre ?: "-"}", style = MaterialTheme.typography.bodyLarge)
                        Text("Fecha: ${cita.fecha.split("-").reversed().joinToString("/")}", style = MaterialTheme.typography.bodyLarge)
                        Text("Hora: ${cita.hora}", style = MaterialTheme.typography.bodyLarge)
                    }
                }
                Button(
                    onClick = { mostrarDialogo = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar cita")
                }
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Cancelar cita") },
            text = { Text("¿Seguro que deseas cancelar esta cita?") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogo = false
                    Repositorio.cancelarCita(citaId)
                    onCitaCancelada()
                }) { Text("Sí, cancelar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) { Text("No") }
            }
        )
    }
}
