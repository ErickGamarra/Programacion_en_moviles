package com.gamarra.tecsupfit.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gamarra.tecsupfit.data.mockGymClasses
import com.gamarra.tecsupfit.data.mockInitialReservations
import com.gamarra.tecsupfit.model.GymReservation
import com.gamarra.tecsupfit.model.ReservationStatus
import com.gamarra.tecsupfit.navigation.DestinoBottomBar
import com.gamarra.tecsupfit.navigation.DestinosSecuenciales
import com.gamarra.tecsupfit.ui.theme.FitVerdeContenedor
import com.gamarra.tecsupfit.ui.theme.FitVerdePrincipal

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var listaReservas by remember { mutableStateOf(mockInitialReservations) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val mostrarBottomBar = currentRoute in listOf(
        DestinoBottomBar.Inicio.ruta,
        DestinoBottomBar.Reservas.ruta,
        DestinoBottomBar.Rutinas.ruta,
        DestinoBottomBar.Perfil.ruta
    )

    Scaffold(
        bottomBar = {
            if (mostrarBottomBar) {
                AppBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { ruta ->
                        navController.navigate(ruta) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = DestinoBottomBar.Inicio.ruta
            ) {
                composable(DestinoBottomBar.Inicio.ruta) {
                    PantallaInicio(
                        onClassClick = { claseId ->
                            navController.navigate(DestinosSecuenciales.detalleClase(claseId))
                        }
                    )
                }

                composable(DestinoBottomBar.Reservas.ruta) {
                    PantallaReservas(
                        listaReservas = listaReservas,
                        onCancelarReserva = { idEliminar ->
                            listaReservas = listaReservas.filterNot { it.id == idEliminar }
                        }
                    )
                }

                composable(DestinoBottomBar.Rutinas.ruta) {
                    PantallaRutinas()
                }

                composable(DestinoBottomBar.Perfil.ruta) {
                    PantallaPerfilUsuario()
                }

                composable(
                    route = DestinosSecuenciales.DETALLE_CLASE,
                    arguments = listOf(navArgument("claseId") { type = NavType.StringType })
                ) { backStack ->
                    val claseId = backStack.arguments?.getString("claseId")
                    val gymClass = remember(claseId) { mockGymClasses.find { it.id == claseId } }

                    PantallaDetalleClase(
                        gymClass = gymClass,
                        onBack = { navController.popBackStack() },
                        onReservarClick = { horario ->
                            val nuevaReserva = GymReservation(
                                id = System.currentTimeMillis().toString(),
                                className = gymClass?.name ?: "Clase",
                                schedule = horario,
                                status = ReservationStatus.CONFIRMADA
                            )
                            listaReservas = listOf(nuevaReserva) + listaReservas

                            claseId?.let {
                                navController.navigate(DestinosSecuenciales.confirmacion(it, horario))
                            }
                        }
                    )
                }

                composable(
                    route = DestinosSecuenciales.CONFIRMACION,
                    arguments = listOf(
                        navArgument("claseId") { type = NavType.StringType },
                        navArgument("horario") { type = NavType.StringType }
                    )
                ) { backStack ->
                    val claseId = backStack.arguments?.getString("claseId")
                    val horario = backStack.arguments?.getString("horario") ?: ""
                    val gymClass = remember(claseId) { mockGymClasses.find { it.id == claseId } }

                    PantallaConfirmacion(
                        className = gymClass?.name ?: "Clase",
                        horario = horario,
                        onVerMisReservas = {
                            navController.navigate(DestinoBottomBar.Reservas.ruta) {
                                popUpTo(DestinoBottomBar.Inicio.ruta) {
                                    inclusive = false
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AppBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        DestinoBottomBar.Inicio,
        DestinoBottomBar.Reservas,
        DestinoBottomBar.Rutinas,
        DestinoBottomBar.Perfil
    )

    NavigationBar(containerColor = Color.White) {
        items.forEach { item ->
            val isSelected = currentRoute == item.ruta
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.ruta) },
                icon = { Icon(item.icono, contentDescription = item.titulo) },
                label = { Text(item.titulo) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = FitVerdePrincipal,
                    selectedTextColor = FitVerdePrincipal,
                    indicatorColor = FitVerdeContenedor,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}