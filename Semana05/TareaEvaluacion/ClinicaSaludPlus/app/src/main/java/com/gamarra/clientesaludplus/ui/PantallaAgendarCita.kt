package com.gamarra.clientesaludplus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgendarCita(
    doctorId: String,
    onBackClick: () -> Unit,
    onConfirmarClick: (doctorId: String, fecha: String, hora: String) -> Unit
) {
    // Listas fijas básicas para fecha y hora
    val fechasDisponibles = listOf("Jue 26", "Vie 27", "Sáb 28")
    val horasDisponibles = listOf("9:00 am", "10:30 am", "3:00 pm")

    // Estados simples con remember: emulan un RadioButton garantizando una sola selección activa
    var fechaSeleccionada by remember { mutableStateOf("Vie 27") }
    var horaSeleccionada by remember { mutableStateOf("10:30 am") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(
                text = "Selecciona fecha",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Selector de fecha única
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                fechasDisponibles.forEach { fecha ->
                    FilterChip(
                        selected = fechaSeleccionada == fecha,
                        onClick = { fechaSeleccionada = fecha },
                        label = { Text(fecha) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Selecciona hora",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Selector de hora única
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                horasDisponibles.forEach { hora ->
                    FilterChip(
                        selected = horaSeleccionada == hora,
                        onClick = { horaSeleccionada = hora },
                        label = { Text(hora) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    onConfirmarClick(doctorId, fechaSeleccionada, horaSeleccionada)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Confirmar cita")
            }
        }
    }
}