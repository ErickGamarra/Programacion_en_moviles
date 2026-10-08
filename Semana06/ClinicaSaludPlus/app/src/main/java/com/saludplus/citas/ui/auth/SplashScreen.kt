package com.saludplus.citas.ui.auth

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: completar pantalla
@Composable
fun SplashScreen(onRegistrarse: () -> Unit, onIniciarSesion: () -> Unit) {
    PantallaEnConstruccion(
        titulo = "Splash",
        acciones = listOf("Registrarse" to onRegistrarse, "Iniciar sesion" to onIniciarSesion)
    )
}
