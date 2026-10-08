package com.saludplus.citas.ui.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
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

private val diasFijos = listOf(
    "Vie 9" to "2026-10-09",
    "Lun 12" to "2026-10-12",
    "Mar 13" to "2026-10-13",
    "Mié 14" to "2026-10-14",
    "Jue 15" to "2026-10-15"
)

@Composable
fun FechaHoraScreen(medicoId: Int, onContinuar: (fecha: String, hora: String) -> Unit, onVolver: () -> Unit) {
    val medico = Repositorio.obtenerMedico(medicoId)
    var fecha by remember { mutableStateOf(diasFijos.first().second) }
    var hora by remember { mutableStateOf<String?>(null) }
    val horarios = Repositorio.horariosDisponibles(medicoId, fecha)

    Scaffold(topBar = { BarraSuperior("Fecha y hora", onVolver) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(medico?.nombre ?: "", style = MaterialTheme.typography.titleLarge)
            Text("Octubre 2026", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 16.dp))

            LazyRow(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(diasFijos) { (etiqueta, valor) ->
                    FilterChip(
                        selected = valor == fecha,
                        onClick = {
                            fecha = valor
                            hora = null
                        },
                        label = { Text(etiqueta) }
                    )
                }
            }

            Text("Horarios disponibles", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
            if (horarios.isEmpty()) {
                Text("No hay horarios disponibles para este día")
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(horarios) { h ->
                    FilterChip(
                        selected = h == hora,
                        onClick = { hora = h },
                        label = { Text(h) }
                    )
                }
            }

            Button(
                onClick = { hora?.let { onContinuar(fecha, it) } },
                enabled = hora != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuar")
            }
        }
    }
}
