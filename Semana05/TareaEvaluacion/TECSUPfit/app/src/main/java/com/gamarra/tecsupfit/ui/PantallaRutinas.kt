package com.gamarra.tecsupfit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gamarra.tecsupfit.ui.theme.FitGrisFondo
import com.gamarra.tecsupfit.ui.theme.FitGrisTexto

@Composable
fun PantallaRutinas() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "Mis rutinas asignadas",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                TarjetaRutina("Fuerza Tren Superior", "4 ejercicios · 45 min", "Enfoque en pecho, hombro y tríceps con peso progresivo.")
            }
            item {
                TarjetaRutina("Core y Estabilidad", "5 ejercicios · 30 min", "Trabajo isométrico de abdomen y zona lumbar.")
            }
            item {
                TarjetaRutina("Cardio Hiit Acondicionamiento", "6 intervalos · 25 min", "Quema calórica y resistencia cardiovascular.")
            }
        }
    }
}

@Composable
private fun TarjetaRutina(titulo: String, subtitulo: String, descripcion: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FitGrisFondo)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = titulo, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitulo, style = MaterialTheme.typography.bodySmall, color = FitGrisTexto)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = descripcion, style = MaterialTheme.typography.bodyMedium)
        }
    }
}