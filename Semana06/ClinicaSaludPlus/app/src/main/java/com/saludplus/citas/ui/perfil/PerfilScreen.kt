package com.saludplus.citas.ui.perfil

import androidx.compose.runtime.Composable
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: datos de Repositorio.usuarioActual, cantidad de citas y boton que llame a
// Repositorio.cerrarSesion() antes de onCerrarSesion().
@Composable
fun PerfilScreen(onCerrarSesion: () -> Unit, onNavegarBarra: (String) -> Unit) {
    PantallaEnConstruccion(
        titulo = "Perfil",
        acciones = listOf("Cerrar sesion" to onCerrarSesion, "Inicio" to { onNavegarBarra(Rutas.HOME) }, "Mis citas" to { onNavegarBarra(Rutas.MIS_CITAS) }, "Resultados" to { onNavegarBarra(Rutas.RESULTADOS) })
    )
}
