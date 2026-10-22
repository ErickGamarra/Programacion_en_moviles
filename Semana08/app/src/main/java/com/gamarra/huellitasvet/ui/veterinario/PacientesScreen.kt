package com.gamarra.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.gamarra.huellitasvet.data.local.relation.MascotaConDueno
import com.gamarra.huellitasvet.model.Especie
import com.gamarra.huellitasvet.ui.components.MascotaAvatar
import com.gamarra.huellitasvet.ui.components.VetTextField
import com.gamarra.huellitasvet.ui.components.edadTexto
import com.gamarra.huellitasvet.ui.components.fechaLarga
import com.gamarra.huellitasvet.viewmodel.PacientesViewModel

private val filtros = listOf(null to "Todos", Especie.PERRO to "Perros", Especie.GATO to "Gatos", Especie.OTRO to "Otros")

@Composable
fun PacientesScreen(vm: PacientesViewModel, onVerPaciente: (Int) -> Unit, modifier: Modifier = Modifier) {
    val texto by vm.texto.collectAsState()
    val especie by vm.especie.collectAsState()
    val pacientes by vm.pacientes.collectAsState()
    val ultimaVisita by vm.ultimaVisita.collectAsState()
    val colores = MaterialTheme.colorScheme

    LazyColumn(
        modifier = modifier.fillMaxSize().background(colores.background),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Text("Pacientes", fontSize = 28.sp, fontWeight = FontWeight.Bold) }
        item { VetTextField(texto, vm::buscar, "Buscar por mascota o dueño", icono = Icons.Outlined.Search) }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filtros) { (valor, nombre) ->
                    FilterChip(
                        selected = especie == valor,
                        onClick = { vm.filtrar(valor) },
                        label = { Text(nombre) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = colores.primary,
                            selectedLabelColor = colores.onPrimary
                        )
                    )
                }
            }
        }
        if (pacientes.isEmpty()) {
            item { Text("No se encontraron pacientes", color = colores.onSurfaceVariant) }
        }
        items(pacientes, key = { it.mascota.id }) { p ->
            TarjetaPaciente(p, ultimaVisita[p.mascota.id]) { onVerPaciente(p.mascota.id) }
        }
    }
}

@Composable
private fun TarjetaPaciente(p: MascotaConDueno, ultima: String?, onClick: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = colores.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            MascotaAvatar(p.mascota.especie, tamano = 56.dp)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(p.mascota.nombre, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                Text("${p.mascota.raza} · ${edadTexto(p.mascota.anioNacimiento)}", color = colores.onSurfaceVariant, fontSize = 14.sp)
                Text("Dueño: ${p.dueno.nombre}", fontSize = 14.sp)
                Text(
                    ultima?.let { "Última visita: ${fechaLarga(it)}" } ?: "Sin visitas",
                    color = colores.primary,
                    fontSize = 13.sp
                )
            }
        }
    }
}
