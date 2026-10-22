package com.gamarra.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.EventAvailable
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
fun AjustesScreen(sesion: SesionViewModel, onCambiarPerfil: () -> Unit, onCerrarSesion: () -> Unit, modifier: Modifier = Modifier) {
    val usuario by sesion.usuarioActual.collectAsState()
    val oscuro by sesion.modoOscuro.collectAsState()
    val notificaciones by sesion.notificaciones.collectAsState()
    val aceptarCitas by sesion.aceptarCitas.collectAsState()
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colores.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Ajustes", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(64.dp).background(colores.primaryContainer, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.MedicalServices, contentDescription = null, tint = colores.primary, modifier = Modifier.size(32.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column {
                Text(usuario?.nombre ?: "", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(usuario?.correo ?: "", color = colores.onSurfaceVariant)
                Text("Veterinario", color = colores.primary, fontSize = 13.sp)
            }
        }

        VetButton(texto = "Cambiar de perfil", onClick = { sesion.cambiarPerfil { onCambiarPerfil() } }, contorno = true)

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = colores.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                FilaAjuste(Icons.Outlined.DarkMode, "Modo oscuro", "Se recuerda al cerrar la app", oscuro, sesion::cambiarModoOscuro)
                HorizontalDivider()
                FilaAjuste(Icons.Outlined.NotificationsActive, "Notificaciones", "Avisos de citas nuevas", notificaciones, sesion::cambiarNotificaciones)
                HorizontalDivider()
                FilaAjuste(
                    Icons.Outlined.EventAvailable,
                    "Aceptar citas",
                    if (aceptarCitas) "Los dueños pueden pedir citas" else "La clínica no recibe citas",
                    aceptarCitas,
                    sesion::cambiarAceptarCitas
                )
            }
        }

        VetButton(texto = "Cerrar sesión", onClick = { sesion.cerrarSesion(onCerrarSesion) })
    }
}
