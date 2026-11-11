package com.tecsup.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tecsup.pokemon.ui.lista.ListaScreen
import com.tecsup.pokemon.ui.lista.pokemonDePrueba
import com.tecsup.pokemon.ui.theme.PokedexFireGamarraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokedexFireGamarraTheme {
                // Lista con datos de prueba (todavía sin API)
                var items by remember { mutableStateOf(pokemonDePrueba) }
                ListaScreen(
                    items = items,
                    onItemClick = { },
                    onFavorito = { id ->
                        items = items.map { if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it }
                    }
                )
            }
        }
    }
}
