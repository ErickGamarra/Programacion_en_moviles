package com.saludplus.citas.ui.auth

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: estados con remember, OutlinedTextField (nombre, correo, telefono, contrasena), validaciones
// y Repositorio.registrarUsuario(...). Al registrar con exito llamar a onRegistroExitoso().
@Composable
fun RegistroScreen(onRegistroExitoso: () -> Unit, onVerTerminos: () -> Unit, onVolver: () -> Unit) {
    PantallaEnConstruccion(
        titulo = "Registro",
        acciones = listOf("Registrar (temporal)" to onRegistroExitoso, "Ver terminos" to onVerTerminos, "Volver" to onVolver)
    )
}
