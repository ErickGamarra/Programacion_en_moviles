package com.saludplus.citas.ui.agendamiento

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: completar pantalla
@Composable
fun EspecialidadesScreen(onEspecialidadClick: (Int) -> Unit, onVolver: () -> Unit) {
    PantallaEnConstruccion(
        titulo = "Especialidades",
        acciones = listOf("Medicina General (id 1)" to { onEspecialidadClick(1) }, "Volver" to onVolver)
    )
}
