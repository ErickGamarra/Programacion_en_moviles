package com.saludplus.citas.ui.citas

import androidx.compose.runtime.Composable
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: Scaffold con NavigationBar y LazyColumn con Repositorio.citasDelUsuario();
// mostrar mensaje si la lista esta vacia.
@Composable
fun MisCitasScreen(onCitaClick: (Int) -> Unit, onNavegarBarra: (String) -> Unit) {
    PantallaEnConstruccion(
        titulo = "Mis citas",
        acciones = listOf("Detalle cita 1" to { onCitaClick(1) }, "Inicio" to { onNavegarBarra(Rutas.HOME) }, "Resultados" to { onNavegarBarra(Rutas.RESULTADOS) }, "Perfil" to { onNavegarBarra(Rutas.PERFIL) })
    )
}
