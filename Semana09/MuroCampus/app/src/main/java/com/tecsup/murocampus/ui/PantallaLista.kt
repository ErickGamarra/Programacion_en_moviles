package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

// La pantalla solo dibuja el estado: no sabe de dónde vienen los datos
@Composable
fun PantallaLista(
    vm: PostsViewModel,
    onAbrir: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by vm.lista.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()

    Column(modifier.fillMaxSize()) {
        FiltroUsuarios(seleccionado = filtro, onElegir = vm::filtrar)

        Box(Modifier.weight(1f).fillMaxWidth()) {
            when (val e = estado) {
                is UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorConReintento(e.mensaje, onReintentar = vm::cargar)
                is UiState.Exito ->
                    if (e.datos.isEmpty()) {
                        Text("Sin publicaciones", Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(e.datos, key = { it.id }) { post ->
                                TarjetaPost(
                                    userId = post.userId,
                                    titulo = post.title,
                                    texto = post.body,
                                    onClick = { onAbrir(post.id) }
                                )
                            }
                        }
                    }
            }
        }
    }
}

// Fila de chips desplazable: Todos y Usuario 1 a Usuario 10
@Composable
private fun FiltroUsuarios(seleccionado: Int?, onElegir: (Int?) -> Unit) {
    val opciones: List<Int?> = listOf(null) + (1..10)
    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(opciones) { userId ->
            FilterChip(
                selected = seleccionado == userId,
                onClick = { onElegir(userId) },
                label = { Text(if (userId == null) "Todos" else "Usuario $userId") }
            )
        }
    }
}
