package com.gamarra.tecsupfit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gamarra.tecsupfit.ui.theme.FitGrisFondo
import com.gamarra.tecsupfit.ui.theme.FitGrisTexto
import com.gamarra.tecsupfit.ui.theme.FitVerdeBadgeFondo
import com.gamarra.tecsupfit.ui.theme.FitVerdeTexto

@Composable
fun PantallaConfirmacion(
    className: String,
    horario: String,
    onVerMisReservas: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Contenedor circular con icono de check
        Box(
            modifier = Modifier
                .size(92.dp)
                .clip(CircleShape)
                .background(FitVerdeBadgeFondo),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = FitVerdeTexto,
                modifier = Modifier.size(52.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "¡Cupo reservado!",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = className,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = horario,
            style = MaterialTheme.typography.bodyMedium,
            color = FitGrisTexto
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onVerMisReservas,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FitGrisFondo)
        ) {
            Text(
                text = "Ver mis reservas",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}