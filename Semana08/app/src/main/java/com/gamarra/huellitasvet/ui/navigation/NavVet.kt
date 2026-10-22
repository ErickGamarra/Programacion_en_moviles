package com.gamarra.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
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
import com.gamarra.huellitasvet.ui.dueno.MisCitasScreen
import com.gamarra.huellitasvet.ui.dueno.PedirCitaScreen
import com.gamarra.huellitasvet.ui.dueno.PerfilScreen
import com.gamarra.huellitasvet.ui.dueno.ReprogramarCitaScreen
import com.gamarra.huellitasvet.ui.dueno.SplashScreen
import com.gamarra.huellitasvet.ui.veterinario.AgendaScreen
import com.gamarra.huellitasvet.ui.veterinario.AjustesScreen
import com.gamarra.huellitasvet.ui.veterinario.AtenderCitaScreen
import com.gamarra.huellitasvet.ui.veterinario.PacientesScreen
import com.gamarra.huellitasvet.ui.veterinario.ServiciosScreen
import com.gamarra.huellitasvet.ui.veterinario.FormularioServicioScreen
import com.gamarra.huellitasvet.ui.veterinario.PanelScreen
import com.gamarra.huellitasvet.viewmodel.AgendaViewModel
import com.gamarra.huellitasvet.viewmodel.AtenderCitaViewModel
import com.gamarra.huellitasvet.viewmodel.InicioViewModel
import com.gamarra.huellitasvet.viewmodel.MascotaViewModel
import com.gamarra.huellitasvet.viewmodel.MisCitasViewModel
import com.gamarra.huellitasvet.viewmodel.PacientesViewModel
import com.gamarra.huellitasvet.viewmodel.PanelViewModel
import com.gamarra.huellitasvet.viewmodel.PedirCitaViewModel
import com.gamarra.huellitasvet.viewmodel.ReprogramarCitaViewModel
import com.gamarra.huellitasvet.viewmodel.ServiciosViewModel
import com.gamarra.huellitasvet.viewmodel.SesionViewModel
import com.gamarra.huellitasvet.viewmodel.VacunasViewModel
import com.gamarra.huellitasvet.viewmodel.VetViewModelFactory

private data class ItemBarra(val ruta: String, val texto: String, val icono: ImageVector)

private val itemsDueno = listOf(
    ItemBarra(Rutas.INICIO, "Inicio", Icons.Filled.Home),
    ItemBarra(Rutas.MASCOTAS, "Mascotas", Icons.Filled.Pets),
    ItemBarra(Rutas.CITAS, "Citas", Icons.Filled.CalendarMonth),
    ItemBarra(Rutas.PERFIL, "Perfil", Icons.Filled.Person)
)

private val itemsVet = listOf(
    ItemBarra(Rutas.PANEL, "Panel", Icons.Filled.Dashboard),
    ItemBarra(Rutas.AGENDA, "Agenda", Icons.Filled.EventNote),
    ItemBarra(Rutas.PACIENTES, "Pacientes", Icons.Filled.Pets),
    ItemBarra(Rutas.SERVICIOS, "Servicios", Icons.Filled.MedicalServices),
    ItemBarra(Rutas.AJUSTES, "Ajustes", Icons.Filled.Settings)
)

