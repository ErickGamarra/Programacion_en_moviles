package com.saludplus.citas.ui.citas

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraNavegacion
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: completar pantalla
@Composable
fun MisCitasScreen(onCitaClick: (Int) -> Unit, onNavegarBarra: (String) -> Unit) {
    Scaffold(bottomBar = { BarraNavegacion(Rutas.MIS_CITAS, onNavegarBarra) }) { padding ->
        PantallaEnConstruccion(
            titulo = "Mis citas",
            acciones = listOf("Detalle cita 1" to { onCitaClick(1) }),
            modifier = Modifier.padding(padding)
        )
    }
}
