package com.saludplus.citas.ui.agendamiento

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: campo de busqueda en tiempo real + LazyColumn con Repositorio.buscarEspecialidades(consulta).
@Composable
fun EspecialidadesScreen(onEspecialidadClick: (Int) -> Unit, onVolver: () -> Unit) {
    PantallaEnConstruccion(
        titulo = "Especialidades",
        acciones = listOf("Medicina General (id 1)" to { onEspecialidadClick(1) }, "Volver" to onVolver)
    )
}
