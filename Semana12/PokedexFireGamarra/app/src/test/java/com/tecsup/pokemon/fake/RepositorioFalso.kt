package com.tecsup.pokemon.fake

import com.tecsup.pokemon.data.repository.ItemRepository
import com.tecsup.pokemon.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

// Repositorio en memoria para probar el ViewModel sin internet ni Room.
class RepositorioFalso(
    guardados: List<Item> = emptyList()
) : ItemRepository {

    private val datos = MutableStateFlow(guardados)

    // Lo que devolverá la "API" en el próximo refrescar()
    var respuestaApi: Result<List<Item>> = Result.success(emptyList())
    var llamadasRefrescar = 0

    override val items: Flow<List<Item>> = datos
    override val favoritos: Flow<List<Item>> = datos.map { l -> l.filter { it.esFavorito } }

    override suspend fun refrescar(): Result<Int> {
        llamadasRefrescar++
        return respuestaApi.map { nuevos ->
            val favs = datos.value.filter { it.esFavorito }.map { it.id }.toSet()
            datos.value = nuevos.map { it.copy(esFavorito = it.id in favs) }
            nuevos.size
        }
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> =
        datos.value.find { it.id == id }?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException())

    override suspend fun alternarFavorito(id: Int) {
        datos.value = datos.value.map { if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it }
    }

    override suspend fun cantidadGuardada() = datos.value.size
    override suspend fun cantidadFavoritos() = datos.value.count { it.esFavorito }
    override suspend fun limpiarCache() { datos.value = emptyList() }
}
