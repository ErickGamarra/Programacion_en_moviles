package com.gamarra.clientesaludplus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gamarra.clientesaludplus.model.Doctor
import com.gamarra.clientesaludplus.ui.theme.ClinicaGrisFondo
import com.gamarra.clientesaludplus.ui.theme.ClinicaMorado

data class OpcionFecha(val diaNombre: String, val diaNumero: String, val valorCompleto: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgendarCita(
    doctor: Doctor?,
    onBack: () -> Unit,
    onConfirmar: (String, String) -> Unit
) {
    val listaFechas = listOf(
        OpcionFecha("Jue", "26", "Jueves 26"),
        OpcionFecha("Vie", "27", "Viernes 27"),
        OpcionFecha("Sáb", "28", "Sábado 28")
    )
    val listaHoras = listOf("9:00", "10:30", "3:00")

    var fechaSeleccionada by remember { mutableStateOf("Viernes 27") }
    var horaSeleccionada by remember { mutableStateOf("10:30") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(
                text = "Selecciona fecha",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Selector horizontal de fecha (cajas rectangulares)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                listaFechas.forEach { opcion ->
                    val isSelected = opcion.valorCompleto == fechaSeleccionada
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(76.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) ClinicaMorado else ClinicaGrisFondo)
                            .clickable { fechaSeleccionada = opcion.valorCompleto },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = opcion.diaNombre,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isSelected) Color.White else Color.Gray
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = opcion.diaNumero,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) Color.White else Color.Black
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Selecciona hora",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Selector horizontal de hora
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                listaHoras.forEach { hora ->
                    val isSelected = hora == horaSeleccionada
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) ClinicaMorado else ClinicaGrisFondo)
                            .clickable { horaSeleccionada = hora },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = hora,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onConfirmar(fechaSeleccionada, horaSeleccionada) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ClinicaMorado)
            ) {
                Text(
                    text = "Confirmar cita",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White
                )
            }
        }
    }
}