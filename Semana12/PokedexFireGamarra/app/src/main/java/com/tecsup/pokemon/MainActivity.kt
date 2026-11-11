package com.tecsup.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.pokemon.ui.lista.ListaScreen
import com.tecsup.pokemon.ui.lista.ListaViewModel
import com.tecsup.pokemon.ui.theme.PokedexFireGamarraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = AppContainer.obtener(applicationContext)
        setContent {
            PokedexFireGamarraTheme {
                val listaViewModel: ListaViewModel = viewModel(factory = ListaViewModel.factory(container))
                ListaScreen(viewModel = listaViewModel, onItemClick = { })
            }
        }
    }
}
