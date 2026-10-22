package com.gamarra.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamarra.huellitasvet.data.local.entity.Vacuna
import com.gamarra.huellitasvet.data.repository.VetRepository
import com.gamarra.huellitasvet.model.EstadoVacuna
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeParseException

data class ErroresVacuna(val nombre: String? = null, val fecha: String? = null, val proximaDosis: String? = null) {
    val hayErrores get() = nombre != null || fecha != null || proximaDosis != null
}

class VacunasViewModel(private val repo: VetRepository) : ViewModel() {
    private val _errores = MutableStateFlow(ErroresVacuna())
    val errores: StateFlow<ErroresVacuna> = _errores.asStateFlow()

    fun vacunas(mascotaId: Int): StateFlow<List<Vacuna>> = repo.observarVacunas(mascotaId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun estado(v: Vacuna): EstadoVacuna {
        val proxima = v.proximaDosis?.let { LocalDate.parse(it) } ?: return EstadoVacuna.DOSIS_UNICA
        val hoy = LocalDate.now()
        return when {
            proxima.isBefore(hoy) -> EstadoVacuna.VENCIDA
            !proxima.isAfter(hoy.plusDays(30)) -> EstadoVacuna.PROXIMA
            else -> EstadoVacuna.AL_DIA
        }
    }

    fun guardar(mascotaId: Int, nombre: String, fecha: String, proximaDosis: String, onGuardada: () -> Unit) {
        val aplicada = leerFecha(fecha)
        val proxima = proximaDosis.takeIf { it.isNotBlank() }?.let { leerFecha(it) }
        val errores = ErroresVacuna(
            nombre = if (nombre.trim().length < 3) "Mínimo 3 caracteres" else null,
            fecha = when {
                aplicada == null -> "Usa el formato aaaa-mm-dd"
                aplicada.isAfter(LocalDate.now()) -> "No puede ser una fecha futura"
                else -> null
            },
            proximaDosis = when {
                proximaDosis.isBlank() -> null
                proxima == null -> "Usa el formato aaaa-mm-dd"
                aplicada != null && !proxima.isAfter(aplicada) -> "Debe ser posterior a la fecha aplicada"
                else -> null
            }
        )
        _errores.value = errores
        if (errores.hayErrores || aplicada == null) return
        viewModelScope.launch {
            repo.guardarVacuna(Vacuna(mascotaId = mascotaId, nombre = nombre.trim(), fecha = aplicada.toString(), proximaDosis = proxima?.toString()))
            onGuardada()
        }
    }

    fun eliminar(v: Vacuna) {
        viewModelScope.launch { repo.eliminarVacuna(v) }
    }

    fun limpiar() {
        _errores.value = ErroresVacuna()
    }

    private fun leerFecha(texto: String): LocalDate? = try {
        LocalDate.parse(texto.trim())
    } catch (e: DateTimeParseException) {
        null
    }
}
