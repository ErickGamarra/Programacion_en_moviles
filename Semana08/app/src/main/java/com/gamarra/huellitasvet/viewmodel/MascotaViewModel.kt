package com.gamarra.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamarra.huellitasvet.data.local.entity.Mascota
import com.gamarra.huellitasvet.data.local.relation.CitaConDetalle
import com.gamarra.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class ErroresMascota(
    val nombre: String? = null,
    val especie: String? = null,
    val sexo: String? = null,
    val anio: String? = null,
    val peso: String? = null
) {
    val hayErrores get() = listOf(nombre, especie, sexo, anio, peso).any { it != null }
}

class MascotaViewModel(private val repo: VetRepository) : ViewModel() {
    private val _errores = MutableStateFlow(ErroresMascota())
    val errores: StateFlow<ErroresMascota> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun mascota(id: Int): StateFlow<Mascota?> = repo.observarMascota(id)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun historial(mascotaId: Int): StateFlow<List<CitaConDetalle>> = repo.observarHistorial(mascotaId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    suspend fun cargar(id: Int): Mascota? = repo.observarMascota(id).first()

    fun guardar(mascota: Mascota, onGuardada: () -> Unit) {
        val anioActual = LocalDate.now().year
        val errores = ErroresMascota(
            nombre = if (mascota.nombre.trim().length < 2) "Mínimo 2 caracteres" else null,
            especie = if (mascota.especie.isBlank()) "Elige una especie" else null,
            sexo = if (mascota.sexo.isBlank()) "Elige el sexo" else null,
            anio = if (mascota.anioNacimiento !in 1990..anioActual) "Entre 1990 y $anioActual" else null,
            peso = if (mascota.peso <= 0) "Debe ser mayor que 0" else null
        )
        _errores.value = errores
        if (errores.hayErrores) return
        viewModelScope.launch {
            repo.guardarMascota(mascota.copy(nombre = mascota.nombre.trim(), raza = mascota.raza.trim()))
            onGuardada()
        }
    }

    fun eliminar(mascota: Mascota, onEliminada: () -> Unit) {
        viewModelScope.launch {
            if (repo.mascotaTieneCitas(mascota.id)) {
                _mensaje.value = "No puedes eliminar a ${mascota.nombre} porque tiene citas registradas"
            } else {
                repo.eliminarMascota(mascota)
                onEliminada()
            }
        }
    }

    fun limpiar() {
        _errores.value = ErroresMascota()
        _mensaje.value = null
    }
}
