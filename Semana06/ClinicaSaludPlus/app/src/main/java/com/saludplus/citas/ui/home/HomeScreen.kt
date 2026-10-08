package com.saludplus.citas.ui.home

import androidx.compose.runtime.Composable
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: Scaffold con NavigationBar (Inicio, Citas, Resultados, Perfil), saludo con el nombre del
// usuario actual y LazyRow de Repositorio.especialidadesDestacadas() (onEspecialidadClick(id)).
@Composable
fun HomeScreen(onAgendarCita: () -> Unit, onEspecialidadClick: (Int) -> Unit, onNotificaciones: () -> Unit, onNavegarBarra: (String) -> Unit) {
    PantallaEnConstruccion(
        titulo = "Inicio",
        acciones = listOf("Agendar cita" to onAgendarCita, "Notificaciones" to onNotificaciones, "Mis citas" to { onNavegarBarra(Rutas.MIS_CITAS) }, "Resultados" to { onNavegarBarra(Rutas.RESULTADOS) }, "Perfil" to { onNavegarBarra(Rutas.PERFIL) })
    )
}
