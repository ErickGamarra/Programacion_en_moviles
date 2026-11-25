package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(vm: PostsViewModel, id: Int, modifier: Modifier = Modifier) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    val estadoBorrar by vm.borrado.collectAsStateWithLifecycle()

    Box(modifier.fillMaxSize()) {
        when (val e = estado) {
            is UiState.Cargando -> Cargando("Cargando publicación…")
            is UiState.Error -> ErrorConReintento(e.mensaje, onReintentar = { vm.abrir(id) })
            is UiState.Exito -> {
                val post = e.datos.post
                val comentarios = e.datos.comentarios
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text("Usuario ${post.userId}", color = MaterialTheme.colorScheme.primary)
                        Text(
                            post.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(post.body, style = MaterialTheme.typography.bodyLarge)
                        HorizontalDivider(Modifier.padding(top = 12.dp))
                    }
                    item {
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { vm.borrar(id) },
                            enabled = estadoBorrar !is UiState.Cargando,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (estadoBorrar is UiState.Cargando) {
                                CircularProgressIndicator(
                                    Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onError
                                )
                            } else {
                                Text("Eliminar publicación")
                            }
                        }
                    }
                    item {
                        when (val eb = estadoBorrar) {
                            is UiState.Exito -> Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Publicación eliminada con éxito", Modifier.padding(16.dp))
                            }
                            is UiState.Error -> Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(eb.mensaje, Modifier.padding(16.dp))
                            }
                            else -> Unit
                        }
                    }
                    item {
                        Text(
                            "Comentarios (${comentarios.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(comentarios, key = { it.id }) { c ->
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(12.dp)) {
                                Text(
                                    c.email,
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.labelLarge
                                )
                                Text(c.body, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
