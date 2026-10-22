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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
class MisCitasViewModel(private val repo: VetRepository) : ViewModel() {
    private val duenoId = MutableStateFlow<Int?>(null)

    private val citas = duenoId.filterNotNull().flatMapLatest { repo.observarCitasDeDueno(it) }

    val proximas: StateFlow<List<CitaConDetalle>> = citas
        .map { lista -> lista.filter { esProxima(it) }.sortedBy { fechaHora(it) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val historial: StateFlow<List<CitaConDetalle>> = citas
        .map { lista -> lista.filterNot { esProxima(it) }.sortedByDescending { fechaHora(it) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun cargar(id: Int) {
        duenoId.value = id
    }

    fun puedeCancelar(c: CitaConDetalle): Boolean {
        val activa = c.cita.estado == EstadoCita.PENDIENTE.name || c.cita.estado == EstadoCita.CONFIRMADA.name
        return activa && Duration.between(LocalDateTime.now(), fechaHora(c)).toMinutes() > 120
    }

    fun cancelar(c: CitaConDetalle) {
        if (!puedeCancelar(c)) {
            _mensaje.value = "Ya no puedes cancelar esta cita"
            return
        }
        viewModelScope.launch { repo.cambiarEstadoCita(c.cita.id, EstadoCita.CANCELADA) }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    private fun esProxima(c: CitaConDetalle): Boolean {
        val activa = c.cita.estado == EstadoCita.PENDIENTE.name || c.cita.estado == EstadoCita.CONFIRMADA.name
        return activa && !fechaHora(c).isBefore(LocalDateTime.now())
    }

    private fun fechaHora(c: CitaConDetalle): LocalDateTime =
        LocalDateTime.of(LocalDate.parse(c.cita.fecha), LocalTime.parse(c.cita.hora))
}
