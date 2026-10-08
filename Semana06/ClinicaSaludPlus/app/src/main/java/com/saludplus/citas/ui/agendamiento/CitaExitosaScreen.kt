package com.saludplus.citas.ui.agendamiento

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: mensaje de confirmacion con resumen de la ultima cita agendada.
@Composable
fun CitaExitosaScreen(onVerMisCitas: () -> Unit, onIrAInicio: () -> Unit) {
    PantallaEnConstruccion(
        titulo = "Cita agendada",
        acciones = listOf("Ver mis citas" to onVerMisCitas, "Ir al inicio" to onIrAInicio)
    )
}
