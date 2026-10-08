package com.saludplus.citas.ui.auth

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: Image del logo, Column y botones "Registrarse" / "Iniciar sesion" segun el diseno.
@Composable
fun SplashScreen(onRegistrarse: () -> Unit, onIniciarSesion: () -> Unit) {
    PantallaEnConstruccion(
        titulo = "Splash",
        acciones = listOf("Registrarse" to onRegistrarse, "Iniciar sesion" to onIniciarSesion)
    )
}