@Composable
fun NavVet(sesion: SesionViewModel, factory: VetViewModelFactory) {
    val nav = rememberNavController()
    val usuario by sesion.usuarioActual.collectAsState()
    val inicioVm: InicioViewModel = viewModel(factory = factory)
    val mascotaVm: MascotaViewModel = viewModel(factory = factory)
    val pedirCitaVm: PedirCitaViewModel = viewModel(factory = factory)
    val misCitasVm: MisCitasViewModel = viewModel(factory = factory)
    val panelVm: PanelViewModel = viewModel(factory = factory)
    val agendaVm: AgendaViewModel = viewModel(factory = factory)
    val atenderVm: AtenderCitaViewModel = viewModel(factory = factory)
    val pacientesVm: PacientesViewModel = viewModel(factory = factory)
    val serviciosVm: ServiciosViewModel = viewModel(factory = factory)
    val reprogramarVm: ReprogramarCitaViewModel = viewModel(factory = factory)
    val vacunasVm: VacunasViewModel = viewModel(factory = factory)

    LaunchedEffect(usuario) {
        usuario?.let {
            inicioVm.cargar(it.id)
            misCitasVm.cargar(it.id)
        }
    }

    fun irAPrincipal(rol: Rol?, desde: String) {
        val destino = when (rol) {
            Rol.CLIENTE -> Rutas.INICIO
            Rol.ADMIN -> Rutas.PANEL
            null -> Rutas.login()
        }
        nav.navigate(destino) { popUpTo(desde) { inclusive = true } }
    }

    val verMascota: (Int) -> Unit = { nav.navigate(Rutas.ficha(it)) }
    val agregarMascota: () -> Unit = { nav.navigate(Rutas.formulario()) }
    val pedirCita: () -> Unit = { nav.navigate(Rutas.pedirCita()) }
    val irALogin: (Rol) -> Unit = { rol -> nav.navigate(Rutas.login(rol)) { popUpTo(nav.graph.id) { inclusive = true } } }

    NavHost(navController = nav, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) { SplashScreen(sesion) { irAPrincipal(it, Rutas.SPLASH) } }
        composable(Rutas.LOGIN, arguments = listOf(navArgument("rol") { defaultValue = Rol.CLIENTE.name })) { entrada ->
            val rol = Rol.valueOf(entrada.arguments?.getString("rol") ?: Rol.CLIENTE.name)
            LoginScreen(sesion, rolInicial = rol) { irAPrincipal(it, Rutas.LOGIN) }
        }
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
        composable(Rutas.CITAS) {
            Scaffold(bottomBar = { BarraInferior(nav, itemsDueno) }) { padding ->
                MisCitasScreen(misCitasVm, pedirCita, onReprogramar = { nav.navigate(Rutas.reprogramar(it)) }, modifier = Modifier.padding(padding))
            }
        }
        composable(Rutas.PERFIL) {
            Scaffold(bottomBar = { BarraInferior(nav, itemsDueno) }) { padding ->
                val mascotas by inicioVm.mascotas.collectAsState()
                val proximas by misCitasVm.proximas.collectAsState()
                val atenciones by misCitasVm.atenciones.collectAsState()
                PerfilScreen(
                    sesion,
                    mascotas = mascotas.size,
                    proximas = proximas.size,
                    atenciones = atenciones,
                    onCambiarPerfil = { irALogin(Rol.ADMIN) },
                    onCerrarSesion = { irALogin(Rol.CLIENTE) },
                    modifier = Modifier.padding(padding)
                )
            }
        }
        composable(Rutas.FICHA, arguments = listOf(navArgument("id") { type = NavType.IntType })) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: 0
            FichaMascotaScreen(
                vm = mascotaVm,
                vacunasVm = vacunasVm,
                mascotaId = id,
                onVolver = { nav.popBackStack() },
                onEditar = { nav.navigate(Rutas.formulario(id)) },
                onPedirCita = { nav.navigate(Rutas.pedirCita(id)) }
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
        composable(
            Rutas.PEDIR_CITA,
            arguments = listOf(navArgument("mascotaId") { type = NavType.IntType; defaultValue = -1 })
        ) { entrada ->
            val mascotaId = entrada.arguments?.getInt("mascotaId")?.takeIf { it > 0 }
            LaunchedEffect(Unit) { pedirCitaVm.iniciar(usuario?.id ?: 0, mascotaId) }
            PedirCitaScreen(
                pedirCitaVm,
                onVolver = { nav.popBackStack() },
                onCreada = {
                    nav.navigate(Rutas.CITAS) {
                        popUpTo(Rutas.INICIO)
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Rutas.REPROGRAMAR, arguments = listOf(navArgument("citaId") { type = NavType.IntType })) { entrada ->
            val citaId = entrada.arguments?.getInt("citaId") ?: 0
            ReprogramarCitaScreen(reprogramarVm, citaId, onVolver = { nav.popBackStack() })
        }
        composable(Rutas.PANEL) {
            Scaffold(bottomBar = { BarraInferior(nav, itemsVet) }) { padding ->
                PanelScreen(
                    sesion,
                    panelVm,
                    onVerPendientes = {
                        nav.navigate(Rutas.AGENDA) {
                            popUpTo(Rutas.PANEL) { saveState = true }
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier.padding(padding)
                )
            }
        }
        composable(Rutas.AGENDA) {
            Scaffold(bottomBar = { BarraInferior(nav, itemsVet) }) { padding ->
                AgendaScreen(agendaVm, onAtender = { nav.navigate(Rutas.atender(it)) }, modifier = Modifier.padding(padding))
            }
        }
        composable(Rutas.ATENDER, arguments = listOf(navArgument("citaId") { type = NavType.IntType })) { entrada ->
            val citaId = entrada.arguments?.getInt("citaId") ?: 0
            AtenderCitaScreen(atenderVm, citaId, onVolver = { nav.popBackStack() })
        }
        composable(Rutas.PACIENTES) {
            Scaffold(bottomBar = { BarraInferior(nav, itemsVet) }) { padding ->
                PacientesScreen(pacientesVm, onVerPaciente = { nav.navigate(Rutas.paciente(it)) }, modifier = Modifier.padding(padding))
            }
        }
        composable(Rutas.PACIENTE, arguments = listOf(navArgument("id") { type = NavType.IntType })) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: 0
            FichaMascotaScreen(vm = mascotaVm, vacunasVm = vacunasVm, mascotaId = id, onVolver = { nav.popBackStack() })
        }
        composable(Rutas.SERVICIOS) {
            Scaffold(bottomBar = { BarraInferior(nav, itemsVet) }) { padding ->
                ServiciosScreen(
                    serviciosVm,
                    onEditar = { nav.navigate(Rutas.servicio(it)) },
                    onNuevo = { nav.navigate(Rutas.servicio()) },
                    modifier = Modifier.padding(padding)
                )
            }
        }
        composable(
            Rutas.SERVICIO,
            arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = -1 })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id")?.takeIf { it > 0 }
            FormularioServicioScreen(serviciosVm, id, onVolver = { nav.popBackStack() })
        }
        composable(Rutas.AJUSTES) {
            Scaffold(bottomBar = { BarraInferior(nav, itemsVet) }) { padding ->
                AjustesScreen(
                    sesion,
                    onCambiarPerfil = { irALogin(Rol.CLIENTE) },
                    onCerrarSesion = { irALogin(Rol.ADMIN) },
                    modifier = Modifier.padding(padding)
                )
            }
        }
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
