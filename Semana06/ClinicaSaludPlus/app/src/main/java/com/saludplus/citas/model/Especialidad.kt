package com.saludplus.citas.model

data class Especialidad(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val destacada: Boolean = false
)
