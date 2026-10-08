package com.saludplus.citas.model

// fecha "yyyy-MM-dd", hora "HH:mm"
data class Cita(
    val id: Int,
    val usuarioId: Int,
    val medicoId: Int,
    val especialidadId: Int,
    val fecha: String,
    val hora: String
)
