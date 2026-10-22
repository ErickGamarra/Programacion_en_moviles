package com.gamarra.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamarra.huellitasvet.data.local.entity.Servicio
import com.gamarra.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ErroresServicio(val nombre: String? = null, val duracion: String? = null, val precio: String? = null) {
    val hayErrores get() = nombre != null || duracion != null || precio != null
}

class ServiciosViewModel(private val repo: VetRepository) : ViewModel() {
    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val duraciones = listOf(15, 30, 45, 60)

    private val _errores = MutableStateFlow(ErroresServicio())
    val errores: StateFlow<ErroresServicio> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun alternar(s: Servicio) {
        viewModelScope.launch { repo.cambiarActivo(s.id, !s.activo) }
    }

    suspend fun cargar(id: Int): Servicio? = repo.observarServicios().first().find { it.id == id }

    fun guardar(servicio: Servicio, onGuardado: () -> Unit) {
        val errores = ErroresServicio(
            nombre = if (servicio.nombre.isBlank()) "El nombre es obligatorio" else null,
            duracion = if (servicio.duracionMin !in duraciones) "Elige 15, 30, 45 o 60 min" else null,
            precio = if (servicio.precio <= 0) "Debe ser mayor que 0" else null
        )
        _errores.value = errores
        if (errores.hayErrores) return
        viewModelScope.launch {
            repo.guardarServicio(servicio.copy(nombre = servicio.nombre.trim(), descripcion = servicio.descripcion.trim()))
            onGuardado()
        }
    }

    fun eliminar(servicio: Servicio, onEliminado: () -> Unit = {}) {
        viewModelScope.launch {
            if (repo.servicioTieneCitas(servicio.id)) {
                _mensaje.value = "${servicio.nombre} tiene citas registradas: no se puede eliminar, solo desactivar"
            } else {
                repo.eliminarServicio(servicio)
                _mensaje.value = null
                onEliminado()
            }
        }
    }

    fun limpiar() {
        _errores.value = ErroresServicio()
        _mensaje.value = null
    }
}
