package com.tecsup.pokemon.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ErrorView

// Panel solo para el administrador
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: AdminViewModel,
    correo: String,
    onVolver: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()
    val trabajando by viewModel.trabajando.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    var confirmarLimpieza by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbar.showSnackbar(it)
            viewModel.mensajeMostrado()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Admin") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        when (val e = estado) {
            is UiState.Cargando -> Cargando("Cargando totales...", modifier)
            is UiState.Error -> ErrorView(e.mensaje, onReintentar = viewModel::sincronizar, modifier = modifier)
            is UiState.Exito -> Column(
                modifier = modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Hola, administrador", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(correo, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaTotal("Guardados", e.datos.guardados, Icons.Outlined.Storage, Modifier.weight(1f))
                    TarjetaTotal("Favoritos", e.datos.favoritos, Icons.Filled.Favorite, Modifier.weight(1f))
                }

                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = viewModel::sincronizar,
                    enabled = !trabajando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    if (trabajando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(Icons.Outlined.Sync, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Sincronizar")
                    }
                }
                OutlinedButton(
                    onClick = { confirmarLimpieza = true },
                    enabled = !trabajando,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Icon(Icons.Outlined.DeleteSweep, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Limpiar caché")
                }
            }
        }
    }

    if (confirmarLimpieza) {
        AlertDialog(
            onDismissRequest = { confirmarLimpieza = false },
            title = { Text("¿Limpiar caché?") },
            text = { Text("Se borrarán los Pokémon guardados en el celular, incluidos los favoritos.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarLimpieza = false
                    viewModel.limpiarCache()
                }) { Text("Limpiar") }
            },
            dismissButton = {
                TextButton(onClick = { confirmarLimpieza = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun TarjetaTotal(titulo: String, valor: Int, icono: ImageVector, modifier: Modifier = Modifier) {
    Card(shape = RoundedCornerShape(20.dp), modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(10.dp))
            Text(valor.toString(), style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
            Text(titulo, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
