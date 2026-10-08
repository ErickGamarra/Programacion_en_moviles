package com.saludplus.citas.ui.agendamiento

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: LazyColumn con Repositorio.buscarMedicos(especialidadId, consulta) (filter + sortedByDescending).
@Composable
fun MedicosScreen(especialidadId: Int, onMedicoClick: (Int) -> Unit, onVolver: () -> Unit) {
    PantallaEnConstruccion(
        titulo = "Medicos (especialidad $especialidadId)",
        acciones = listOf("Medico 1" to { onMedicoClick(1) }, "Volver" to onVolver)
    )
}
