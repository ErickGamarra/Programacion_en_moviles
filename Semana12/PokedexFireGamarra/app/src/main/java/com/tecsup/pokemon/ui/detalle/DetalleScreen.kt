package com.tecsup.pokemon.ui.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tecsup.pokemon.domain.model.DetallePokemon
import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ErrorView
import com.tecsup.pokemon.ui.components.nombreBonito

@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    onVolver: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    DetalleContenido(
        estado = estado,
        onVolver = onVolver,
        onFavorito = viewModel::alternarFavorito,
        onReintentar = viewModel::cargar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleContenido(
    estado: UiState<Item>,
    onVolver: () -> Unit,
    onFavorito: () -> Unit,
    onReintentar: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (estado is UiState.Exito) nombreBonito(estado.datos.titulo) else "Detalle")
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (estado is UiState.Exito) {
                        IconButton(onClick = onFavorito) {
                            Icon(
                                if (estado.datos.esFavorito) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorito"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        when (estado) {
            is UiState.Cargando -> Cargando("Cargando detalle...", modifier)
            is UiState.Error -> ErrorView(estado.mensaje, onReintentar, modifier)
            is UiState.Exito -> FichaPokemon(estado.datos, modifier)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FichaPokemon(item: Item, modifier: Modifier = Modifier) {
    val detalle = item.detalle
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        // Cabecera con la imagen oficial
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.titulo,
                modifier = Modifier.size(240.dp)
            )
            Text(
                item.subtitulo,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.85f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            )
        }

        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                nombreBonito(item.titulo),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            if (detalle != null) {
                Spacer(Modifier.height(12.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    detalle.tipos.forEach { ChipTipo(it) }
                }
                Spacer(Modifier.height(20.dp))
                DatosBasicos(detalle)
                Spacer(Modifier.height(20.dp))
                Text("Habilidades", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    detalle.habilidades.forEach {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                nombreBonito(it),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
                Text("Estadísticas base", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                detalle.estadisticas.forEach { BarraEstadistica(nombreEstadistica(it.nombre), it.valor) }
            }
        }
    }
}

@Composable
private fun DatosBasicos(detalle: DetallePokemon) {
    Card(shape = RoundedCornerShape(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Dato("Altura", "%.1f m".format(detalle.alturaMetros))
            Dato("Peso", "%.1f kg".format(detalle.pesoKg))
            Dato("Exp. base", detalle.experienciaBase?.toString() ?: "-")
        }
    }
}

@Composable
private fun Dato(titulo: String, valor: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(valor, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(titulo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun BarraEstadistica(nombre: String, valor: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 5.dp)
    ) {
        Text(nombre, modifier = Modifier.width(96.dp), style = MaterialTheme.typography.bodyMedium)
        Text(
            valor.toString(),
            modifier = Modifier.width(40.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        LinearProgressIndicator(
            progress = { (valor / 200f).coerceIn(0f, 1f) },
            modifier = Modifier
                .weight(1f)
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun ChipTipo(tipo: String) {
    Surface(shape = RoundedCornerShape(50), color = colorTipo(tipo)) {
        Text(
            nombreTipo(tipo),
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )
    }
}

// Nombres en español de las estadísticas y los tipos
private fun nombreEstadistica(nombre: String) = when (nombre) {
    "hp" -> "PS"
    "attack" -> "Ataque"
    "defense" -> "Defensa"
    "special-attack" -> "At. Esp."
    "special-defense" -> "Def. Esp."
    "speed" -> "Velocidad"
    else -> nombreBonito(nombre)
}

private fun nombreTipo(tipo: String) = when (tipo) {
    "fire" -> "Fuego"; "water" -> "Agua"; "grass" -> "Planta"; "flying" -> "Volador"
    "ground" -> "Tierra"; "rock" -> "Roca"; "normal" -> "Normal"; "psychic" -> "Psíquico"
    "ghost" -> "Fantasma"; "dragon" -> "Dragón"; "fighting" -> "Lucha"; "bug" -> "Bicho"
    "steel" -> "Acero"; "dark" -> "Siniestro"; "poison" -> "Veneno"; "electric" -> "Eléctrico"
    "ice" -> "Hielo"; "fairy" -> "Hada"
    else -> nombreBonito(tipo)
}

private fun colorTipo(tipo: String) = when (tipo) {
    "fire" -> Color(0xFFE4572E); "water" -> Color(0xFF4A90E2); "grass" -> Color(0xFF5BAA4A)
    "flying" -> Color(0xFF8E9BE0); "ground" -> Color(0xFFC9A55B); "rock" -> Color(0xFFB09A50)
    "psychic" -> Color(0xFFE85C8A); "ghost" -> Color(0xFF6B5B95); "dragon" -> Color(0xFF6A4FD8)
    "fighting" -> Color(0xFFC03028); "bug" -> Color(0xFF9AAA2A); "steel" -> Color(0xFF8E9AAA)
    "dark" -> Color(0xFF5A4A40); "poison" -> Color(0xFF9B4FA0); "electric" -> Color(0xFFE5B800)
    "ice" -> Color(0xFF6CC5D0); "fairy" -> Color(0xFFE38FB5)
    else -> Color(0xFF8A8A7A)
}
