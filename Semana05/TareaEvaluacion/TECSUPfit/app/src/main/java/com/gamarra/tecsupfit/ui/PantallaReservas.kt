package com.gamarra.tecsupfit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gamarra.tecsupfit.model.GymReservation
import com.gamarra.tecsupfit.model.ReservationStatus
import com.gamarra.tecsupfit.ui.theme.FitGrisBadgeFondo
import com.gamarra.tecsupfit.ui.theme.FitGrisFondo
import com.gamarra.tecsupfit.ui.theme.FitGrisTexto
import com.gamarra.tecsupfit.ui.theme.FitVerdeBadgeFondo
import com.gamarra.tecsupfit.ui.theme.FitVerdePrincipal
import com.gamarra.tecsupfit.ui.theme.FitVerdeTexto

@Composable
fun PantallaReservas(
    listaReservas: List<GymReservation>,
    onCancelarReserva: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "Mis reservas",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (listaReservas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No tienes reservas registradas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = FitGrisTexto
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(listaReservas, key = { it.id }) { reserva ->
                    TarjetaReservaItem(
                        reserva = reserva,
                        onCancelar = { onCancelarReserva(reserva.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaReservaItem(
    reserva: GymReservation,
    onCancelar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FitGrisFondo),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // Franja vertical verde para reservas activas
            if (reserva.status == ReservationStatus.CONFIRMADA) {
                Box(
                    modifier = Modifier
                        .width(6.dp)
                        .fillMaxHeight()
                        .background(
                            color = FitVerdePrincipal,
                            shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                        )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = reserva.className,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = reserva.schedule,
                    style = MaterialTheme.typography.bodyMedium,
                    color = FitGrisTexto
                )

                Spacer(modifier = Modifier.height(10.dp))

                val isConfirmada = reserva.status == ReservationStatus.CONFIRMADA
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isConfirmada) FitVerdeBadgeFondo else FitGrisBadgeFondo
                ) {
                    Text(
                        text = if (isConfirmada) "Confirmada" else "Completada",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = if (isConfirmada) FitVerdeTexto else Color.Gray
                    )
                }

                if (isConfirmada) {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = onCancelar,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Cancelar reserva")
                    }
                }
            }
        }
    }
}