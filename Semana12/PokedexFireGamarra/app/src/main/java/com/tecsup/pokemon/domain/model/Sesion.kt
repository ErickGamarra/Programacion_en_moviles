package com.tecsup.pokemon.domain.model

// Tipos de usuario. El rol no se decide en la app: se lee de Firestore (usuarios/{uid}).
enum class Rol { ADMIN, USUARIO }

// Persona que inició sesión.
data class Sesion(
    val uid: String,
    val correo: String,
    val rol: Rol
)
