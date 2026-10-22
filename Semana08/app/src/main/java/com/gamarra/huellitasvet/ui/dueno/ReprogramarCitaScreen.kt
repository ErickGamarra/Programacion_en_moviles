package com.gamarra.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamarra.huellitasvet.ui.components.CitaCard
import com.gamarra.huellitasvet.ui.components.FechaChip
import com.gamarra.huellitasvet.ui.components.HoraChip
import com.gamarra.huellitasvet.ui.components.VetButton
import com.gamarra.huellitasvet.ui.components.fechaLarga
import com.gamarra.huellitasvet.viewmodel.ReprogramarCitaViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReprogramarCitaScreen(vm: ReprogramarCitaViewModel, citaId: Int, onVolver: () -> Unit) {
    LaunchedEffect(citaId) { vm.cargar(citaId) }
    val cargada by vm.cita.collectAsState()
    val detalle = cargada?.takeIf { it.cita.id == citaId }
    val fecha by vm.fecha.collectAsState()
    val hora by vm.hora.collectAsState()
    val ocupadas by vm.horasOcupadas.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val colores = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reprogramar cita") },
                navigationIcon = {
                    IconButton(onClick = onVolver) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colores.background)
            )
        }
    ) { padding ->
        val c = detalle ?: return@Scaffold
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colores.background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Cita actual", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            CitaCard(c)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colores.primaryContainer, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Icon(Icons.Outlined.Info, contentDescription = null, tint = colores.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    "Puedes reprogramar hasta 2 horas antes. La cita vuelve a quedar PENDIENTE hasta que la clínica la confirme.",
                    color = colores.onPrimaryContainer,
                    fontSize = 14.sp
                )
            }

            Text("Nueva fecha", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(vm.fechas) { f -> FechaChip(f, seleccionado = f == fecha) { vm.elegirFecha(f) } }
            }

            Text("Nueva hora", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                vm.horarios.forEach { h ->
                    HoraChip(h, seleccionado = h == hora, ocupado = !vm.estaDisponible(h, ocupadas)) { vm.elegirHora(h) }
                }
            }

            hora?.let {
                Text(
                    "Nuevo horario: ${fechaLarga(fecha.toString())} a las $it",
                    color = colores.primary,
                    fontWeight = FontWeight.Medium
                )
            }
            mensaje?.let { Text(it, color = colores.error, fontSize = 14.sp) }

            VetButton(
                texto = "Confirmar cambio",
                onClick = { vm.confirmar(onVolver) },
                habilitado = hora != null && vm.puedeReprogramar(c)
            )
        }
    }
}
