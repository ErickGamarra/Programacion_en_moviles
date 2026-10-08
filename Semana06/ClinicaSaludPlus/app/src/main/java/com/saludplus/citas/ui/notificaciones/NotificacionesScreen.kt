package com.saludplus.citas.ui.notificaciones

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: completar pantalla
@Composable
fun NotificacionesScreen(onVolver: () -> Unit) {
    PantallaEnConstruccion(
        titulo = "Notificaciones",
        acciones = listOf("Volver" to onVolver)
    )
}
