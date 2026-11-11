package com.tecsup.pokemon.data.repository

import com.tecsup.pokemon.domain.model.Item
import kotlinx.coroutines.flow.Flow

// El ViewModel depende de esta interface, nunca de la clase concreta.
interface ItemRepository {
    val items: Flow<List<Item>>               // sale de Room
    val favoritos: Flow<List<Item>>
    suspend fun refrescar(): Result<Int>      // Retrofit -> Room; devuelve cuántos guardó
    suspend fun obtenerDetalle(id: Int): Result<Item>
    suspend fun alternarFavorito(id: Int)
    suspend fun cantidadGuardada(): Int
    suspend fun cantidadFavoritos(): Int
    suspend fun limpiarCache()
}
