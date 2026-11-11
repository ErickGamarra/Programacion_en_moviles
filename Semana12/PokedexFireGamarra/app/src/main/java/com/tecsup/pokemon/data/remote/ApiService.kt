package com.tecsup.pokemon.data.remote

import com.tecsup.pokemon.data.remote.dto.PokemonDetalleDto
import com.tecsup.pokemon.data.remote.dto.TipoRespuestaDto
import retrofit2.http.GET
import retrofit2.http.Path

// Endpoints de PokeAPI que usa el grupo (tema Pokémon, variante fire).
interface ApiService {

    // Lista: todos los Pokémon de un tipo
    @GET("type/{tipo}")
    suspend fun listar(@Path("tipo") tipo: String = TIPO): TipoRespuestaDto

    // Detalle: un Pokémon por su nombre
    @GET("pokemon/{nombre}")
    suspend fun detalle(@Path("nombre") nombre: String): PokemonDetalleDto

    companion object {
        const val TIPO = "fire"
    }
}
