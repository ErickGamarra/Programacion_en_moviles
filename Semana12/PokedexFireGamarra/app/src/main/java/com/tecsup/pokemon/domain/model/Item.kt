package com.tecsup.pokemon.domain.model

// Modelo que usa la interfaz: un Pokémon de tipo fuego.
data class Item(
    val id: Int,
    val titulo: String,       // nombre del Pokémon
    val subtitulo: String,    // número de la Pokédex, por ejemplo "#004"
    val imagenUrl: String?,
    val esFavorito: Boolean = false
)
