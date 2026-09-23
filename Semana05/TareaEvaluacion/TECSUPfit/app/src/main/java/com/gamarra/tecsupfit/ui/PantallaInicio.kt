package com.gamarra.tecsupfit.ui

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.gamarra.tecsupfit.model.FilterPeriod
import com.gamarra.tecsupfit.model.GymClass
import com.gamarra.tecsupfit.ui.theme.FitGrisFondo
import com.gamarra.tecsupfit.ui.theme.FitGrisTexto
import com.gamarra.tecsupfit.ui.theme.FitVerdeBadgeFondo
import com.gamarra.tecsupfit.ui.theme.FitVerdeContenedor
import com.gamarra.tecsupfit.ui.theme.FitVerdePrincipal
import com.gamarra.tecsupfit.ui.theme.FitVerdeTexto

@Composable
fun PantallaInicio(
    clases: List<GymClass>,
    onClassClick: (String) -> Unit
) {
    var selectedPeriod by remember { mutableStateOf(FilterPeriod.HOY) }

    val filteredClasses = remember(selectedPeriod, clases) {
        clases.filter { it.period == selectedPeriod }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Cabecera institucional
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = FitVerdePrincipal,
                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                )
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Column {
                Text(
                    text = "TECSUP Fit",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Hola, Diego",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Filtro de periodos
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(FilterPeriod.entries.toTypedArray()) { period ->
                val isSelected = period == selectedPeriod
                Surface(
                    shape = CircleShape,
                    color = if (isSelected) FitVerdePrincipal else FitGrisFondo,
                    modifier = Modifier.clickable { selectedPeriod = period }
                ) {
                    Text(
                        text = period.label,
                        color = if (isSelected) Color.White else Color.Black,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "Clases disponibles",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredClasses, key = { it.id }) { gymClass ->
                TarjetaClaseItem(
                    gymClass = gymClass,
                    onClick = {
                        if (gymClass.availableSlots > 0) {
                            onClassClick(gymClass.id)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TarjetaClaseItem(
    gymClass: GymClass,
    onClick: () -> Unit
) {
    val tieneCupos = gymClass.availableSlots > 0
    val ultimosCupos = gymClass.availableSlots in 1..3
    val porcentajeOcupacion = if (gymClass.totalSlots > 0) {
        (gymClass.totalSlots - gymClass.availableSlots).toFloat() / gymClass.totalSlots.toFloat()
    } else 0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = tieneCupos) { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (tieneCupos) FitGrisFondo else Color(0xFFF7F7F7)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono representativo o bloqueo si está agotado
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (tieneCupos) FitVerdeContenedor else Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (tieneCupos) Icons.Outlined.FitnessCenter else Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = if (tieneCupos) FitVerdePrincipal else Color.Gray,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = gymClass.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (tieneCupos) Color.Black else Color.Gray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${gymClass.time} · ${gymClass.room}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = FitGrisTexto
                    )
                }

                // Badge de disponibilidad contextual
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when {
                        !tieneCupos -> Color(0xFFFFEBEE)
                        ultimosCupos -> Color(0xFFFFF3E0)
                        else -> FitVerdeBadgeFondo
                    }
                ) {
                    Text(
                        text = when {
                            !tieneCupos -> "Agotado"
                            ultimosCupos -> "¡Últimos ${gymClass.availableSlots}!"
                            else -> "${gymClass.availableSlots} cupos"
                        },
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = when {
                            !tieneCupos -> Color(0xFFC62828)
                            ultimosCupos -> Color(0xFFE65100)
                            else -> FitVerdeTexto
                        },
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Barra indicadora de nivel de ocupación
            LinearProgressIndicator(
                progress = { porcentajeOcupacion },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = when {
                    !tieneCupos -> Color.Gray
                    ultimosCupos -> Color(0xFFE65100)
                    else -> FitVerdePrincipal
                },
                trackColor = Color(0xFFE0E0E0)
            )
        }
    }
}