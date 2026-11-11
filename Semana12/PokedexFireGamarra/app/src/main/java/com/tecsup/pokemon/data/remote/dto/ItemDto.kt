package com.tecsup.pokemon.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Lo que responde GET /type/fire
// { "pokemon": [ { "pokemon": { "name": "charmander", "url": ".../pokemon/4/" }, "slot": 1 }, ... ] }
@Serializable
data class TipoRespuestaDto(
    val pokemon: List<TipoPokemonDto>
)

@Serializable
data class TipoPokemonDto(
    val pokemon: ItemDto,
    val slot: Int
)

// Un elemento de la lista: nombre y url. El id sale del final de la url.
@Serializable
data class ItemDto(
    val name: String,
    val url: String
) {
    val id: Int
        get() = url.trimEnd('/').substringAfterLast('/').toInt()
}

// Lo que responde GET /pokemon/{nombre}
@Serializable
data class PokemonDetalleDto(
    val id: Int,
    val name: String,
    val height: Int,                     // decímetros
    val weight: Int,                     // hectogramos
    @SerialName("base_experience") val baseExperience: Int? = null,
    val types: List<TipoSlotDto>,
    val stats: List<StatDto>,
    val abilities: List<HabilidadSlotDto>,
    val sprites: SpritesDto
)

@Serializable
data class RecursoDto(val name: String, val url: String)

@Serializable
data class TipoSlotDto(val slot: Int, val type: RecursoDto)

@Serializable
data class StatDto(
    @SerialName("base_stat") val baseStat: Int,
    val stat: RecursoDto
)

@Serializable
data class HabilidadSlotDto(
    val ability: RecursoDto,
    @SerialName("is_hidden") val oculta: Boolean = false
)

@Serializable
data class SpritesDto(
    @SerialName("front_default") val frontDefault: String? = null,
    val other: OtrosSpritesDto? = null
)

@Serializable
data class OtrosSpritesDto(
    @SerialName("official-artwork") val arteOficial: ArteDto? = null
)

@Serializable
data class ArteDto(
    @SerialName("front_default") val frontDefault: String? = null
)
