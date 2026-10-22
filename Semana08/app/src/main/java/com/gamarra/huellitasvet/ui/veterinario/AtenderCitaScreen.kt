package com.gamarra.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.gamarra.huellitasvet.ui.components.EstadoCitaChip
import com.gamarra.huellitasvet.ui.components.MascotaAvatar
import com.gamarra.huellitasvet.ui.components.VetButton
import com.gamarra.huellitasvet.ui.components.VetTextField
import com.gamarra.huellitasvet.ui.components.fechaLarga
import com.gamarra.huellitasvet.ui.components.pesoTexto
import com.gamarra.huellitasvet.viewmodel.AtenderCitaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtenderCitaScreen(vm: AtenderCitaViewModel, citaId: Int, onVolver: () -> Unit) {
    LaunchedEffect(citaId) { vm.cargar(citaId) }
    val cargada by vm.cita.collectAsState()
    val detalle = cargada?.takeIf { it.cita.id == citaId }
    val errores by vm.errores.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    var diagnostico by remember { mutableStateOf("") }
    var tratamiento by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var confirmarCancelar by remember { mutableStateOf(false) }
    val colores = MaterialTheme.colorScheme

    LaunchedEffect(detalle?.cita?.id) {
        detalle?.let { if (peso.isBlank()) peso = it.mascota.mascota.peso.toString() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Atender cita") },
                navigationIcon = {
                    IconButton(onClick = onVolver) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colores.background)
            )
        }
    ) { padding ->
        val c = detalle ?: return@Scaffold
        val mascota = c.mascota.mascota
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colores.background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colores.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    MascotaAvatar(mascota.especie, tamano = 64.dp)
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(mascota.nombre, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = colores.onPrimaryContainer)
                        Text("Dueño: ${c.mascota.dueno.nombre}", color = colores.onPrimaryContainer)
                        Text(
                            "${c.servicio.nombre} · ${fechaLarga(c.cita.fecha)} · ${c.cita.hora}",
                            color = colores.onPrimaryContainer.copy(alpha = 0.8f),
                            fontSize = 13.sp
                        )
                    }
                    EstadoCitaChip(c.cita.estado)
                }
            }

            if (c.cita.nota.isNotBlank()) {
                Text("Nota del dueño: ${c.cita.nota}", color = colores.onSurfaceVariant, fontSize = 14.sp)
            }

            VetTextField(
                peso, { peso = it.replace(',', '.') }, "Peso (kg)",
                error = errores.peso, tipoTeclado = KeyboardType.Decimal
            )
            Text("Peso anterior: ${pesoTexto(mascota.peso)}", color = colores.onSurfaceVariant, fontSize = 13.sp)
            VetTextField(diagnostico, { diagnostico = it }, "Diagnóstico", error = errores.diagnostico, unaLinea = false)
            VetTextField(tratamiento, { tratamiento = it }, "Tratamiento", unaLinea = false)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colores.surfaceVariant, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Icon(Icons.Outlined.Info, contentDescription = null, tint = colores.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Se actualizará el peso de ${mascota.nombre} en su ficha", fontSize = 14.sp)
            }

            mensaje?.let { Text(it, color = colores.error, fontSize = 14.sp) }

            val activa = vm.sePuedeAtender(c)
            VetButton(
                texto = "Registrar atención",
                onClick = { vm.registrar(diagnostico, tratamiento, peso.toDoubleOrNull(), onVolver) },
                habilitado = activa
            )
            VetButton(texto = "Cancelar cita", onClick = { confirmarCancelar = true }, habilitado = activa, contorno = true)
        }
    }

    if (confirmarCancelar) {
        AlertDialog(
            onDismissRequest = { confirmarCancelar = false },
            title = { Text("Cancelar cita") },
            text = { Text("La cita pasará a CANCELADA y no se podrá atender.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarCancelar = false
                    vm.cancelar(onVolver)
                }) { Text("Sí, cancelar", color = colores.error) }
            },
            dismissButton = { TextButton(onClick = { confirmarCancelar = false }) { Text("Volver") } }
        )
    }
}
