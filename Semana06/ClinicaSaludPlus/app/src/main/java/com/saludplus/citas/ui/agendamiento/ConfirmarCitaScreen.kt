package com.saludplus.citas.ui.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
fun ConfirmarCitaScreen(medicoId: Int, fecha: String, hora: String, onCitaAgendada: () -> Unit, onVolver: () -> Unit) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(topBar = { BarraSuperior("Confirmar cita", onVolver) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Especialidad: ${especialidad?.nombre ?: "-"}", style = MaterialTheme.typography.bodyLarge)
                    Text("Médico: ${medico?.nombre ?: "-"}", style = MaterialTheme.typography.bodyLarge)
                    Text("Fecha: ${fecha.split("-").reversed().joinToString("/")}", style = MaterialTheme.typography.bodyLarge)
                    Text("Hora: $hora", style = MaterialTheme.typography.bodyLarge)
                }
            }
            if (error != null) {
                Text(error!!, color = MaterialTheme.colorScheme.error)
            }
            Button(
                onClick = {
                    if (Repositorio.agendarCita(medicoId, fecha, hora)) onCitaAgendada()
                    else error = "No se pudo agendar: el horario ya fue reservado"
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar cita")
            }
        }
    }
}
