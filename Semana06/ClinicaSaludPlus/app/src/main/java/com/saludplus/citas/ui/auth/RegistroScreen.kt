package com.saludplus.citas.ui.auth

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: completar pantalla
@Composable
fun RegistroScreen(onRegistroExitoso: () -> Unit, onVerTerminos: () -> Unit, onVolver: () -> Unit) {
    PantallaEnConstruccion(
        titulo = "Registro",
        acciones = listOf("Registrar (temporal)" to onRegistroExitoso, "Ver terminos" to onVerTerminos, "Volver" to onVolver)
    )
}
