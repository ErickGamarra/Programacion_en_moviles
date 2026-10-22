package com.gamarra.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gamarra.huellitasvet.model.Rol
import com.gamarra.huellitasvet.ui.dueno.FichaMascotaScreen
import com.gamarra.huellitasvet.ui.dueno.FormularioMascotaScreen
import com.gamarra.huellitasvet.ui.dueno.InicioScreen
import com.gamarra.huellitasvet.ui.dueno.LoginScreen
import com.gamarra.huellitasvet.ui.dueno.MascotasScreen
import com.gamarra.huellitasvet.ui.dueno.SplashScreen
import com.gamarra.huellitasvet.ui.veterinario.PanelScreen
import com.gamarra.huellitasvet.viewmodel.InicioViewModel
import com.gamarra.huellitasvet.viewmodel.MascotaViewModel
import com.gamarra.huellitasvet.viewmodel.SesionViewModel
import com.gamarra.huellitasvet.viewmodel.VetViewModelFactory

private data class ItemBarra(val ruta: String, val texto: String, val icono: ImageVector)

private val itemsDueno = listOf(
    ItemBarra(Rutas.INICIO, "Inicio", Icons.Filled.Home),
    ItemBarra(Rutas.MASCOTAS, "Mascotas", Icons.Filled.Pets)
)

@Composable
fun NavVet(sesion: SesionViewModel, factory: VetViewModelFactory) {
    val nav = rememberNavController()
    val usuario by sesion.usuarioActual.collectAsState()
    val inicioVm: InicioViewModel = viewModel(factory = factory)
    val mascotaVm: MascotaViewModel = viewModel(factory = factory)

    LaunchedEffect(usuario) { usuario?.let { inicioVm.cargar(it.id) } }

    fun irAPrincipal(rol: Rol?, desde: String) {
        val destino = when (rol) {
            Rol.CLIENTE -> Rutas.INICIO
            Rol.ADMIN -> Rutas.PANEL
            null -> Rutas.LOGIN
        }
        nav.navigate(destino) { popUpTo(desde) { inclusive = true } }
    }

    val verMascota: (Int) -> Unit = { nav.navigate(Rutas.ficha(it)) }
    val agregarMascota: () -> Unit = { nav.navigate(Rutas.formulario()) }
    // TODO Paso 9: navegar a pedir cita
    val pedirCita: () -> Unit = {}

    NavHost(navController = nav, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) { SplashScreen(sesion) { irAPrincipal(it, Rutas.SPLASH) } }
        composable(Rutas.LOGIN) { LoginScreen(sesion) { irAPrincipal(it, Rutas.LOGIN) } }
        composable(Rutas.INICIO) {
            Scaffold(bottomBar = { BarraInferior(nav, itemsDueno) }) { padding ->
                InicioScreen(sesion, inicioVm, verMascota, agregarMascota, pedirCita, Modifier.padding(padding))
            }
        }
        composable(Rutas.MASCOTAS) {
            Scaffold(bottomBar = { BarraInferior(nav, itemsDueno) }) { padding ->
                MascotasScreen(inicioVm, verMascota, agregarMascota, Modifier.padding(padding))
            }
        }
        composable(Rutas.FICHA, arguments = listOf(navArgument("id") { type = NavType.IntType })) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: 0
            FichaMascotaScreen(
                vm = mascotaVm,
                mascotaId = id,
                onVolver = { nav.popBackStack() },
                onEditar = { nav.navigate(Rutas.formulario(id)) },
                onPedirCita = pedirCita
            )
        }
        composable(
            Rutas.FORMULARIO,
            arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = -1 })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id")?.takeIf { it > 0 }
            FormularioMascotaScreen(
                vm = mascotaVm,
                duenoId = usuario?.id ?: 0,
                mascotaId = id,
                onVolver = { nav.popBackStack() },
                onEliminada = { nav.popBackStack(Rutas.MASCOTAS, inclusive = false) || nav.popBackStack(Rutas.INICIO, inclusive = false) }
            )
        }
        composable(Rutas.PANEL) { PanelScreen(sesion) }
    }
}

@Composable
private fun BarraInferior(nav: NavHostController, items: List<ItemBarra>) {
    val actual = nav.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = actual == item.ruta,
                onClick = {
                    nav.navigate(item.ruta) {
                        popUpTo(items.first().ruta) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icono, contentDescription = null) },
                label = { Text(item.texto) }
            )
        }
    }
}
