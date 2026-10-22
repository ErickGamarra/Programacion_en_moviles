package com.gamarra.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamarra.huellitasvet.data.local.relation.CitaConDetalle
import com.gamarra.huellitasvet.data.repository.VetRepository
import com.gamarra.huellitasvet.model.EstadoCita
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ErroresAtencion(val diagnostico: String? = null, val peso: String? = null)

@OptIn(ExperimentalCoroutinesApi::class)
class AtenderCitaViewModel(private val repo: VetRepository) : ViewModel() {
    private val citaId = MutableStateFlow<Int?>(null)

    val cita: StateFlow<CitaConDetalle?> = citaId.filterNotNull()
        .flatMapLatest { repo.observarCita(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _errores = MutableStateFlow(ErroresAtencion())
    val errores: StateFlow<ErroresAtencion> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun cargar(id: Int) {
        citaId.value = id
        _errores.value = ErroresAtencion()
        _mensaje.value = null
    }

    fun registrar(diagnostico: String, tratamiento: String, peso: Double?, onRegistrada: () -> Unit) {
        val actual = cita.value ?: return
        if (!sePuedeAtender(actual)) {
            _mensaje.value = "Solo se puede atender una cita pendiente o confirmada"
            return
        }
        val errores = ErroresAtencion(
            diagnostico = if (diagnostico.trim().length < 5) "Mínimo 5 caracteres" else null,
            peso = if (peso == null || peso <= 0) "Debe ser mayor que 0" else null
        )
        _errores.value = errores
        if (errores.diagnostico != null || errores.peso != null || peso == null) return
        viewModelScope.launch {
            repo.registrarAtencion(actual.cita.id, diagnostico.trim(), tratamiento.trim(), peso)
            onRegistrada()
        }
    }

    fun cancelar(onCancelada: () -> Unit) {
        val actual = cita.value ?: return
        if (!sePuedeAtender(actual)) {
            _mensaje.value = "Esta cita ya no se puede cancelar"
            return
        }
        viewModelScope.launch {
            repo.cambiarEstadoCita(actual.cita.id, EstadoCita.CANCELADA)
            onCancelada()
        }
    }

    fun sePuedeAtender(c: CitaConDetalle) =
        c.cita.estado == EstadoCita.PENDIENTE.name || c.cita.estado == EstadoCita.CONFIRMADA.name
}
