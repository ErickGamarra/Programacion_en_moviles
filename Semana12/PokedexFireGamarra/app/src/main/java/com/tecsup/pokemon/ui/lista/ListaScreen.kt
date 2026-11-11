package com.tecsup.pokemon.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ErrorView
import com.tecsup.pokemon.ui.components.TarjetaPokemon
import com.tecsup.pokemon.ui.components.VacioView
import com.tecsup.pokemon.ui.theme.PokedexFireGamarraTheme

// La pantalla solo observa el estado y llama funciones del ViewModel.
@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    onItemClick: (Int) -> Unit,
    onAbrirFavoritos: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()
    ListaContenido(
        estado = estado,
        aviso = aviso,
        onItemClick = onItemClick,
        onFavorito = { viewModel.alternarFavorito(it) },
        onReintentar = viewModel::refrescar,
        onAbrirFavoritos = onAbrirFavoritos
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaContenido(
    estado: UiState<List<Item>>,
    aviso: String?,
    onItemClick: (Int) -> Unit,
    onFavorito: (Int) -> Unit,
    onReintentar: () -> Unit,
    onAbrirFavoritos: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pokédex de Fuego") },
                actions = {
                    IconButton(onClick = onAbrirFavoritos) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Favoritos")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        when (estado) {
            is UiState.Cargando -> Cargando(modifier = modifier)
            is UiState.Error -> ErrorView(estado.mensaje, onReintentar, modifier)
            is UiState.Exito -> {
                if (estado.datos.isEmpty()) {
                    VacioView(
                        mensaje = "No hay Pokémon guardados.",
                        textoBoton = "Volver a cargar",
                        onBoton = onReintentar,
                        modifier = modifier
                    )
                } else {
                    Column(modifier) {
                        if (aviso != null) AvisoSinConexion(aviso, onReintentar)
                        GrillaPokemon(estado.datos, onItemClick, onFavorito, Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// Franja que avisa que se muestran datos guardados
@Composable
private fun AvisoSinConexion(texto: String, onReintentar: () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
        ) {
            Icon(Icons.Outlined.CloudOff, contentDescription = null, tint = MaterialTheme.colorScheme.onTertiary)
            Spacer(Modifier.width(12.dp))
            Text(
                texto,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onReintentar) {
                Text("Reintentar", color = MaterialTheme.colorScheme.onTertiary)
            }
        }
    }
}

@Composable
fun GrillaPokemon(
    items: List<Item>,
    onItemClick: (Int) -> Unit,
    onFavorito: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        items(items, key = { it.id }) { item ->
            TarjetaPokemon(
                item = item,
                onClick = { onItemClick(item.id) },
                onFavorito = { onFavorito(item.id) }
            )
        }
    }
}

// Datos de prueba para la vista previa.
private const val ARTE = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"

val pokemonDePrueba = listOf(
    Item(4, "charmander", "#004", "${ARTE}4.png"),
    Item(5, "charmeleon", "#005", "${ARTE}5.png"),
    Item(6, "charizard", "#006", "${ARTE}6.png", esFavorito = true),
    Item(37, "vulpix", "#037", "${ARTE}37.png")
)

@Preview(showBackground = true)
@Composable
private fun ListaPreview() {
    PokedexFireGamarraTheme {
        ListaContenido(UiState.Exito(pokemonDePrueba), "Sin conexión: mostrando datos guardados", {}, {}, {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ListaErrorPreview() {
    PokedexFireGamarraTheme {
        ListaContenido(UiState.Error("No hay conexión a internet"), null, {}, {}, {})
    }
}
