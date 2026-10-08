package com.saludplus.citas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saludplus.citas.ui.agendamiento.CitaExitosaScreen
import com.saludplus.citas.ui.agendamiento.ConfirmarCitaScreen
import com.saludplus.citas.ui.agendamiento.EspecialidadesScreen
import com.saludplus.citas.ui.agendamiento.FechaHoraScreen
import com.saludplus.citas.ui.agendamiento.MedicosScreen
import com.saludplus.citas.ui.auth.LoginScreen
import com.saludplus.citas.ui.auth.RegistroScreen
import com.saludplus.citas.ui.auth.SplashScreen
import com.saludplus.citas.ui.auth.TerminosScreen
import com.saludplus.citas.ui.citas.DetalleCitaScreen
import com.saludplus.citas.ui.citas.MisCitasScreen
import com.saludplus.citas.ui.home.HomeScreen
import com.saludplus.citas.ui.notificaciones.NotificacionesScreen
import com.saludplus.citas.ui.perfil.PerfilScreen
import com.saludplus.citas.ui.resultados.ResultadosScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {

    // Navegacion de la barra inferior: no apila copias y siempre regresa a Inicio.
    val navegarBarra: (String) -> Unit = { ruta ->
        navController.navigate(ruta) {
            popUpTo(Rutas.HOME)
            launchSingleTop = true
        }
    }

    NavHost(navController = navController, startDestination = Rutas.SPLASH) {

        composable(Rutas.SPLASH) {
            SplashScreen(
                onRegistrarse = { navController.navigate(Rutas.REGISTRO) },
                onIniciarSesion = { navController.navigate(Rutas.LOGIN) }
            )
        }
        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onRegistroExitoso = {
                    navController.navigate(Rutas.HOME) { popUpTo(Rutas.SPLASH) { inclusive = true } }
                },
                onVerTerminos = { navController.navigate(Rutas.TERMINOS) },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Rutas.LOGIN) {
            LoginScreen(
                onLoginExitoso = {
                    navController.navigate(Rutas.HOME) { popUpTo(Rutas.SPLASH) { inclusive = true } }
                },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Rutas.TERMINOS) {
            TerminosScreen(onVolver = { navController.popBackStack() })
        }

        composable(Rutas.HOME) {
            HomeScreen(
                onAgendarCita = { navController.navigate(Rutas.ESPECIALIDADES) },
                onEspecialidadClick = { id -> navController.navigate(Rutas.medicos(id)) },
                onNotificaciones = { navController.navigate(Rutas.NOTIFICACIONES) },
                onNavegarBarra = navegarBarra
            )
        }

        composable(Rutas.ESPECIALIDADES) {
            EspecialidadesScreen(
                onEspecialidadClick = { id -> navController.navigate(Rutas.medicos(id)) },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(
            route = Rutas.MEDICOS,
            arguments = listOf(navArgument(Rutas.ARG_ESPECIALIDAD_ID) { type = NavType.IntType })
        ) { entry ->
            val especialidadId = entry.arguments?.getInt(Rutas.ARG_ESPECIALIDAD_ID) ?: 0
            MedicosScreen(
                especialidadId = especialidadId,
                onMedicoClick = { medicoId -> navController.navigate(Rutas.fechaHora(medicoId)) },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(
            route = Rutas.FECHA_HORA,
            arguments = listOf(navArgument(Rutas.ARG_MEDICO_ID) { type = NavType.IntType })
        ) { entry ->
            val medicoId = entry.arguments?.getInt(Rutas.ARG_MEDICO_ID) ?: 0
            FechaHoraScreen(
                medicoId = medicoId,
                onContinuar = { fecha, hora ->
                    navController.navigate(Rutas.confirmarCita(medicoId, fecha, hora))
                },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(
            route = Rutas.CONFIRMAR_CITA,
            arguments = listOf(
                navArgument(Rutas.ARG_MEDICO_ID) { type = NavType.IntType },
                navArgument(Rutas.ARG_FECHA) { type = NavType.StringType },
                navArgument(Rutas.ARG_HORA) { type = NavType.StringType }
            )
        ) { entry ->
            val medicoId = entry.arguments?.getInt(Rutas.ARG_MEDICO_ID) ?: 0
            val fecha = entry.arguments?.getString(Rutas.ARG_FECHA).orEmpty()
            val hora = entry.arguments?.getString(Rutas.ARG_HORA).orEmpty()
            ConfirmarCitaScreen(
                medicoId = medicoId,
                fecha = fecha,
                hora = hora,
                // popUpTo(HOME) borra el flujo de agendamiento del historial
                onCitaAgendada = {
                    navController.navigate(Rutas.CITA_EXITOSA) { popUpTo(Rutas.HOME) }
                },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Rutas.CITA_EXITOSA) {
            CitaExitosaScreen(
                onVerMisCitas = {
                    navController.navigate(Rutas.MIS_CITAS) { popUpTo(Rutas.HOME) }
                },
                onIrAInicio = {
                    navController.navigate(Rutas.HOME) { popUpTo(Rutas.HOME) { inclusive = true } }
                }
            )
        }

        composable(Rutas.MIS_CITAS) {
            MisCitasScreen(
                onCitaClick = { citaId -> navController.navigate(Rutas.detalleCita(citaId)) },
                onNavegarBarra = navegarBarra
            )
        }
        composable(
            route = Rutas.DETALLE_CITA,
            arguments = listOf(navArgument(Rutas.ARG_CITA_ID) { type = NavType.IntType })
        ) { entry ->
            val citaId = entry.arguments?.getInt(Rutas.ARG_CITA_ID) ?: 0
            DetalleCitaScreen(
                citaId = citaId,
                onVolver = { navController.popBackStack() },
                onCitaCancelada = { navController.popBackStack() }
            )
        }
        composable(Rutas.RESULTADOS) {
            ResultadosScreen(onNavegarBarra = navegarBarra)
        }
        composable(Rutas.PERFIL) {
            PerfilScreen(
                onCerrarSesion = {
                    navController.navigate(Rutas.SPLASH) { popUpTo(0) }
                },
                onNavegarBarra = navegarBarra
            )
        }
        composable(Rutas.NOTIFICACIONES) {
            NotificacionesScreen(onVolver = { navController.popBackStack() })
        }
    }
}
