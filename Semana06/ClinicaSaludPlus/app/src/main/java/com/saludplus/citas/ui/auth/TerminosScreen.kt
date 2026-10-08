package com.saludplus.citas.ui.auth

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: completar pantalla
@Composable
fun TerminosScreen(onVolver: () -> Unit) {
    PantallaEnConstruccion(
        titulo = "Terminos y condiciones",
        acciones = listOf("Volver" to onVolver)
    )
}
