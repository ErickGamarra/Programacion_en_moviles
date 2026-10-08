package com.saludplus.citas.ui.citas

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: completar pantalla
@Composable
fun DetalleCitaScreen(citaId: Int, onVolver: () -> Unit, onCitaCancelada: () -> Unit) {
    PantallaEnConstruccion(
        titulo = "Detalle de cita $citaId",
        acciones = listOf("Cancelar cita (temporal)" to onCitaCancelada, "Volver" to onVolver)
    )
}
