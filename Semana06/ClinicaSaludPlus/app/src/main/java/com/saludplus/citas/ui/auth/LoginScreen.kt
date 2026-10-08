package com.saludplus.citas.ui.auth

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: campos correo y contrasena, busqueda con Repositorio.iniciarSesion(...) y mensaje de error
// si las credenciales no coinciden.
@Composable
fun LoginScreen(onLoginExitoso: () -> Unit, onVolver: () -> Unit) {
    PantallaEnConstruccion(
        titulo = "Iniciar sesion",
        acciones = listOf("Entrar (temporal)" to onLoginExitoso, "Volver" to onVolver)
    )
}
