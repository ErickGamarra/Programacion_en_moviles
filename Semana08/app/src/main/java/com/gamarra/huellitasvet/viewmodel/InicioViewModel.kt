package com.gamarra.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamarra.huellitasvet.data.local.entity.Mascota
import com.gamarra.huellitasvet.data.local.relation.CitaConDetalle
import com.gamarra.huellitasvet.data.repository.VetRepository
import com.gamarra.huellitasvet.model.EstadoCita
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
class InicioViewModel(private val repo: VetRepository) : ViewModel() {
    private val duenoId = MutableStateFlow<Int?>(null)

    val mascotas: StateFlow<List<Mascota>> = duenoId.filterNotNull()
        .flatMapLatest { repo.observarMascotasDeDueno(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val proximaCita: StateFlow<CitaConDetalle?> = duenoId.filterNotNull()
        .flatMapLatest { repo.observarCitasDeDueno(it) }
        .map { citas ->
            val ahora = LocalDateTime.now()
            citas.filter {
                (it.cita.estado == EstadoCita.PENDIENTE.name || it.cita.estado == EstadoCita.CONFIRMADA.name) &&
                    !fechaHora(it).isBefore(ahora)
            }.minByOrNull { fechaHora(it) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun cargar(id: Int) {
        duenoId.value = id
    }

    private fun fechaHora(c: CitaConDetalle): LocalDateTime =
        LocalDateTime.of(LocalDate.parse(c.cita.fecha), LocalTime.parse(c.cita.hora))
}
