package com.tecsup.pokemon.ui.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.pokemon.AppContainer
import com.tecsup.pokemon.data.repository.ItemRepository
import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DetalleViewModel(
    private val repo: ItemRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // El id llega como argumento de la ruta "detalle/{id}"
    private val id: Int = checkNotNull(savedStateHandle["id"])

    private val _estado = MutableStateFlow<UiState<Item>>(UiState.Cargando)
    val estado: StateFlow<UiState<Item>> = _estado.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            repo.obtenerDetalle(id)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure { _estado.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun alternarFavorito() {
        val actual = (_estado.value as? UiState.Exito)?.datos ?: return
        viewModelScope.launch {
            repo.alternarFavorito(id)
            _estado.value = UiState.Exito(actual.copy(esFavorito = !actual.esFavorito))
        }
    }

    private fun mensajeDe(e: Throwable) = when (e) {
        is IOException -> "No hay conexión a internet"
        is HttpException -> "El servidor respondió con error ${e.code()}"
        else -> "Ocurrió un error inesperado"
    }

    companion object {
        fun factory(container: AppContainer) = viewModelFactory {
            initializer { DetalleViewModel(container.itemRepository, createSavedStateHandle()) }
        }
    }
}
