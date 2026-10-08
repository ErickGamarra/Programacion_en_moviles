package com.saludplus.citas.ui.resultados

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraNavegacion
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: completar pantalla
@Composable
fun ResultadosScreen(onNavegarBarra: (String) -> Unit) {
    Scaffold(bottomBar = { BarraNavegacion(Rutas.RESULTADOS, onNavegarBarra) }) { padding ->
        PantallaEnConstruccion(
            titulo = "Resultados",
            modifier = Modifier.padding(padding)
        )
    }
}
