package com.tecsup.pokemon.data.mapper

import com.tecsup.pokemon.data.remote.dto.ItemDto
import com.tecsup.pokemon.data.remote.dto.PokemonDetalleDto
import com.tecsup.pokemon.domain.model.DetallePokemon
import com.tecsup.pokemon.domain.model.Estadistica
import com.tecsup.pokemon.domain.model.Item

private const val URL_ARTE =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"

// Imagen oficial a partir del id
fun urlArte(id: Int) = "$URL_ARTE$id.png"

// Número de Pokédex con tres cifras: 4 -> "#004"
fun numeroPokedex(id: Int) = "#" + id.toString().padStart(3, '0')

// Dto (API) -> Item (UI)
fun ItemDto.aModelo(esFavorito: Boolean = false) = Item(
    id = id,
    titulo = name,
    subtitulo = numeroPokedex(id),
    imagenUrl = urlArte(id),
    esFavorito = esFavorito
)

// Detalle (API) -> Item con su DetallePokemon
fun PokemonDetalleDto.aModelo(esFavorito: Boolean) = Item(
    id = id,
    titulo = name,
    subtitulo = numeroPokedex(id),
    imagenUrl = sprites.other?.arteOficial?.frontDefault ?: sprites.frontDefault ?: urlArte(id),
    esFavorito = esFavorito,
    detalle = DetallePokemon(
        tipos = types.sortedBy { it.slot }.map { it.type.name },
        alturaMetros = height / 10.0,
        pesoKg = weight / 10.0,
        habilidades = abilities.map { it.ability.name },
        estadisticas = stats.map { Estadistica(it.stat.name, it.baseStat) },
        experienciaBase = baseExperience
    )
)
