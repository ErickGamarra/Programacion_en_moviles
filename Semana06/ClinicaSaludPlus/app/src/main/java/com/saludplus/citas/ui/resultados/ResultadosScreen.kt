package com.saludplus.citas.ui.resultados

import androidx.compose.runtime.Composable
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: completar pantalla
@Composable
fun ResultadosScreen(onNavegarBarra: (String) -> Unit) {
    PantallaEnConstruccion(
        titulo = "Resultados",
        acciones = listOf("Inicio" to { onNavegarBarra(Rutas.HOME) }, "Mis citas" to { onNavegarBarra(Rutas.MIS_CITAS) }, "Perfil" to { onNavegarBarra(Rutas.PERFIL) })
    )
}
