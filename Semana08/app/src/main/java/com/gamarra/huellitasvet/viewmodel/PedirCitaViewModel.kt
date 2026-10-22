package com.gamarra.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamarra.huellitasvet.data.local.entity.Mascota
import com.gamarra.huellitasvet.data.local.entity.Servicio
import com.gamarra.huellitasvet.data.prefs.PreferenciasStore
import com.gamarra.huellitasvet.data.repository.VetRepository
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
class PedirCitaViewModel(private val repo: VetRepository, prefs: PreferenciasStore) : ViewModel() {
    private val duenoId = MutableStateFlow<Int?>(null)

    val mascotas: StateFlow<List<Mascota>> = duenoId.filterNotNull()
        .flatMapLatest { repo.observarMascotasDeDueno(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val aceptarCitas: StateFlow<Boolean> = prefs.aceptarCitas
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val fechas: List<LocalDate> get() = (0L..7L).map { LocalDate.now().plusDays(it) }
    val horarios: List<String> = Horarios.delDia

    private val _mascota = MutableStateFlow<Mascota?>(null)
    val mascota: StateFlow<Mascota?> = _mascota.asStateFlow()

    private val _servicio = MutableStateFlow<Servicio?>(null)
    val servicio: StateFlow<Servicio?> = _servicio.asStateFlow()

    private val _fecha = MutableStateFlow(LocalDate.now())
    val fecha: StateFlow<LocalDate> = _fecha.asStateFlow()

    private val _hora = MutableStateFlow<String?>(null)
    val hora: StateFlow<String?> = _hora.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    val horasOcupadas: StateFlow<List<String>> = _fecha
        .flatMapLatest { repo.observarHorasOcupadas(it.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun iniciar(duenoId: Int, mascotaId: Int?) {
        this.duenoId.value = duenoId
        _servicio.value = null
        _fecha.value = LocalDate.now()
        _hora.value = null
        _mensaje.value = null
        _mascota.value = null
        if (mascotaId != null) {
            viewModelScope.launch { _mascota.value = repo.observarMascota(mascotaId).first() }
        }
    }

    fun elegirMascota(m: Mascota) {
        _mascota.value = m
    }

    fun elegirServicio(s: Servicio) {
        _servicio.value = s
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

    fun estaDisponible(h: String, ocupadas: List<String>): Boolean {
        if (h in ocupadas) return false
        val fechaHora = LocalDateTime.of(_fecha.value, LocalTime.parse(h))
        return fechaHora.isAfter(LocalDateTime.now())
    }

    fun confirmar(nota: String, onCreada: () -> Unit) {
        val m = _mascota.value
        val s = _servicio.value
        val h = _hora.value
        val f = _fecha.value
        if (!aceptarCitas.value) {
            _mensaje.value = "La clínica no está recibiendo citas"
            return
        }
        if (m == null || s == null || h == null) {
            _mensaje.value = "Elige mascota, servicio, fecha y hora"
            return
        }
        if (f.isBefore(LocalDate.now()) || f.isAfter(LocalDate.now().plusDays(7))) {
            _mensaje.value = "Solo puedes pedir citas de hoy hasta 7 días"
            return
        }
        viewModelScope.launch {
            val ocupadas = repo.observarHorasOcupadas(f.toString()).first()
            if (!estaDisponible(h, ocupadas)) {
                _hora.value = null
                _mensaje.value = "Ese horario ya no está disponible, elige otro"
                return@launch
            }
            repo.crearCita(m.id, s.id, f.toString(), h, nota.trim())
            onCreada()
        }
    }
}
