package com.gamarra.huellitasvet.ui.dueno

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamarra.huellitasvet.data.local.entity.Mascota
import com.gamarra.huellitasvet.model.Especie
import com.gamarra.huellitasvet.model.Sexo
import com.gamarra.huellitasvet.ui.components.MascotaAvatar
import com.gamarra.huellitasvet.ui.components.VetButton
import com.gamarra.huellitasvet.ui.components.VetTextField
import com.gamarra.huellitasvet.ui.components.nombreEspecie
import com.gamarra.huellitasvet.ui.components.nombreSexo
import com.gamarra.huellitasvet.viewmodel.MascotaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioMascotaScreen(
    vm: MascotaViewModel,
    duenoId: Int,
    mascotaId: Int?,
    onVolver: () -> Unit,
    onEliminada: () -> Unit
) {
    var original by remember { mutableStateOf<Mascota?>(null) }
    var nombre by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }
    var confirmarEliminar by remember { mutableStateOf(false) }
    val errores by vm.errores.collectAsState()
    val mensaje by vm.mensaje.collectAsState()

    DisposableEffect(Unit) { onDispose { vm.limpiar() } }
    LaunchedEffect(mascotaId) {
        val m = mascotaId?.let { vm.cargar(it) } ?: return@LaunchedEffect
        original = m
        nombre = m.nombre
        raza = m.raza
        anio = m.anioNacimiento.toString()
        peso = m.peso.toString()
        especie = m.especie
        sexo = m.sexo
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (mascotaId == null) "Nueva mascota" else "Editar mascota") },
                navigationIcon = {
                    IconButton(onClick = onVolver) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MascotaAvatar(especie, tamano = 96.dp, modifier = Modifier.align(Alignment.CenterHorizontally))

            VetTextField(nombre, { nombre = it }, "Nombre", error = errores.nombre)

            Etiqueta("Especie")
            Chips(Especie.entries.map { it.name }, especie, ::nombreEspecie) { especie = it }
            errores.especie?.let { TextoError(it) }

            VetTextField(raza, { raza = it }, "Raza")

            Etiqueta("Sexo")
            Chips(Sexo.entries.map { it.name }, sexo, ::nombreSexo) { sexo = it }
            errores.sexo?.let { TextoError(it) }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                VetTextField(
                    anio, { anio = it.filter(Char::isDigit).take(4) }, "Año de nacimiento",
                    modifier = Modifier.weight(1f), error = errores.anio, tipoTeclado = KeyboardType.Number
                )
                VetTextField(
                    peso, { peso = it.replace(',', '.') }, "Peso (kg)",
                    modifier = Modifier.weight(1f), error = errores.peso, tipoTeclado = KeyboardType.Decimal
                )
            }

            VetButton(
                texto = "Guardar",
                onClick = {
                    val mascota = Mascota(
                        id = original?.id ?: 0,
                        duenoId = original?.duenoId ?: duenoId,
                        nombre = nombre,
                        especie = especie,
                        raza = raza,
                        anioNacimiento = anio.toIntOrNull() ?: 0,
                        peso = peso.toDoubleOrNull() ?: 0.0,
                        sexo = sexo
                    )
                    vm.guardar(mascota, onVolver)
                }
            )
            original?.let {
                VetButton(texto = "Eliminar mascota", onClick = { confirmarEliminar = true }, contorno = true)
            }
            mensaje?.let { TextoError(it) }
        }
    }

    if (confirmarEliminar) {
        AlertDialog(
            onDismissRequest = { confirmarEliminar = false },
            title = { Text("Eliminar mascota") },
            text = { Text("¿Seguro que quieres eliminar a ${original?.nombre}?") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarEliminar = false
                    original?.let { vm.eliminar(it, onEliminada) }
                }) { Text("Eliminar") }
            },
            dismissButton = { TextButton(onClick = { confirmarEliminar = false }) { Text("Cancelar") } }
        )
    }
}

@Composable
private fun Etiqueta(texto: String) {
    Text(texto, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
}

@Composable
private fun TextoError(texto: String) {
    Text(texto, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
}

@Composable
private fun Chips(opciones: List<String>, elegido: String, texto: (String) -> String, onElegir: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        opciones.forEach { opcion ->
            FilterChip(
                selected = elegido == opcion,
                onClick = { onElegir(opcion) },
                label = { Text(texto(opcion)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}
