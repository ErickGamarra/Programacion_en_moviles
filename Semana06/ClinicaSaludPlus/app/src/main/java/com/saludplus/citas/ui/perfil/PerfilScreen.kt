package com.saludplus.citas.ui.perfil

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraNavegacion
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: completar pantalla
@Composable
fun PerfilScreen(onCerrarSesion: () -> Unit, onNavegarBarra: (String) -> Unit) {
    Scaffold(bottomBar = { BarraNavegacion(Rutas.PERFIL, onNavegarBarra) }) { padding ->
        PantallaEnConstruccion(
            titulo = "Perfil",
            acciones = listOf("Cerrar sesion" to onCerrarSesion),
            modifier = Modifier.padding(padding)
        )
    }
}
