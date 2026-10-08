package com.saludplus.citas.ui.citas

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO (reto extra): datos de la cita (Repositorio.obtenerCita) y AlertDialog para
// Repositorio.cancelarCita(citaId).
@Composable
fun DetalleCitaScreen(citaId: Int, onVolver: () -> Unit, onCitaCancelada: () -> Unit) {
    PantallaEnConstruccion(
        titulo = "Detalle de cita $citaId",
        acciones = listOf("Cancelar cita (temporal)" to onCitaCancelada, "Volver" to onVolver)
    )
}
