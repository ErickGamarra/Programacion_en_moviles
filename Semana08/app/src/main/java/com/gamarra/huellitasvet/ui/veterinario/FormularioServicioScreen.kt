package com.gamarra.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamarra.huellitasvet.data.local.entity.Servicio
import com.gamarra.huellitasvet.ui.components.VetButton
import com.gamarra.huellitasvet.ui.components.VetTextField
import com.gamarra.huellitasvet.ui.components.iconoServicio
import com.gamarra.huellitasvet.ui.components.iconosServicio
import com.gamarra.huellitasvet.viewmodel.ServiciosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioServicioScreen(vm: ServiciosViewModel, servicioId: Int?, onVolver: () -> Unit) {
    var original by remember { mutableStateOf<Servicio?>(null) }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var duracion by remember { mutableIntStateOf(30) }
    var precio by remember { mutableStateOf("") }
    var icono by remember { mutableStateOf(iconosServicio.first()) }
    var activo by remember { mutableStateOf(true) }
    val errores by vm.errores.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val colores = MaterialTheme.colorScheme

    DisposableEffect(Unit) { onDispose { vm.limpiar() } }
    LaunchedEffect(servicioId) {
        val s = servicioId?.let { vm.cargar(it) } ?: return@LaunchedEffect
        original = s
        nombre = s.nombre
        descripcion = s.descripcion
        duracion = s.duracionMin
        precio = s.precio.toString()
        icono = s.icono
        activo = s.activo
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (servicioId == null) "Nuevo servicio" else "Editar servicio") },
                navigationIcon = {
                    IconButton(onClick = onVolver) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colores.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colores.background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VetTextField(nombre, { nombre = it }, "Nombre", error = errores.nombre)

            Text("Duración", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                vm.duraciones.forEach { d ->
                    Chip("$d min", duracion == d) { duracion = d }
                }
            }
            errores.duracion?.let { Text(it, color = colores.error, fontSize = 13.sp) }

            VetTextField(
                precio, { precio = it.replace(',', '.') }, "Precio (S/)",
                error = errores.precio, tipoTeclado = KeyboardType.Decimal
            )
            VetTextField(descripcion, { descripcion = it }, "Descripción", unaLinea = false)

            Text("Ícono", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                iconosServicio.forEach { i ->
                    FilterChip(
                        selected = icono == i,
                        onClick = { icono = i },
                        label = { Icon(iconoServicio(i), contentDescription = i) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = colores.primary,
                            selectedLabelColor = colores.onPrimary
                        )
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("Servicio activo", fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                Switch(checked = activo, onCheckedChange = { activo = it })
            }

            VetButton(
                texto = "Guardar",
                onClick = {
                    val servicio = Servicio(
                        id = original?.id ?: 0,
                        nombre = nombre,
                        descripcion = descripcion,
                        duracionMin = duracion,
                        precio = precio.toDoubleOrNull() ?: 0.0,
                        activo = activo,
                        icono = icono
                    )
                    vm.guardar(servicio, onVolver)
                }
            )
            original?.let { s ->
                VetButton(texto = "Eliminar servicio", onClick = { vm.eliminar(s, onVolver) }, contorno = true)
            }
            mensaje?.let { Text(it, color = colores.error, fontSize = 14.sp) }
        }
    }
}

@Composable
private fun Chip(texto: String, elegido: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = elegido,
        onClick = onClick,
        label = { Text(texto) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
