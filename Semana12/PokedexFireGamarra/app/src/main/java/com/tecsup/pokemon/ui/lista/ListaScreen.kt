package com.tecsup.pokemon.ui.lista

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.ui.components.TarjetaPokemon
import com.tecsup.pokemon.ui.theme.PokedexFireGamarraTheme

// Lista de Pokémon de tipo fuego. Por ahora con datos de prueba.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    items: List<Item>,
    onItemClick: (Int) -> Unit,
    onFavorito: (Int) -> Unit
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
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
}

// Datos de prueba mientras no se conecta la API.
private const val ARTE = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"

val pokemonDePrueba = listOf(
    Item(4, "charmander", "#004", "${ARTE}4.png"),
    Item(5, "charmeleon", "#005", "${ARTE}5.png"),
    Item(6, "charizard", "#006", "${ARTE}6.png", esFavorito = true),
    Item(37, "vulpix", "#037", "${ARTE}37.png"),
    Item(38, "ninetales", "#038", "${ARTE}38.png"),
    Item(58, "growlithe", "#058", "${ARTE}58.png")
)

@Preview(showBackground = true)
@Composable
private fun ListaScreenPreview() {
    PokedexFireGamarraTheme {
        ListaScreen(items = pokemonDePrueba, onItemClick = {}, onFavorito = {})
    }
}
