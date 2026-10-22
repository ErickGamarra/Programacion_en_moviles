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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
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
import com.gamarra.huellitasvet.data.local.entity.Mascota
import com.gamarra.huellitasvet.model.Especie
import com.gamarra.huellitasvet.ui.components.MascotaAvatar
import com.gamarra.huellitasvet.viewmodel.InicioViewModel
import java.time.LocalDate

@Composable
fun MascotasScreen(
    vm: InicioViewModel,
    onVerMascota: (Int) -> Unit,
    onAgregar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mascotas by vm.mascotas.collectAsState()

    Box(modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Mis mascotas", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text(
                    "${mascotas.size} registradas",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            if (mascotas.isEmpty()) {
                item { Text("Aún no registras mascotas", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
            items(mascotas, key = { it.id }) { TarjetaMascota(it) { onVerMascota(it.id) } }
        }
        FloatingActionButton(
            onClick = onAgregar,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar mascota")
        }
    }
}

@Composable
private fun TarjetaMascota(mascota: Mascota, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            MascotaAvatar(mascota.especie, tamano = 60.dp)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(mascota.nombre, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    "${nombreEspecie(mascota.especie)} · ${mascota.raza}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(edad(mascota.anioNacimiento), color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun nombreEspecie(especie: String) = when (especie) {
    Especie.PERRO.name -> "Perro"
    Especie.GATO.name -> "Gato"
    else -> "Otro"
}

private fun edad(anioNacimiento: Int): String {
    val anios = LocalDate.now().year - anioNacimiento
    return when {
        anios < 1 -> "Menos de 1 año"
        anios == 1 -> "1 año"
        else -> "$anios años"
    }
}
