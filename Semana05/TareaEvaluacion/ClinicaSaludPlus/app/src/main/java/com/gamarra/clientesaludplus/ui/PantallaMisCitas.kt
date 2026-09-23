package com.gamarra.clientesaludplus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gamarra.clientesaludplus.data.mockInitialAppointments
import com.gamarra.clientesaludplus.model.Appointment
import com.gamarra.clientesaludplus.model.AppointmentStatus

@Composable
fun PantallaMisCitas() {
    // Estado local con la lista inicial de citas
    var listaCitas by remember { mutableStateOf(mockInitialAppointments) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis citas programadas",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (listaCitas.isEmpty()) {
            Text(
                text = "No tienes citas registradas.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listaCitas) { cita ->
                    TarjetaCita(
                        cita = cita,
                        onCancelar = { citaId ->
                            // Cancelación directa simple (sin confirmación, ideal para optimizar en mejora-ia)
                            listaCitas = listaCitas.filterNot { it.id == citaId }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaCita(
    cita: Appointment,
    onCancelar: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cita.doctorName,
                    style = MaterialTheme.typography.titleMedium
                )
                // Indicador simple de estado
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (cita.status == AppointmentStatus.CONFIRMADA)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = cita.status.name,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = if (cita.status == AppointmentStatus.CONFIRMADA)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = cita.specialty,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${cita.date} • ${cita.time}",
                style = MaterialTheme.typography.bodyMedium
            )

            // Opción de cancelar solo si la cita está confirmada
            if (cita.status == AppointmentStatus.CONFIRMADA) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { onCancelar(cita.id) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Cancelar cita")
                }
            }
        }
    }
}