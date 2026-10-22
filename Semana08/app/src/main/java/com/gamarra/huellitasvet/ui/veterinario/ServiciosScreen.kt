package com.gamarra.huellitasvet.ui.veterinario

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamarra.huellitasvet.data.local.entity.Servicio
import com.gamarra.huellitasvet.ui.components.iconoServicio
import com.gamarra.huellitasvet.ui.components.precioTexto
import com.gamarra.huellitasvet.viewmodel.ServiciosViewModel

@Composable
fun ServiciosScreen(vm: ServiciosViewModel, onEditar: (Int) -> Unit, onNuevo: () -> Unit, modifier: Modifier = Modifier) {
    val servicios by vm.servicios.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val colores = MaterialTheme.colorScheme

    Box(modifier.fillMaxSize().background(colores.background)) {
        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Servicios", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text("Los servicios inactivos no aparecen al pedir cita", color = colores.onSurfaceVariant, fontSize = 14.sp)
            }
            mensaje?.let { item { Text(it, color = colores.error, fontSize = 14.sp) } }
            items(servicios, key = { it.id }) { s ->
                ServicioItem(s, onToggle = { vm.alternar(s) }, onEditar = { onEditar(s.id) }, onEliminar = { vm.eliminar(s) })
            }
        }
        FloatingActionButton(
            onClick = onNuevo,
            containerColor = colores.primary,
            contentColor = colores.onPrimary,
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Nuevo servicio")
        }
    }
}

@Composable
private fun ServicioItem(s: Servicio, onToggle: () -> Unit, onEditar: () -> Unit, onEliminar: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = colores.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onEditar)
    ) {
        Row(Modifier.padding(start = 14.dp, top = 10.dp, bottom = 10.dp, end = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(44.dp).background(colores.primaryContainer, CircleShape).alpha(if (s.activo) 1f else 0.5f),
                contentAlignment = Alignment.Center
            ) {
                Icon(iconoServicio(s.icono), contentDescription = null, tint = colores.primary)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f).alpha(if (s.activo) 1f else 0.5f)) {
                Text(s.nombre, fontWeight = FontWeight.SemiBold)
                Text("${s.duracionMin} min · ${precioTexto(s.precio)}", color = colores.onSurfaceVariant, fontSize = 13.sp)
                Text(if (s.activo) "Activo" else "Inactivo", color = if (s.activo) colores.primary else colores.onSurfaceVariant, fontSize = 12.sp)
            }
            Switch(checked = s.activo, onCheckedChange = { onToggle() })
            IconButton(onClick = onEliminar) {
                Icon(Icons.Outlined.Delete, contentDescription = "Eliminar", tint = colores.error)
            }
        }
    }
}
