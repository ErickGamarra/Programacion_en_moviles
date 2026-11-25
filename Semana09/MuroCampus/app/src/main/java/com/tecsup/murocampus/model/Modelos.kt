package com.tecsup.murocampus.model

import kotlinx.serialization.Serializable

// [E] Ejemplo resuelto
@Serializable
data class Usuario(
    val id: Int,
    val name: String,
    val email: String
)

// Una publicación tal como llega de GET posts y GET posts/{id}
@Serializable
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

// Un comentario de GET posts/{id}/comments
@Serializable
data class Comentario(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String
)

// Lo que enviamos en el POST: no lleva id porque lo asigna el servidor
@Serializable
data class NuevoPost(
    val userId: Int,
    val title: String,
    val body: String
)
