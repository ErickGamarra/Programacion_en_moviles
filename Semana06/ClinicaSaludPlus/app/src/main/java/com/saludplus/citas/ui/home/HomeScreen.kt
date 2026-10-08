package com.saludplus.citas.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.saludplus.citas.ui.components.TarjetaAcceso
import com.saludplus.citas.ui.components.TarjetaEspecialidad

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAgendarCita: () -> Unit,
    onEspecialidadClick: (Int) -> Unit,
    onNotificaciones: () -> Unit,
    onNavegarBarra: (String) -> Unit
) {
    val nombre = Repositorio.usuarioActual?.nombre?.substringBefore(" ") ?: "paciente"
    val destacadas = Repositorio.especialidadesDestacadas()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SaludPlus") },
                actions = {
                    IconButton(onClick = onNotificaciones) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = { BarraNavegacion(Rutas.HOME, onNavegarBarra) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Hola, $nombre", style = MaterialTheme.typography.titleLarge)
            Text(
                "¿Qué necesitas hoy?",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaAcceso(
                    icono = Icons.Filled.CalendarMonth,
                    texto = "Agendar cita",
                    onClick = onAgendarCita,
                    modifier = Modifier.weight(1f)
                )
                TarjetaAcceso(
                    icono = Icons.Filled.Event,
                    texto = "Mis citas",
                    onClick = { onNavegarBarra(Rutas.MIS_CITAS) },
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                "Especialidades destacadas",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(destacadas) { especialidad ->
                    TarjetaEspecialidad(
                        especialidad = especialidad,
                        onClick = { onEspecialidadClick(especialidad.id) }
                    )
                }
            }
        }
    }
}
