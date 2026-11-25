package com.tecsup.murocampus.data

import com.tecsup.murocampus.data.remote.PostApi
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.NuevoPost
import com.tecsup.murocampus.model.Post
import kotlin.coroutines.cancellation.CancellationException
import retrofit2.HttpException

// Lo único que conoce el ViewModel: funciones que devuelven Result
interface PostRepository {
    suspend fun listar(): Result<List<Post>>
    suspend fun detalle(id: Int): Result<Post>
    suspend fun comentarios(id: Int): Result<List<Comentario>>
    suspend fun porUsuario(userId: Int): Result<List<Post>>
    suspend fun crear(nuevo: NuevoPost): Result<Post>
    suspend fun borrar(id: Int): Result<Unit>
}

class PostRepositoryRemote(private val api: PostApi) : PostRepository {

    override suspend fun listar() = llamar { api.listar() }

    override suspend fun detalle(id: Int) = llamar { api.detalle(id) }

    override suspend fun comentarios(id: Int) = llamar { api.comentarios(id) }

    override suspend fun porUsuario(userId: Int) = llamar { api.porUsuario(userId) }

    override suspend fun crear(nuevo: NuevoPost) = llamar { api.crear(nuevo) }

    override suspend fun borrar(id: Int): Result<Unit> = llamar {
        val response = api.borrar(id)
        if (response.code() !in 200..299) {
            throw HttpException(response)
        }
    }

    // Un solo try/catch para las cinco llamadas: ninguna excepción sale del Repository
    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> =
        try {
            Result.success(bloque())
        } catch (e: CancellationException) {
            throw e                      // la cancelación de la corrutina no es un error
        } catch (e: Exception) {
            Result.failure(e)            // IOException, HttpException, SerializationException...
        }
}
