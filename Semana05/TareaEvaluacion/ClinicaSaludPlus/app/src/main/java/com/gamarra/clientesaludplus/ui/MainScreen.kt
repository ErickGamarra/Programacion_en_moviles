package com.gamarra.clientesaludplus.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gamarra.clientesaludplus.navigation.Rutas
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Variable simple para rastrear la opción seleccionada en el menú
    var seccionActual by remember { mutableStateOf(Rutas.INICIO) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Juan Pérez",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    text = "Paciente",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Destino 1: Inicio
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = seccionActual == Rutas.INICIO,
                    onClick = {
                        seccionActual = Rutas.INICIO
                        coroutineScope.launch { drawerState.close() }
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.INICIO) { inclusive = true }
                        }
                    }
                )

                // Destino 2: Mis citas
                NavigationDrawerItem(
                    label = { Text("Mis citas") },
                    selected = seccionActual == Rutas.MIS_CITAS,
                    onClick = {
                        seccionActual = Rutas.MIS_CITAS
                        coroutineScope.launch { drawerState.close() }
                        navController.navigate(Rutas.MIS_CITAS)
                    }
                )

                // Destino 3: Historial médico
                NavigationDrawerItem(
                    label = { Text("Historial médico") },
                    selected = seccionActual == Rutas.HISTORIAL,
                    onClick = {
                        seccionActual = Rutas.HISTORIAL
                        coroutineScope.launch { drawerState.close() }
                        navController.navigate(Rutas.HISTORIAL)
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Clínica Salud+") },
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir Menú")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { paddingValores ->
            // Contenedor principal con el padding aplicado conforme exige la rúbrica
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValores)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Rutas.INICIO
                ) {

                    //composable(Rutas.INICIO) {
                      //  Text(
                        //    text = "Pantalla de Inicio (Base)",
                          //  modifier = Modifier.padding(16.dp)
                       // )
                   // }

                    // Reemplaza las rutas de arriba para conectarlo con pantallaInicio
                    composable(Rutas.INICIO) {
                        PantallaInicio(
                            onDoctorClick = { doctorId ->
                                navController.navigate("perfil/$doctorId")
                            }
                        )
                    }

                    // Se reemplaza la ruta de abajo
                    // composable(Rutas.MIS_CITAS) {
                    //     Text(text = "Pantalla de Mis Citas (Base)", modifier = Modifier.padding(16.dp))
                    // }

                    // Por esto:
                    composable(Rutas.MIS_CITAS) {
                        PantallaMisCitas()
                    }


                    composable(Rutas.HISTORIAL) {
                        Text(
                            text = "Historial Médico (Próximamente)",
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    //Ruta dentro del NavHOst
                    composable(Rutas.PERFIL) { backStackEntry ->
                        val doctorId = backStackEntry.arguments?.getString("doctorId")
                        val doctor = com.gamarra.clientesaludplus.data.mockDoctors.find { it.id == doctorId }

                        PantallaPerfilMedico(
                            doctor = doctor,
                            onBackClick = { navController.popBackStack() },
                            onAgendarClick = { id ->
                                navController.navigate("agendar/$id")
                            }
                        )
                    }

                    // Ruta de la nueva screen para agendar clientes
                    composable(Rutas.AGENDAR) { backStackEntry ->
                        val doctorId = backStackEntry.arguments?.getString("doctorId") ?: ""
                        PantallaAgendarCita(
                            doctorId = doctorId,
                            onBackClick = { navController.popBackStack() },
                            onConfirmarClick = { docId, fecha, hora ->
                                navController.navigate("confirmacion/$docId/$fecha/$hora")
                            }
                        )
                    }

                    // Conector para la pantalla de confirmacion
                    composable(Rutas.CONFIRMACION) { backStackEntry ->
                        val doctorId = backStackEntry.arguments?.getString("doctorId") ?: ""
                        val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
                        val hora = backStackEntry.arguments?.getString("hora") ?: ""

                        // Buscamos el nombre del médico según el id recibido
                        val doctor = com.gamarra.clientesaludplus.data.mockDoctors.find { it.id == doctorId }
                        val doctorNombre = doctor?.name ?: "Médico general"

                        PantallaConfirmacion(
                            doctorNombre = doctorNombre,
                            fecha = fecha,
                            hora = hora,
                            onVolverInicio = {
                                navController.navigate(Rutas.INICIO) {
                                    popUpTo(Rutas.INICIO) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}