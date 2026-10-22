package com.gamarra.huellitasvet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gamarra.huellitasvet.model.Rol
import com.gamarra.huellitasvet.ui.dueno.InicioScreen
import com.gamarra.huellitasvet.ui.dueno.LoginScreen
import com.gamarra.huellitasvet.ui.dueno.SplashScreen
import com.gamarra.huellitasvet.ui.veterinario.PanelScreen
import com.gamarra.huellitasvet.viewmodel.SesionViewModel

@Composable
fun NavVet(sesion: SesionViewModel) {
    val nav = rememberNavController()

    fun irAPrincipal(rol: Rol?, desde: String) {
        val destino = when (rol) {
            Rol.CLIENTE -> Rutas.INICIO
            Rol.ADMIN -> Rutas.PANEL
            null -> Rutas.LOGIN
        }
        nav.navigate(destino) { popUpTo(desde) { inclusive = true } }
    }

    NavHost(navController = nav, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) { SplashScreen(sesion) { irAPrincipal(it, Rutas.SPLASH) } }
        composable(Rutas.LOGIN) { LoginScreen(sesion) { irAPrincipal(it, Rutas.LOGIN) } }
        composable(Rutas.INICIO) { InicioScreen(sesion) }
        composable(Rutas.PANEL) { PanelScreen(sesion) }
    }
}
