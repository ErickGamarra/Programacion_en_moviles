package com.gamarra.clientesaludplus.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.gamarra.clientesaludplus.data.mockDoctors
import com.gamarra.clientesaludplus.data.mockSpecialties
import com.gamarra.clientesaludplus.model.Doctor

@Composable
fun PantallaInicio(
    onDoctorClick: (String) -> Unit
) {
    // Estado para registrar la especialidad seleccionada (no hay o bueno, sin ViewModel)
    var especialidadSeleccionada by remember { mutableStateOf<String?>(null) }

    // Filtro básico de la lista según el chip activo
    val doctoresMostrados = if (especialidadSeleccionada == null) {
        mockDoctors
    } else {
        mockDoctors.filter { it.specialty.contains(especialidadSeleccionada!!, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Hola, Juan",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "¿Qué especialista necesitas hoy?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // LazyRow para filtros de especialidad
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(mockSpecialties) { especialidad ->
                val isSelected = especialidadSeleccionada == especialidad.name
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        especialidadSeleccionada = if (isSelected) null else especialidad.name
                    },
                    label = { Text(especialidad.name) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Médicos disponibles",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // LazyColumn para lista de médicos
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(doctoresMostrados) { doctor ->
                DoctorCard(
                    doctor = doctor,
                    onClick = { onDoctorClick(doctor.id) }
                )
            }
        }
    }
}

@Composable
fun DoctorCard(
    doctor: Doctor,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono representativo del médico
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = doctor.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = doctor.specialty,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Calificación con estrella
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = doctor.rating.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}