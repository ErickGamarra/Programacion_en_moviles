package com.gamarra.clientesaludplus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gamarra.clientesaludplus.data.mockDoctors
import com.gamarra.clientesaludplus.data.mockInitialAppointments
import com.gamarra.clientesaludplus.model.Appointment
import com.gamarra.clientesaludplus.model.AppointmentStatus
import com.gamarra.clientesaludplus.ui.theme.ClinicaMorado
import com.gamarra.clientesaludplus.ui.theme.ClinicaMoradoClaro
import com.gamarra.clientesaludplus.ui.theme.ClinicaMoradoPastel
import kotlinx.coroutines.launch

private object Destinos {
    const val INICIO = "inicio"
    const val MIS_CITAS = "mis_citas"
    const val HISTORIAL = "historial"
    const val PERFIL_USUARIO = "perfil_usuario"
    const val PERFIL_MEDICO = "perfil/{doctorId}"
    const val AGENDAR_CITA = "agendar/{doctorId}"
    const val CONFIRMACION = "confirmacion/{doctorId}/{fecha}/{hora}"

    fun perfilMedico(doctorId: String) = "perfil/$doctorId"
    fun agendarCita(doctorId: String) = "agendar/$doctorId"
    fun confirmacion(doctorId: String, fecha: String, hora: String) =
        "confirmacion/$doctorId/$fecha/$hora"
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    var listaCitas by remember { mutableStateOf(mockInitialAppointments) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Destinos.INICIO

    val drawerItems = listOf(
        Pair("Inicio", Destinos.INICIO),
        Pair("Mis citas", Destinos.MIS_CITAS),
        Pair("Historial médico", Destinos.HISTORIAL),
        Pair("Perfil", Destinos.PERFIL_USUARIO)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(300.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(ClinicaMoradoPastel),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "JP",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = ClinicaMorado
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Juan Pérez",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Paciente",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = DividerDefaults.color.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(16.dp))

                drawerItems.forEach { (titulo, ruta) ->
                    val isSelected = currentRoute == ruta
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Circle,
                                contentDescription = null,
                                tint = if (isSelected) ClinicaMorado else Color.DarkGray
                            )
                        },
                        label = {
                            Text(
                                text = titulo,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) ClinicaMorado else Color.Black
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate(ruta) {
                                popUpTo(Destinos.INICIO) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = ClinicaMoradoClaro,
                            unselectedContainerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    ) {
        Scaffold { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Destinos.INICIO
                ) {
                    composable(Destinos.INICIO) {
                        PantallaInicio(
                            onDoctorClick = { doctorId ->
                                navController.navigate(Destinos.perfilMedico(doctorId))
                            },
                            onMenuClick = {
                                coroutineScope.launch { drawerState.open() }
                            }
                        )
                    }

                    composable(
                        route = Destinos.PERFIL_MEDICO,
                        arguments = listOf(navArgument("doctorId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val doctorId = backStackEntry.arguments?.getString("doctorId")
                        val doctor = remember(doctorId) { mockDoctors.find { it.id == doctorId } }
                        PantallaPerfilMedico(
                            doctor = doctor,
                            onBack = { navController.popBackStack() },
                            onAgendarClick = {
                                doctorId?.let { navController.navigate(Destinos.agendarCita(it)) }
                            }
                        )
                    }

                    composable(
                        route = Destinos.AGENDAR_CITA,
                        arguments = listOf(navArgument("doctorId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val doctorId = backStackEntry.arguments?.getString("doctorId")
                        val doctor = remember(doctorId) { mockDoctors.find { it.id == doctorId } }
                        PantallaAgendarCita(
                            doctor = doctor,
                            onBack = { navController.popBackStack() },
                            onConfirmar = { fecha, hora ->
                                val nuevaCita = Appointment(
                                    id = System.currentTimeMillis().toString(),
                                    doctorName = doctor?.name ?: "Médico",
                                    specialty = doctor?.specialty ?: "General",
                                    date = fecha,
                                    time = hora,
                                    status = AppointmentStatus.CONFIRMADA
                                )
                                listaCitas = listOf(nuevaCita) + listaCitas

                                doctorId?.let {
                                    navController.navigate(Destinos.confirmacion(it, fecha, hora))
                                }
                            }
                        )
                    }

                    composable(
                        route = Destinos.CONFIRMACION,
                        arguments = listOf(
                            navArgument("doctorId") { type = NavType.StringType },
                            navArgument("fecha") { type = NavType.StringType },
                            navArgument("hora") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val doctorId = backStackEntry.arguments?.getString("doctorId")
                        val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
                        val hora = backStackEntry.arguments?.getString("hora") ?: ""
                        val doctor = remember(doctorId) { mockDoctors.find { it.id == doctorId } }

                        PantallaConfirmacion(
                            doctorName = doctor?.name ?: "Médico",
                            fecha = fecha,
                            hora = hora,
                            onVerMisCitas = {
                                navController.navigate(Destinos.MIS_CITAS) {
                                    popUpTo(Destinos.INICIO)
                                }
                            }
                        )
                    }

                    composable(Destinos.MIS_CITAS) {
                        PantallaMisCitas(
                            listaCitas = listaCitas,
                            onCancelarConfirmada = { idEliminar ->
                                listaCitas = listaCitas.filterNot { it.id == idEliminar }
                            },
                            onMenuClick = {
                                coroutineScope.launch { drawerState.open() }
                            }
                        )
                    }

                    composable(Destinos.HISTORIAL) {
                        PantallaHistorialMedico(
                            onMenuClick = {
                                coroutineScope.launch { drawerState.open() }
                            }
                        )
                    }

                    composable(Destinos.PERFIL_USUARIO) {
                        PantallaPerfilUsuario(
                            onMenuClick = {
                                coroutineScope.launch { drawerState.open() }
                            }
                        )
                    }
                }
            }
        }
    }
}