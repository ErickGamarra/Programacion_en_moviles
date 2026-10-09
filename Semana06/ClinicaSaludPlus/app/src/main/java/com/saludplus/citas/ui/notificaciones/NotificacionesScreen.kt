package com.saludplus.citas.ui.notificaciones

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior

@Composable
fun NotificacionesScreen(onVolver: () -> Unit) {
    val mensajes = Repositorio.citasDelUsuario().map { cita ->
        val medico = Repositorio.obtenerMedico(cita.medicoId)?.nombre ?: "tu médico"
        val fecha = cita.fecha.split("-").reversed().joinToString("/")
        "Recuerda tu cita con $medico el $fecha a las ${cita.hora}"
    }

    Scaffold(topBar = { BarraSuperior("Notificaciones", onVolver) }) { padding ->
        if (mensajes.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("No tienes notificaciones", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(mensajes) { mensaje ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(mensaje, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}
