package com.tecsup.pokemon.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    onItemClick: (Int) -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    ListaContenido(
        estado = estado,
        onItemClick = onItemClick,
        onFavorito = { viewModel.alternarFavorito(it) },
        onReintentar = viewModel::refrescar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaContenido(
    estado: UiState<List<Item>>,
    onItemClick: (Int) -> Unit,
    onFavorito: (Int) -> Unit,
    onReintentar: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pokédex de Fuego") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
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
                    GrillaPokemon(estado.datos, onItemClick, onFavorito, modifier)
                }
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
        ListaContenido(UiState.Exito(pokemonDePrueba), {}, {}, {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ListaErrorPreview() {
    PokedexFireGamarraTheme {
        ListaContenido(UiState.Error("No hay conexión a internet"), {}, {}, {})
    }
}
