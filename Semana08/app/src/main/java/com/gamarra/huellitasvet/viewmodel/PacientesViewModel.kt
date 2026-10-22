package com.gamarra.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamarra.huellitasvet.data.local.relation.MascotaConDueno
import com.gamarra.huellitasvet.data.repository.VetRepository
import com.gamarra.huellitasvet.model.Especie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class PacientesViewModel(private val repo: VetRepository) : ViewModel() {
    private val _texto = MutableStateFlow("")
    val texto: StateFlow<String> = _texto.asStateFlow()

    private val _especie = MutableStateFlow<Especie?>(null)
    val especie: StateFlow<Especie?> = _especie.asStateFlow()

    private val todas = repo.observarTodasLasMascotas()

    val pacientes: StateFlow<List<MascotaConDueno>> = combine(todas, _texto, _especie) { lista, texto, especie ->
        val busqueda = texto.trim().lowercase()
        lista.filter { p ->
            val coincideTexto = busqueda.isEmpty() ||
                p.mascota.nombre.lowercase().contains(busqueda) ||
                p.dueno.nombre.lowercase().contains(busqueda)
            val coincideEspecie = especie == null || p.mascota.especie == especie.name
            coincideTexto && coincideEspecie
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val ultimaVisita: StateFlow<Map<Int, String>> = todas
        .flatMapLatest { lista ->
            if (lista.isEmpty()) flowOf(emptyMap())
            else combine(lista.map { p ->
                repo.observarHistorial(p.mascota.id).map { p.mascota.id to it.firstOrNull()?.cita?.fecha }
            }) { pares -> pares.mapNotNull { (id, fecha) -> fecha?.let { id to it } }.toMap() }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun buscar(t: String) {
        _texto.value = t
    }

    fun filtrar(e: Especie?) {
        _especie.value = e
    }
}
