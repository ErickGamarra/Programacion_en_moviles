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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
    var listaCitas by remember { mutableStateOf(mockInitialAppointments) }

    // Estado básico: guarda la cita que el usuario intenta cancelar
    // Si es null, el diálogo permanece oculto
    var citaParaCancelar by remember { mutableStateOf<Appointment?>(null) }

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
                        onCancelar = {
                            // En lugar de borrar de golpe, guardamos la cita para detonar el diálogo
                            citaParaCancelar = cita
                        }
                    )
                }
            }
        }
    }

    // Mejora IA: AlertDialog de confirmación
    if (citaParaCancelar != null) {
        AlertDialog(
            onDismissRequest = { citaParaCancelar = null },
            title = { Text("Cancelar cita") },
            text = {
                Text("¿Estás seguro de que deseas cancelar tu cita con ${citaParaCancelar?.doctorName}?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        listaCitas = listaCitas.filterNot { it.id == citaParaCancelar?.id }
                        citaParaCancelar = null
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                TextButton(onClick = { citaParaCancelar = null }) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
fun TarjetaCita(
    cita: Appointment,
    onCancelar: () -> Unit
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

            if (cita.status == AppointmentStatus.CONFIRMADA) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onCancelar,
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