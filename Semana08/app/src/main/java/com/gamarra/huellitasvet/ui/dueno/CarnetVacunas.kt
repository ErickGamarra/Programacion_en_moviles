package com.gamarra.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamarra.huellitasvet.data.local.entity.Vacuna
import com.gamarra.huellitasvet.model.EstadoVacuna
import com.gamarra.huellitasvet.ui.components.VetButton
import com.gamarra.huellitasvet.ui.components.VetTextField
import com.gamarra.huellitasvet.ui.components.fechaLarga
import com.gamarra.huellitasvet.ui.theme.EstadoCancelada
import com.gamarra.huellitasvet.ui.theme.EstadoConfirmada
import com.gamarra.huellitasvet.ui.theme.EstadoPendiente
import com.gamarra.huellitasvet.viewmodel.VacunasViewModel
import java.time.LocalDate

@Composable
fun CarnetVacunas(vm: VacunasViewModel, mascotaId: Int, vacunas: List<Vacuna>) {
    var registrando by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (vacunas.isEmpty()) {
            Text("Aún no tiene vacunas registradas", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        vacunas.forEach { v -> ItemVacuna(v, vm.estado(v)) { vm.eliminar(v) } }
        if (registrando) {
            FormularioVacuna(vm, mascotaId) {
                vm.limpiar()
                registrando = false
            }
        } else {
            VetButton(texto = "Registrar vacuna", onClick = { registrando = true }, contorno = true)
        }
    }
}

@Composable
private fun ItemVacuna(v: Vacuna, estado: EstadoVacuna, onEliminar: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    val (texto, color) = when (estado) {
        EstadoVacuna.AL_DIA -> "Al día" to EstadoConfirmada
        EstadoVacuna.PROXIMA -> "Próxima" to EstadoPendiente
        EstadoVacuna.VENCIDA -> "Vencida" to EstadoCancelada
        EstadoVacuna.DOSIS_UNICA -> "Dosis única" to colores.onSurfaceVariant
    }
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colores.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(start = 14.dp, top = 12.dp, bottom = 12.dp, end = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).background(colores.primaryContainer, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Vaccines, contentDescription = null, tint = colores.primary, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(v.nombre, fontWeight = FontWeight.SemiBold)
                Text("Aplicada: ${fechaLarga(v.fecha)}", color = colores.onSurfaceVariant, fontSize = 13.sp)
                v.proximaDosis?.let { Text("Próxima dosis: ${fechaLarga(it)}", color = colores.onSurfaceVariant, fontSize = 13.sp) }
            }
            Chip(texto, color)
            IconButton(onClick = onEliminar) {
                Icon(Icons.Outlined.Delete, contentDescription = "Eliminar vacuna", tint = colores.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun Chip(texto: String, color: Color) {
    Text(
        texto,
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

@Composable
private fun FormularioVacuna(vm: VacunasViewModel, mascotaId: Int, onCerrar: () -> Unit) {
    val errores by vm.errores.collectAsState()
    var nombre by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf(LocalDate.now().toString()) }
    var proxima by remember { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Registrar vacuna", fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
            VetTextField(nombre, { nombre = it }, "Vacuna", error = errores.nombre)
            VetTextField(fecha, { fecha = it }, "Fecha aplicada (aaaa-mm-dd)", error = errores.fecha)
            VetTextField(proxima, { proxima = it }, "Próxima dosis (opcional)", error = errores.proximaDosis)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                VetButton("Cancelar", onCerrar, Modifier.weight(1f), contorno = true)
                VetButton("Guardar", { vm.guardar(mascotaId, nombre, fecha, proxima, onCerrar) }, Modifier.weight(1f))
            }
        }
    }
}
