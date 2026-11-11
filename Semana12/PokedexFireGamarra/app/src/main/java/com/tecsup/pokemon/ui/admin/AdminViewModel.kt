package com.tecsup.pokemon.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.pokemon.AppContainer
import com.tecsup.pokemon.data.repository.ItemRepository
import com.tecsup.pokemon.notification.Notificador
import com.tecsup.pokemon.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

// Totales que muestra el panel
data class Totales(
    val guardados: Int,
    val favoritos: Int
)

class AdminViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Totales>>(UiState.Cargando)
    val estado: StateFlow<UiState<Totales>> = _estado.asStateFlow()

    // Mensaje corto después de sincronizar o limpiar
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _trabajando = MutableStateFlow(false)
    val trabajando: StateFlow<Boolean> = _trabajando.asStateFlow()

    init {
        // Los totales se actualizan solos porque salen de Room
        viewModelScope.launch {
            combine(repo.items, repo.favoritos) { items, favs -> Totales(items.size, favs.size) }
                .collect { _estado.value = UiState.Exito(it) }
        }
    }

    fun sincronizar() {
        viewModelScope.launch {
            _trabajando.value = true
            repo.refrescar()
                .onSuccess { n ->
                    _mensaje.value = "Sincronizado: se guardaron $n Pokémon"
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                }
                .onFailure { _mensaje.value = "No se pudo sincronizar. Revisa tu conexión" }
            _trabajando.value = false
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            repo.limpiarCache()
            _mensaje.value = "Caché limpiada"
        }
    }

    fun mensajeMostrado() {
        _mensaje.value = null
    }

    companion object {
        fun factory(container: AppContainer) = viewModelFactory {
            initializer { AdminViewModel(container.itemRepository, container.notificador) }
        }
    }
}
