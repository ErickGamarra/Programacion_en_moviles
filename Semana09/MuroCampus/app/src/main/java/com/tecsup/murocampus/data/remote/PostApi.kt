package com.tecsup.murocampus.data.remote

import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.NuevoPost
import com.tecsup.murocampus.model.Post
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// Describe las llamadas del servicio; Retrofit genera la implementación
interface PostApi {

    @GET("posts")
    suspend fun listar(): List<Post>

    // {id} se reemplaza en la ruta: posts/1
    @GET("posts/{id}")
    suspend fun detalle(@Path("id") id: Int): Post

    @GET("posts/{id}/comments")
    suspend fun comentarios(@Path("id") id: Int): List<Comentario>

    // @Query agrega ?userId=2 al final de la URL
    @GET("posts")
    suspend fun porUsuario(@Query("userId") userId: Int): List<Post>

    // @Body convierte el objeto a JSON y lo manda en el cuerpo
    @POST("posts")
    suspend fun crear(@Body nuevo: NuevoPost): Post

    @DELETE("posts/{id}")
    suspend fun borrar(@Path("id") id: Int): Response<Unit>
}
