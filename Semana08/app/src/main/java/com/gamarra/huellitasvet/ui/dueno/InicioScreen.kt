package com.gamarra.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamarra.huellitasvet.data.local.entity.Mascota
import com.gamarra.huellitasvet.data.local.relation.CitaConDetalle
import com.gamarra.huellitasvet.ui.components.MascotaAvatar
import com.gamarra.huellitasvet.ui.components.VetButton
import com.gamarra.huellitasvet.viewmodel.InicioViewModel
import com.gamarra.huellitasvet.viewmodel.SesionViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun InicioScreen(
    sesion: SesionViewModel,
    vm: InicioViewModel,
    onVerMascota: (Int) -> Unit,
    onAgregar: () -> Unit,
    onPedirCita: () -> Unit,
    modifier: Modifier = Modifier
) {
    val usuario by sesion.usuarioActual.collectAsState()
    val mascotas by vm.mascotas.collectAsState()
    val proxima by vm.proximaCita.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Text("Hola, ${usuario?.nombre?.substringBefore(" ") ?: ""}", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("¿Cómo están tus engreídos hoy?", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item { TarjetaProximaCita(proxima) }
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Mis mascotas", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                TextButton(onClick = onAgregar) {
                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Agregar")
                }
            }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(mascotas, key = { it.id }) { ItemMascota(it) { onVerMascota(it.id) } }
                item { ItemAgregar(onAgregar) }
            }
        }
        item { VetButton(texto = "Pedir una cita", onClick = onPedirCita) }
    }
}

@Composable
private fun TarjetaProximaCita(proxima: CitaConDetalle?) {
    val colores = MaterialTheme.colorScheme
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = if (proxima != null) colores.primary else colores.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        if (proxima == null) {
            Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = colores.primary)
                Spacer(Modifier.width(12.dp))
                Text("Aún no tienes citas", color = colores.onSurfaceVariant)
            }
            return@Card
        }
        Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Próxima cita", color = colores.onPrimary.copy(alpha = 0.8f), fontSize = 13.sp)
                Text(proxima.servicio.nombre, color = colores.onPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(
                    "${proxima.mascota.mascota.nombre} · ${fechaCorta(proxima.cita.fecha)} · ${proxima.cita.hora}",
                    color = colores.onPrimary
                )
                Text(
                    proxima.cita.estado.lowercase().replaceFirstChar { it.uppercase() },
                    color = colores.onPrimary.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Box(Modifier.background(colores.surface, CircleShape).padding(4.dp)) {
                MascotaAvatar(proxima.mascota.mascota.especie, tamano = 52.dp)
            }
        }
    }
}

@Composable
private fun ItemMascota(mascota: Mascota, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(88.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
    ) {
        MascotaAvatar(mascota.especie, tamano = 68.dp)
        Text(mascota.nombre, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 6.dp))
    }
}

@Composable
private fun ItemAgregar(onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(88.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
    ) {
        Box(
            Modifier.size(68.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        Text("Agregar", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 6.dp))
    }
}

private fun fechaCorta(fecha: String): String {
    val dia = LocalDate.parse(fecha)
    return when (dia) {
        LocalDate.now() -> "Hoy"
        LocalDate.now().plusDays(1) -> "Mañana"
        else -> dia.format(DateTimeFormatter.ofPattern("EEE d MMM", Locale.forLanguageTag("es-PE")))
    }
}
