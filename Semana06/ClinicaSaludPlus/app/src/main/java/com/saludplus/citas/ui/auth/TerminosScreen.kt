package com.saludplus.citas.ui.auth

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO (reto extra): texto largo con scroll (verticalScroll) o AlertDialog.
@Composable
fun TerminosScreen(onVolver: () -> Unit) {
    PantallaEnConstruccion(
        titulo = "Terminos y condiciones",
        acciones = listOf("Volver" to onVolver)
    )
}
