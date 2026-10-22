package com.gamarra.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamarra.huellitasvet.data.local.relation.CitaConDetalle
import com.gamarra.huellitasvet.data.repository.VetRepository
import com.gamarra.huellitasvet.model.EstadoCita
import com.gamarra.huellitasvet.model.Horarios
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
class ReprogramarCitaViewModel(private val repo: VetRepository) : ViewModel() {
    private val citaId = MutableStateFlow<Int?>(null)

    val cita: StateFlow<CitaConDetalle?> = citaId.filterNotNull()
        .flatMapLatest { repo.observarCita(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val fechas: List<LocalDate> get() = (0L..7L).map { LocalDate.now().plusDays(it) }
    val horarios: List<String> = Horarios.delDia

    private val _fecha = MutableStateFlow(LocalDate.now())
    val fecha: StateFlow<LocalDate> = _fecha.asStateFlow()

    private val _hora = MutableStateFlow<String?>(null)
    val hora: StateFlow<String?> = _hora.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    val horasOcupadas: StateFlow<List<String>> = _fecha
        .flatMapLatest { repo.observarHorasOcupadas(it.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun cargar(id: Int) {
        citaId.value = id
        _hora.value = null
        _mensaje.value = null
        viewModelScope.launch {
            val actual = repo.observarCita(id).first() ?: return@launch
            val dia = LocalDate.parse(actual.cita.fecha)
            _fecha.value = if (dia in fechas) dia else LocalDate.now()
        }
    }

    fun elegirFecha(f: LocalDate) {
        if (f == _fecha.value) return
        _fecha.value = f
        _hora.value = null
        _mensaje.value = null
    }

    fun elegirHora(h: String) {
        _hora.value = h
        _mensaje.value = null
    }

    fun puedeReprogramar(c: CitaConDetalle): Boolean {
        val activa = c.cita.estado == EstadoCita.PENDIENTE.name || c.cita.estado == EstadoCita.CONFIRMADA.name
        return activa && Duration.between(LocalDateTime.now(), fechaHora(c.cita.fecha, c.cita.hora)).toMinutes() > 120
    }

    fun estaDisponible(h: String, ocupadas: List<String>): Boolean =
        h !in ocupadas && LocalDateTime.of(_fecha.value, LocalTime.parse(h)).isAfter(LocalDateTime.now())

    fun confirmar(onReprogramada: () -> Unit) {
        val c = cita.value ?: return
        val h = _hora.value
        val f = _fecha.value
        when {
            !puedeReprogramar(c) -> _mensaje.value = "Solo puedes reprogramar citas pendientes o confirmadas hasta 2 horas antes"
            h == null -> _mensaje.value = "Elige la nueva fecha y hora"
            f.toString() == c.cita.fecha && h == c.cita.hora -> _mensaje.value = "Elige un horario distinto al actual"
            f.isBefore(LocalDate.now()) || f.isAfter(LocalDate.now().plusDays(7)) -> _mensaje.value = "Solo de hoy hasta 7 días"
            else -> viewModelScope.launch {
                val ocupadas = repo.observarHorasOcupadas(f.toString()).first()
                if (!estaDisponible(h, ocupadas)) {
                    _hora.value = null
                    _mensaje.value = "Ese horario ya no está disponible, elige otro"
                    return@launch
                }
                repo.reprogramarCita(c.cita.id, f.toString(), h)
                onReprogramada()
            }
        }
    }

    private fun fechaHora(fecha: String, hora: String): LocalDateTime =
        LocalDateTime.of(LocalDate.parse(fecha), LocalTime.parse(hora))
}
