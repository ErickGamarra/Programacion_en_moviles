package com.gamarra.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.gamarra.huellitasvet.ui.components.FilaAjuste
import com.gamarra.huellitasvet.ui.components.VetButton
import com.gamarra.huellitasvet.viewmodel.SesionViewModel

@Composable
fun PerfilScreen(
    sesion: SesionViewModel,
    mascotas: Int,
    proximas: Int,
    atenciones: Int,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val usuario by sesion.usuarioActual.collectAsState()
    val oscuro by sesion.modoOscuro.collectAsState()
    val recordatorios by sesion.notificaciones.collectAsState()
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colores.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            Modifier.padding(top = 12.dp).size(88.dp).background(colores.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Person, contentDescription = null, tint = colores.primary, modifier = Modifier.size(48.dp))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(usuario?.nombre ?: "", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(usuario?.correo ?: "", color = colores.onSurfaceVariant)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            Contador("Mascotas", mascotas, Modifier.weight(1f))
            Contador("Próximas", proximas, Modifier.weight(1f))
            Contador("Atenciones", atenciones, Modifier.weight(1f))
        }

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = colores.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                FilaAjuste(Icons.Outlined.DarkMode, "Modo oscuro", "Se recuerda al cerrar la app", oscuro, sesion::cambiarModoOscuro)
                HorizontalDivider()
                FilaAjuste(Icons.Outlined.NotificationsActive, "Recordatorios", "Avisos antes de cada cita", recordatorios, sesion::cambiarNotificaciones)
            }
        }

        VetButton(texto = "Cambiar de perfil", onClick = { sesion.cambiarPerfil { onCambiarPerfil() } })
        VetButton(texto = "Cerrar sesión", onClick = { sesion.cerrarSesion(onCerrarSesion) }, contorno = true)
    }
}

@Composable
private fun Contador(titulo: String, valor: Int, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier
    ) {
        Column(Modifier.fillMaxWidth().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(valor.toString(), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(titulo, fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}
