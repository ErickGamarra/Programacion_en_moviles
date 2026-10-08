package com.saludplus.citas.ui.citas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.repository.Repositorio
import com.saludplus.citas.ui.components.BarraNavegacion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCitasScreen(onCitaClick: (Int) -> Unit, onNavegarBarra: (String) -> Unit) {
    val citas = Repositorio.citasDelUsuario()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis citas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = { BarraNavegacion(Rutas.MIS_CITAS, onNavegarBarra) }
    ) { padding ->
        if (citas.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Aún no tienes citas agendadas",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(citas, key = { it.id }) { cita ->
                    val medico = Repositorio.obtenerMedico(cita.medicoId)
                    val especialidad = Repositorio.obtenerEspecialidad(cita.especialidadId)
                    Card(onClick = { onCitaClick(cita.id) }, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(medico?.nombre ?: "-", style = MaterialTheme.typography.titleMedium)
                            Text(
                                especialidad?.nombre ?: "-",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "${cita.fecha.split("-").reversed().joinToString("/")} - ${cita.hora}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
