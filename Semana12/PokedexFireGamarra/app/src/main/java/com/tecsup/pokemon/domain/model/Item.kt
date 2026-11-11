package com.tecsup.pokemon.domain.model

// Modelo que usa la interfaz: un Pokémon de tipo fuego.
data class Item(
    val id: Int,
    val titulo: String,       // nombre del Pokémon
    val subtitulo: String,    // número de la Pokédex, por ejemplo "#004"
    val imagenUrl: String?,
    val esFavorito: Boolean = false,
    val detalle: DetallePokemon? = null   // solo se llena en la pantalla de detalle
)

// Datos extra que trae el segundo endpoint (/pokemon/{nombre}).
data class DetallePokemon(
    val tipos: List<String>,
    val alturaMetros: Double,
    val pesoKg: Double,
    val habilidades: List<String>,
    val estadisticas: List<Estadistica>,
    val experienciaBase: Int?
)

data class Estadistica(
    val nombre: String,
    val valor: Int
)
