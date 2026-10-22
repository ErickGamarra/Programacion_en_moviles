package com.gamarra.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamarra.huellitasvet.data.repository.VetRepository
import com.gamarra.huellitasvet.model.EstadoCita
import com.gamarra.huellitasvet.ui.components.graficos.DatoGrafico
import com.gamarra.huellitasvet.ui.theme.AzulVet
import com.gamarra.huellitasvet.ui.theme.EstadoAtendida
import com.gamarra.huellitasvet.ui.theme.EstadoCancelada
import com.gamarra.huellitasvet.ui.theme.EstadoConfirmada
import com.gamarra.huellitasvet.ui.theme.EstadoPendiente
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class PanelViewModel(private val repo: VetRepository) : ViewModel() {
    private val inicio = SharingStarted.WhileSubscribed(5000)

    val citasHoy: StateFlow<Int> = repo.observarCitasDeFecha(LocalDate.now().toString())
        .stateIn(viewModelScope, inicio, 0)

    val pendientes: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoCita.PENDIENTE)
        .stateIn(viewModelScope, inicio, 0)

    val atendidas: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoCita.ATENDIDA)
        .stateIn(viewModelScope, inicio, 0)

    val ingresosMes: StateFlow<Double> = repo.observarIngresosDesde(LocalDate.now().withDayOfMonth(1).toString())
        .stateIn(viewModelScope, inicio, 0.0)

    val porEstado: StateFlow<List<DatoGrafico>> = combine(
        repo.observarCantidadPorEstado(EstadoCita.PENDIENTE),
        repo.observarCantidadPorEstado(EstadoCita.CONFIRMADA),
        repo.observarCantidadPorEstado(EstadoCita.ATENDIDA),
        repo.observarCantidadPorEstado(EstadoCita.CANCELADA)
    ) { p, c, a, x ->
        listOf(
            DatoGrafico("Pendientes", p.toFloat(), EstadoPendiente),
            DatoGrafico("Confirmadas", c.toFloat(), EstadoConfirmada),
            DatoGrafico("Atendidas", a.toFloat(), EstadoAtendida),
            DatoGrafico("Canceladas", x.toFloat(), EstadoCancelada)
        )
    }.stateIn(viewModelScope, inicio, emptyList())

    val porServicio: StateFlow<List<DatoGrafico>> = repo.observarCitasPorServicio()
        .map { lista -> lista.map { DatoGrafico(it.nombre, it.total.toFloat(), AzulVet) } }
        .stateIn(viewModelScope, inicio, emptyList())
}
