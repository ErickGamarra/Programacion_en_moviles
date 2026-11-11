package com.tecsup.pokemon.data.repository

import com.tecsup.pokemon.data.mapper.aModelo
import com.tecsup.pokemon.data.remote.ApiService
import com.tecsup.pokemon.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import retrofit2.HttpException
import java.io.IOException

// Por ahora los datos se guardan en memoria. Luego se cambia a Room.
class ItemRepositoryImpl(
    private val api: ApiService
) : ItemRepository {

    private val memoria = MutableStateFlow<List<Item>>(emptyList())

    override val items: Flow<List<Item>> = memoria
    override val favoritos: Flow<List<Item>> = memoria.map { lista -> lista.filter { it.esFavorito } }

    override suspend fun refrescar(): Result<Int> = try {
        val favoritos = memoria.value.filter { it.esFavorito }.map { it.id }.toSet()
        val nuevos = api.listar().pokemon
            .map { it.pokemon }
            .filter { it.id < 10000 }            // solo las especies, sin formas especiales
            .map { it.aModelo(esFavorito = it.id in favoritos) }
            .sortedBy { it.id }
        memoria.value = nuevos
        Result.success(nuevos.size)
    } catch (e: IOException) {                  // sin internet
        Result.failure(e)
    } catch (e: HttpException) {                // 404, 500...
        Result.failure(e)
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> = try {
        val guardado = memoria.value.find { it.id == id }
        val nombre = guardado?.titulo ?: id.toString()
        Result.success(api.detalle(nombre).aModelo(esFavorito = guardado?.esFavorito ?: false))
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) =
        memoria.update { lista -> lista.map { if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it } }

    override suspend fun cantidadGuardada() = memoria.value.size
    override suspend fun cantidadFavoritos() = memoria.value.count { it.esFavorito }
    override suspend fun limpiarCache() { memoria.value = emptyList() }
}
