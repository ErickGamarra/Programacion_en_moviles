package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.NuevoPost
import com.tecsup.murocampus.model.Post
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Lo que necesita la pantalla de detalle: la publicación y sus comentarios
data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

// Solo conoce la interface PostRepository: nada de Retrofit ni de Context
class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista = _lista.asStateFlow()

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle = _detalle.asStateFlow()

    // null significa Todos
    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro = _usuarioFiltro.asStateFlow()

    // null = todavía no se envió nada
    private val _publicacion = MutableStateFlow<UiState<Post>?>(null)
    val publicacion = _publicacion.asStateFlow()

    // null = todavía no se borró nada
    private val _borrado = MutableStateFlow<UiState<Unit>?>(null)
    val borrado = _borrado.asStateFlow()

    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda = _textoBusqueda.asStateFlow()

    private var postsCache: List<Post> = emptyList()

    private var trabajoDetalle: Job? = null

    init { cargar() }

    // Carga respetando el filtro actual (también lo usa Reintentar)
    fun cargar() {
        viewModelScope.launch {
            _lista.value = UiState.Cargando
            val userId = _usuarioFiltro.value
            val resultado = if (userId == null) repo.listar() else repo.porUsuario(userId)
            resultado
                .onSuccess {
                    postsCache = it
                    aplicarFiltroBusqueda()
                }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    fun abrir(id: Int) {
        _borrado.value = null
        trabajoDetalle?.cancel()          // si se abre otra publicación, se olvida la anterior
        trabajoDetalle = viewModelScope.launch {
            _detalle.value = UiState.Cargando
            // Las dos llamadas salen a la vez
            val (rPost, rComentarios) = coroutineScope {
                val post = async { repo.detalle(id) }
                val comentarios = async { repo.comentarios(id) }
                post.await() to comentarios.await()
            }
            // Si cualquiera de las dos falla, todo el detalle muestra el error
            _detalle.value = rPost.fold(
                onSuccess = { post ->
                    rComentarios.fold(
                        onSuccess = { UiState.Exito(DetalleUi(post, it)) },
                        onFailure = { UiState.Error(mensajeDe(it)) }
                    )
                },
                onFailure = { UiState.Error(mensajeDe(it)) }
            )
        }
    }

    fun publicar(titulo: String, texto: String) {
        viewModelScope.launch {
            _publicacion.value = UiState.Cargando
            repo.crear(NuevoPost(userId = 1, title = titulo, body = texto))
                .onSuccess { _publicacion.value = UiState.Exito(it) }
                .onFailure { _publicacion.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun borrar(id: Int) {
        viewModelScope.launch {
            _borrado.value = UiState.Cargando
            repo.borrar(id)
                .onSuccess {
                    _borrado.value = UiState.Exito(it)
                    // JSONPlaceholder no borra de verdad: se quita de la lista local en vez de recargar
                    postsCache = postsCache.filterNot { post -> post.id == id }
                    aplicarFiltroBusqueda()
                }
                .onFailure { _borrado.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun buscar(texto: String) {
        _textoBusqueda.value = texto
        // Solo se filtra si ya hay publicaciones descargadas: no se tapa un Cargando o un Error
        if (_lista.value is UiState.Exito) aplicarFiltroBusqueda()
    }

    private fun aplicarFiltroBusqueda() {
        val query = _textoBusqueda.value.trim()
        val filtrados = if (query.isEmpty()) {
            postsCache
        } else {
            postsCache.filter { it.title.contains(query, ignoreCase = true) }
        }
        _lista.value = UiState.Exito(filtrados)
    }

    // Al entrar de nuevo al formulario no se muestra el mensaje anterior
    fun nuevaPublicacion() {
        _publicacion.value = null
    }
}
