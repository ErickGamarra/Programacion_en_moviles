package com.saludplus.citas.ui.agendamiento

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: resumen de medico, fecha y hora; al confirmar usar Repositorio.agendarCita(...) y llamar a
// onCitaAgendada() (AppNavigation hace popUpTo(HOME)).
@Composable
fun ConfirmarCitaScreen(medicoId: Int, fecha: String, hora: String, onCitaAgendada: () -> Unit, onVolver: () -> Unit) {
    PantallaEnConstruccion(
        titulo = "Confirmar cita ($medicoId, $fecha, $hora)",
        acciones = listOf("Agendar cita (temporal)" to onCitaAgendada, "Volver" to onVolver)
    )
}
