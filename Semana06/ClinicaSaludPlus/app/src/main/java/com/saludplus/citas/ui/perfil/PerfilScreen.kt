package com.saludplus.citas.ui.perfil

import androidx.compose.runtime.Composable
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: completar pantalla
@Composable
fun PerfilScreen(onCerrarSesion: () -> Unit, onNavegarBarra: (String) -> Unit) {
    PantallaEnConstruccion(
        titulo = "Perfil",
        acciones = listOf("Cerrar sesion" to onCerrarSesion, "Inicio" to { onNavegarBarra(Rutas.HOME) }, "Mis citas" to { onNavegarBarra(Rutas.MIS_CITAS) }, "Resultados" to { onNavegarBarra(Rutas.RESULTADOS) })
    )
}
