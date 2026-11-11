package com.tecsup.pokemon.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.pokemon.AppContainer
import com.tecsup.pokemon.ui.detalle.DetalleScreen
import com.tecsup.pokemon.ui.detalle.DetalleViewModel
import com.tecsup.pokemon.ui.favoritos.FavoritosScreen
import com.tecsup.pokemon.ui.favoritos.FavoritosViewModel
import com.tecsup.pokemon.ui.login.LoginScreen
import com.tecsup.pokemon.ui.login.LoginViewModel
import com.tecsup.pokemon.ui.lista.ListaScreen
import com.tecsup.pokemon.ui.lista.ListaViewModel

// Rutas de la app
object Rutas {
    const val LOGIN = "login"
    const val LISTA = "lista"
    const val DETALLE = "detalle/{id}"
    const val FAVORITOS = "favoritos"
    fun detalle(id: Int) = "detalle/$id"
}

@Composable
fun AppNav(container: AppContainer) {
    val navController = rememberNavController()
    // El LoginViewModel vive en la Activity: guarda la sesión mientras la app está abierta
    val loginVm: LoginViewModel = viewModel(factory = LoginViewModel.factory(container))

    // Cerrar sesión: limpia el login y vuelve a la pantalla de ingreso sin historial
    val cerrarSesion: () -> Unit = {
        loginVm.cerrarSesion()
        navController.navigate(Rutas.LOGIN) {
            popUpTo(0) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = Rutas.LOGIN) {

        composable(Rutas.LOGIN) {
            LoginScreen(
                viewModel = loginVm,
                onIngreso = {
                    navController.navigate(Rutas.LISTA) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.LISTA) {
            val vm: ListaViewModel = viewModel(factory = ListaViewModel.factory(container))
            ListaScreen(
                viewModel = vm,
                onItemClick = { id -> navController.navigate(Rutas.detalle(id)) },
                onAbrirFavoritos = { navController.navigate(Rutas.FAVORITOS) },
                onCerrarSesion = cerrarSesion
            )
        }

        composable(Rutas.FAVORITOS) {
            val vm: FavoritosViewModel = viewModel(factory = FavoritosViewModel.factory(container))
            FavoritosScreen(
                viewModel = vm,
                onItemClick = { id -> navController.navigate(Rutas.detalle(id)) },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            val vm: DetalleViewModel = viewModel(factory = DetalleViewModel.factory(container))
            DetalleScreen(viewModel = vm, onVolver = { navController.popBackStack() })
        }
    }
}
