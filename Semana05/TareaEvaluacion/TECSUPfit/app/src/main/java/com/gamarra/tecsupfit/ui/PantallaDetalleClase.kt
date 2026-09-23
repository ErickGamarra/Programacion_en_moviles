package com.gamarra.tecsupfit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.gamarra.tecsupfit.model.GymClass
import com.gamarra.tecsupfit.ui.theme.FitGrisFondo
import com.gamarra.tecsupfit.ui.theme.FitGrisTexto
import com.gamarra.tecsupfit.ui.theme.FitVerdeBadgeFondo
import com.gamarra.tecsupfit.ui.theme.FitVerdeContenedor
import com.gamarra.tecsupfit.ui.theme.FitVerdePrincipal
import com.gamarra.tecsupfit.ui.theme.FitVerdeTexto

data class OpcionHorarioDetalle(
    val id: String,
    val horario: String,
    val turnoNombre: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleClase(
    gymClass: GymClass?,
    onBack: () -> Unit,
    onReservarClick: (String) -> Unit
) {
    val hayCupos = (gymClass?.availableSlots ?: 0) > 0

    val listaTurnos = remember(gymClass) {
        listOf(
            OpcionHorarioDetalle(
                id = "1",
                horario = gymClass?.time ?: "7:00 am",
                turnoNombre = "Turno Principal"
            ),
            OpcionHorarioDetalle(
                id = "2",
                horario = "8:30 pm",
                turnoNombre = "Turno Tarde/Noche"
            )
        )
    }

    var horarioSeleccionado by remember(gymClass) {
        mutableStateOf(gymClass?.time ?: "7:00 am")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de clase", fontWeight = FontWeight.SemiBold) },
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
        if (gymClass == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Clase no disponible o no encontrada", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar representativo
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(if (hayCupos) FitVerdeContenedor else Color(0xFFF0F0F0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FitnessCenter,
                        contentDescription = null,
                        tint = if (hayCupos) FitVerdePrincipal else Color.Gray,
                        modifier = Modifier.size(46.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = gymClass.name,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${gymClass.room} · ${gymClass.duration}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = FitGrisTexto
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Badge de aforo
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (hayCupos) FitVerdeBadgeFondo else Color(0xFFFFEBEE)
                ) {
                    Text(
                        text = if (hayCupos) {
                            "${gymClass.availableSlots} cupos disponibles de ${gymClass.totalSlots}"
                        } else {
                            "Cupos agotados para esta fecha"
                        },
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = if (hayCupos) FitVerdeTexto else Color(0xFFC62828),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Descripción
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = FitGrisFondo)
                ) {
                    Text(
                        text = gymClass.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Selector de turnos
                Text(
                    text = "Selecciona un turno:",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listaTurnos.forEach { turno ->
                        val isSelected = turno.horario == horarioSeleccionado

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = if (isSelected && hayCupos) 1.5.dp else 0.dp,
                                    color = if (isSelected && hayCupos) FitVerdePrincipal else Color.Transparent,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable(enabled = hayCupos) {
                                    horarioSeleccionado = turno.horario
                                },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected && hayCupos) FitVerdeContenedor.copy(alpha = 0.45f) else FitGrisFondo
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { if (hayCupos) horarioSeleccionado = turno.horario },
                                    enabled = hayCupos,
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = FitVerdePrincipal,
                                        unselectedColor = Color.Gray
                                    )
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = turno.turnoNombre,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = if (hayCupos) Color.Black else Color.Gray
                                    )
                                    Text(
                                        text = turno.horario,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = FitGrisTexto
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Outlined.Schedule,
                                    contentDescription = null,
                                    tint = if (isSelected && hayCupos) FitVerdePrincipal else Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                if (!hayCupos) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null,
                            tint = Color(0xFFC62828),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "No es posible reservar en este momento por falta de cupos.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFC62828)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Botón con deshabilitación condicional
                Button(
                    onClick = { onReservarClick(horarioSeleccionado) },
                    enabled = hayCupos,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FitVerdePrincipal,
                        disabledContainerColor = Color(0xFFE0E0E0),
                        disabledContentColor = Color.Gray
                    )
                ) {
                    Text(
                        text = if (hayCupos) "Reservar cupo" else "Aforo completo",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = if (hayCupos) Color.White else Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}