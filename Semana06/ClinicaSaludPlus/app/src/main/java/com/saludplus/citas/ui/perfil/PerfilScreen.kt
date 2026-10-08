package com.saludplus.citas.ui.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.repository.Repositorio
import com.saludplus.citas.ui.components.BarraNavegacion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(onCerrarSesion: () -> Unit, onNavegarBarra: (String) -> Unit) {
    val usuario = Repositorio.usuarioActual

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = { BarraNavegacion(Rutas.PERFIL, onNavegarBarra) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                Icons.Filled.AccountCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(96.dp)
            )
            Text(usuario?.nombre ?: "-", style = MaterialTheme.typography.titleLarge)
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Correo: ${usuario?.correo ?: "-"}", style = MaterialTheme.typography.bodyLarge)
                    Text("Teléfono: ${usuario?.telefono ?: "-"}", style = MaterialTheme.typography.bodyLarge)
                    Text("Citas agendadas: ${Repositorio.citasDelUsuario().size}", style = MaterialTheme.typography.bodyLarge)
                }
            }
            Button(
                onClick = {
                    Repositorio.cerrarSesion()
                    onCerrarSesion()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}
