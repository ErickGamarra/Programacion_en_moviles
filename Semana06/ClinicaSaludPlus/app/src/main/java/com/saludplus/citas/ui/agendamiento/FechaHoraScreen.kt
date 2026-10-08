package com.saludplus.citas.ui.agendamiento

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: seleccion de dia y LazyVerticalGrid con Repositorio.horariosDisponibles(medicoId, fecha).
// "Continuar" solo se habilita con dia y hora elegidos.
@Composable
fun FechaHoraScreen(medicoId: Int, onContinuar: (fecha: String, hora: String) -> Unit, onVolver: () -> Unit) {
    PantallaEnConstruccion(
        titulo = "Fecha y hora (medico $medicoId)",
        acciones = listOf("Continuar (temporal)" to { onContinuar("2026-10-16", "09:00") }, "Volver" to onVolver)
    )
}
