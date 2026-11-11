package com.tecsup.pokemon.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.pokemon.AppContainer
import com.tecsup.pokemon.data.repository.ItemRepository
import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.notification.Notificador
import com.tecsup.pokemon.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ListaViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    // Aviso cuando no hay internet pero sí hay datos guardados
    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    // Texto del buscador
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    // true mientras se sincroniza deslizando hacia abajo
    private val _refrescando = MutableStateFlow(false)
    val refrescando: StateFlow<Boolean> = _refrescando.asStateFlow()

    // Última lista que llegó de Room (sin filtrar)
    private var ultimaLista: List<Item> = emptyList()

    private var refrescoTerminado = false

    init {
        viewModelScope.launch {
            repo.items.collect { lista ->
                ultimaLista = lista
                if (lista.isNotEmpty() || refrescoTerminado) _estado.value = UiState.Exito(filtrar(lista))
            }
        }
        refrescar()
    }

    fun refrescar() {
        viewModelScope.launch {
            if (_estado.value is UiState.Error) _estado.value = UiState.Cargando
            val resultado = repo.refrescar()
            refrescoTerminado = true
            resultado
                .onSuccess { n ->
                    if (_estado.value is UiState.Cargando) _estado.value = UiState.Exito(emptyList())
                    _aviso.value = null
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                }
                .onFailure { e ->
                    if (repo.cantidadGuardada() == 0) _estado.value = UiState.Error(mensajeDe(e))
                    else _aviso.value = "Sin conexión: mostrando datos guardados"
                }
        }
    }

    // Filtra por nombre o por número de Pokédex ("char", "#006", "6")
    fun buscar(texto: String) {
        _busqueda.value = texto
        if (_estado.value is UiState.Exito) _estado.value = UiState.Exito(filtrar(ultimaLista))
    }

    // Deslizar hacia abajo: sincroniza sin quitar la lista de la pantalla
    fun refrescarDeslizando() {
        viewModelScope.launch {
            _refrescando.value = true
            repo.refrescar()
                .onSuccess { n ->
                    _aviso.value = null
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                }
                .onFailure { _aviso.value = "Sin conexión: mostrando datos guardados" }
            _refrescando.value = false
        }
    }

    private fun filtrar(lista: List<Item>): List<Item> {
        val texto = _busqueda.value.trim().lowercase().removePrefix("#")
        if (texto.isEmpty()) return lista
        return lista.filter { item ->
            item.titulo.contains(texto) || item.id.toString() == texto.trimStart('0')
        }
    }

    fun alternarFavorito(id: Int) = viewModelScope.launch { repo.alternarFavorito(id) }

    private fun mensajeDe(e: Throwable) = when (e) {
        is IOException -> "No hay conexión a internet"
        is HttpException -> "El servidor respondió con error ${e.code()}"
        else -> "Ocurrió un error inesperado"
    }

    companion object {
        fun factory(container: AppContainer) = viewModelFactory {
            initializer { ListaViewModel(container.itemRepository, container.notificador) }
        }
    }
}
