package com.saludplus.citas.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.ui.components.BarraSuperior

private val secciones = listOf(
    "1. Uso de la aplicación" to "SaludPlus permite a los pacientes agendar, consultar y cancelar citas médicas. El usuario se compromete a ingresar datos verdaderos al registrarse.",
    "2. Citas médicas" to "Cada cita queda sujeta a la disponibilidad del médico. El paciente puede cancelarla desde el detalle de la cita antes de la fecha programada.",
    "3. Privacidad de los datos" to "Los datos personales se usan solo para gestionar las citas y no se comparten con terceros sin autorización del paciente.",
    "4. Resultados médicos" to "Los resultados de exámenes se muestran de forma informativa y no reemplazan la consulta con un profesional de la salud.",
    "5. Responsabilidad" to "El usuario es responsable de mantener la confidencialidad de su contraseña y de cerrar sesión al terminar de usar la aplicación."
)

@Composable
fun TerminosScreen(onVolver: () -> Unit) {
    Scaffold(topBar = { BarraSuperior("Términos y condiciones", onVolver) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            secciones.forEach { (titulo, texto) ->
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(titulo, style = MaterialTheme.typography.titleMedium)
                    Text(texto, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
