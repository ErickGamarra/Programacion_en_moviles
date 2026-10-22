package com.gamarra.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamarra.huellitasvet.data.local.entity.Servicio
import com.gamarra.huellitasvet.ui.components.FechaChip
import com.gamarra.huellitasvet.ui.components.HoraChip
import com.gamarra.huellitasvet.ui.components.MascotaAvatar
import com.gamarra.huellitasvet.ui.components.VetButton
import com.gamarra.huellitasvet.ui.components.VetTextField
import com.gamarra.huellitasvet.ui.components.fechaLarga
import com.gamarra.huellitasvet.ui.components.iconoServicio
import com.gamarra.huellitasvet.ui.components.precioTexto
import com.gamarra.huellitasvet.viewmodel.PedirCitaViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PedirCitaScreen(vm: PedirCitaViewModel, onVolver: () -> Unit, onCreada: () -> Unit) {
    val mascotas by vm.mascotas.collectAsState()
    val servicios by vm.servicios.collectAsState()
    val mascota by vm.mascota.collectAsState()
    val servicio by vm.servicio.collectAsState()
    val fecha by vm.fecha.collectAsState()
    val hora by vm.hora.collectAsState()
    val ocupadas by vm.horasOcupadas.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val aceptarCitas by vm.aceptarCitas.collectAsState()
    var nota by remember { mutableStateOf("") }
    val colores = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedir cita") },
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (!aceptarCitas) {
                Text(
                    "La clínica no está recibiendo citas",
                    color = colores.onErrorContainer,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colores.errorContainer, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                )
            }
            Seccion("Mascota")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(mascotas, key = { it.id }) { m ->
                    val elegida = mascota?.id == m.id
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { vm.elegirMascota(m) }.padding(4.dp)
                    ) {
                        Box(
                            Modifier
                                .background(if (elegida) colores.primary else colores.surface, CircleShape)
                                .padding(3.dp)
                        ) { MascotaAvatar(m.especie, tamano = 58.dp) }
                        Text(
                            m.nombre,
                            fontWeight = if (elegida) FontWeight.Bold else FontWeight.Normal,
                            color = if (elegida) colores.primary else colores.onSurface,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Seccion("Servicio")
            servicios.forEach { s ->
                TarjetaServicio(s, elegido = servicio?.id == s.id) { vm.elegirServicio(s) }
            }

            Seccion("Fecha")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(vm.fechas) { f -> FechaChip(f, seleccionado = f == fecha) { vm.elegirFecha(f) } }
            }

            Seccion("Hora")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                vm.horarios.forEach { h ->
                    HoraChip(h, seleccionado = h == hora, ocupado = !vm.estaDisponible(h, ocupadas)) { vm.elegirHora(h) }
                }
            }

            VetTextField(nota, { nota = it }, "Nota (opcional)", unaLinea = false)

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = colores.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Resumen", fontWeight = FontWeight.Bold, color = colores.onPrimaryContainer)
                    FilaResumen("Mascota", mascota?.nombre)
                    FilaResumen("Servicio", servicio?.nombre)
                    FilaResumen("Fecha", fechaLarga(fecha.toString()))
                    FilaResumen("Hora", hora)
                    HorizontalDivider(color = colores.onPrimaryContainer.copy(alpha = 0.2f))
                    FilaResumen("Precio", servicio?.let { precioTexto(it.precio) }, destacado = true)
                }
            }

            mensaje?.let { Text(it, color = colores.error, fontSize = 14.sp) }

            VetButton(
                texto = "Confirmar cita",
                onClick = { vm.confirmar(nota, onCreada) },
                habilitado = aceptarCitas && mascota != null && servicio != null && hora != null
            )
            Spacer(Modifier.size(8.dp))
        }
    }
}

@Composable
private fun Seccion(texto: String) {
    Text(texto, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
}

@Composable
private fun TarjetaServicio(s: Servicio, elegido: Boolean, onClick: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colores.surface),
        border = BorderStroke(if (elegido) 2.dp else 1.dp, if (elegido) colores.primary else colores.outlineVariant),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(40.dp).background(colores.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(iconoServicio(s.icono), contentDescription = null, tint = colores.primary, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(s.nombre, fontWeight = FontWeight.SemiBold)
                Text("${s.duracionMin} min", color = colores.onSurfaceVariant, fontSize = 13.sp)
            }
            Text(precioTexto(s.precio), fontWeight = FontWeight.Bold, color = colores.primary)
        }
    }
}

@Composable
private fun FilaResumen(etiqueta: String, valor: String?, destacado: Boolean = false) {
    val color = MaterialTheme.colorScheme.onPrimaryContainer
    Row(Modifier.fillMaxWidth()) {
        Text(etiqueta, color = color.copy(alpha = 0.7f), modifier = Modifier.weight(1f))
        Text(
            valor ?: "—",
            color = color,
            fontWeight = if (destacado) FontWeight.Bold else FontWeight.Medium,
            fontSize = if (destacado) 17.sp else 15.sp
        )
    }
}
