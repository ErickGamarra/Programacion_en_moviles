package com.tecsup.pokemon.data.repository

import com.tecsup.pokemon.data.local.ItemDao
import com.tecsup.pokemon.data.mapper.aEntity
import com.tecsup.pokemon.data.mapper.aModelo
import com.tecsup.pokemon.data.remote.ApiService
import com.tecsup.pokemon.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

// Room es la fuente única de verdad: la UI lee de Room y Retrofit solo la actualiza.
class ItemRepositoryImpl(
    private val api: ApiService,
    private val dao: ItemDao
) : ItemRepository {

    override val items: Flow<List<Item>> = dao.todos().map { lista -> lista.map { it.aModelo() } }
    override val favoritos: Flow<List<Item>> = dao.favoritos().map { lista -> lista.map { it.aModelo() } }

    override suspend fun refrescar(): Result<Int> = try {
        // Si se inserta sin cuidado se pierden los favoritos. Se conservan aquí:
        val favoritos = dao.idsFavoritos().toSet()
        val nuevos = api.listar().pokemon
            .map { it.pokemon }
            .filter { it.id < 10000 }            // solo las especies, sin formas especiales
            .map { it.aEntity(esFavorito = it.id in favoritos) }
        dao.guardarTodos(nuevos)
        Result.success(nuevos.size)
    } catch (e: IOException) {                  // sin internet
        Result.failure(e)
    } catch (e: HttpException) {                // 404, 500...
        Result.failure(e)
    }

    // Segundo endpoint: busca el nombre en Room y pide /pokemon/{nombre}
    override suspend fun obtenerDetalle(id: Int): Result<Item> = try {
        val guardado = dao.buscar(id)
        val nombre = guardado?.titulo ?: id.toString()
        Result.success(api.detalle(nombre).aModelo(esFavorito = guardado?.esFavorito ?: false))
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) = dao.alternarFavorito(id)
    override suspend fun cantidadGuardada() = dao.contar()
    override suspend fun cantidadFavoritos() = dao.contarFavoritos()
    override suspend fun limpiarCache() = dao.borrarTodo()
}
