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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.foundation.shape.RoundedCornerShape
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
    onAbrirFavoritos: () -> Unit,
    onCerrarSesion: () -> Unit,
    esAdmin: Boolean = false,
    onAbrirAdmin: () -> Unit = {}
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()
    val busqueda by viewModel.busqueda.collectAsStateWithLifecycle()
    val refrescando by viewModel.refrescando.collectAsStateWithLifecycle()
    ListaContenido(
        estado = estado,
        aviso = aviso,
        busqueda = busqueda,
        onBuscar = viewModel::buscar,
        refrescando = refrescando,
        onDeslizar = viewModel::refrescarDeslizando,
        onItemClick = onItemClick,
        onFavorito = { viewModel.alternarFavorito(it) },
        onReintentar = viewModel::refrescar,
        onAbrirFavoritos = onAbrirFavoritos,
        onCerrarSesion = onCerrarSesion,
        esAdmin = esAdmin,
        onAbrirAdmin = onAbrirAdmin
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaContenido(
    estado: UiState<List<Item>>,
    aviso: String?,
    busqueda: String = "",
    onBuscar: (String) -> Unit = {},
    refrescando: Boolean = false,
    onDeslizar: () -> Unit = {},
    onItemClick: (Int) -> Unit,
    onFavorito: (Int) -> Unit,
    onReintentar: () -> Unit,
    onAbrirFavoritos: () -> Unit = {},
    onCerrarSesion: () -> Unit = {},
    esAdmin: Boolean = false,
    onAbrirAdmin: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pokédex de Fuego") },
                actions = {
                    // Solo el admin ve el acceso al panel
                    if (esAdmin) {
                        IconButton(onClick = onAbrirAdmin) {
                            Icon(Icons.Filled.AdminPanelSettings, contentDescription = "Panel Admin")
                        }
                    }
                    IconButton(onClick = onAbrirFavoritos) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Favoritos")
                    }
                    IconButton(onClick = onCerrarSesion) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
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
                if (estado.datos.isEmpty() && busqueda.isBlank()) {
                    VacioView(
                        mensaje = "No hay Pokémon guardados.",
                        textoBoton = "Volver a cargar",
                        onBoton = onReintentar,
                        modifier = modifier
                    )
                } else {
                    Column(modifier) {
                        if (aviso != null) AvisoSinConexion(aviso, onReintentar)
                        Buscador(busqueda, onBuscar)
                        // Deslizar hacia abajo para sincronizar
                        PullToRefreshBox(
                            isRefreshing = refrescando,
                            onRefresh = onDeslizar,
                            modifier = Modifier.weight(1f)
                        ) {
                            if (estado.datos.isEmpty()) {
                                VacioView(mensaje = "Ningún Pokémon coincide con \"$busqueda\".")
                            } else {
                                GrillaPokemon(estado.datos, onItemClick, onFavorito, Modifier.fillMaxSize())
                            }
                        }
                    }
                }
            }
        }
    }
}

// Campo de búsqueda por nombre o número
@Composable
private fun Buscador(texto: String, onBuscar: (String) -> Unit) {
    OutlinedTextField(
        value = texto,
        onValueChange = onBuscar,
        placeholder = { Text("Buscar por nombre o número") },
        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
        trailingIcon = {
            if (texto.isNotEmpty()) {
                IconButton(onClick = { onBuscar("") }) {
                    Icon(Icons.Outlined.Close, contentDescription = "Borrar búsqueda")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(50),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 12.dp)
    )
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
        ListaContenido(estado = UiState.Exito(pokemonDePrueba), aviso = null, onItemClick = {}, onFavorito = {}, onReintentar = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ListaErrorPreview() {
    PokedexFireGamarraTheme {
        ListaContenido(estado = UiState.Error("No hay conexión a internet"), aviso = null, onItemClick = {}, onFavorito = {}, onReintentar = {})
    }
}
