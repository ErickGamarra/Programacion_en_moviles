package com.saludplus.citas.model

// fecha en formato "yyyy-MM-dd" (ej. "2026-10-16") y hora en formato "HH:mm" (ej. "09:00")
data class Cita(
    val id: Int,
    val usuarioId: Int,
    val medicoId: Int,
    val especialidadId: Int,
    val fecha: String,
    val hora: String
)
