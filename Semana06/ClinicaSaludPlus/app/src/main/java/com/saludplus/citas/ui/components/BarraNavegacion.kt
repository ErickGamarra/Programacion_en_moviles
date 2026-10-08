package com.saludplus.citas.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.saludplus.citas.navigation.Rutas

private data class DestinoBarra(val ruta: String, val etiqueta: String, val icono: ImageVector)

private val destinos = listOf(
    DestinoBarra(Rutas.HOME, "Inicio", Icons.Filled.Home),
    DestinoBarra(Rutas.MIS_CITAS, "Mis citas", Icons.Filled.Event),
    DestinoBarra(Rutas.RESULTADOS, "Resultados", Icons.Filled.Description),
    DestinoBarra(Rutas.PERFIL, "Perfil", Icons.Filled.Person)
)

@Composable
fun BarraNavegacion(rutaActual: String, onNavegar: (String) -> Unit) {
    NavigationBar {
        destinos.forEach { destino ->
            NavigationBarItem(
                selected = destino.ruta == rutaActual,
                onClick = { onNavegar(destino.ruta) },
                icon = { Icon(destino.icono, contentDescription = destino.etiqueta) },
                label = { Text(destino.etiqueta) }
            )
        }
    }
}
