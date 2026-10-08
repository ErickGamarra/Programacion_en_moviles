package com.saludplus.citas.ui.notificaciones

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO (reto extra): recordatorios generados con map sobre Repositorio.citasDelUsuario().
@Composable
fun NotificacionesScreen(onVolver: () -> Unit) {
    PantallaEnConstruccion(
        titulo = "Notificaciones",
        acciones = listOf("Volver" to onVolver)
    )
}
