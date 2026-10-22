package com.gamarra.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamarra.huellitasvet.ui.components.VetButton
import com.gamarra.huellitasvet.viewmodel.SesionViewModel

@Composable
fun PerfilScreen(sesion: SesionViewModel, onCerrarSesion: () -> Unit, modifier: Modifier = Modifier) {
    val usuario by sesion.usuarioActual.collectAsState()
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = modifier.fillMaxSize().background(colores.background).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            Modifier.padding(top = 24.dp).size(88.dp).background(colores.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Person, contentDescription = null, tint = colores.primary, modifier = Modifier.size(48.dp))
        }
        Text(usuario?.nombre ?: "", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(usuario?.correo ?: "", color = colores.onSurfaceVariant)
        // TODO Paso 13: contadores, modo oscuro, recordatorios y cambiar de perfil
        VetButton(texto = "Cerrar sesión", onClick = { sesion.cerrarSesion(onCerrarSesion) }, contorno = true)
    }
}
