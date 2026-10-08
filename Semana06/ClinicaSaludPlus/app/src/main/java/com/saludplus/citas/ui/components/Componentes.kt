package com.saludplus.citas.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Contenido temporal de las pantallas del esqueleto. Cada boton de [acciones] permite pasar a la
 * siguiente pantalla. Al terminar una pantalla, borra su llamada a PantallaEnConstruccion.
 */
@Composable
fun PantallaEnConstruccion(
    titulo: String,
    acciones: List<Pair<String, () -> Unit>> = emptyList(),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Text(text = titulo, style = MaterialTheme.typography.titleLarge)
        Text(text = "Pantalla en construccion", style = MaterialTheme.typography.bodyLarge)
        acciones.forEach { (texto, onClick) ->
            Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
                Text(texto)
            }
        }
    }
}

/*
 * Componentes reutilizables sugeridos (por crear en este paquete):
 *
 *  - BarraSuperior(titulo, onVolver): TopAppBar con titulo y flecha de regreso.
 *  - BarraInferior(rutaActual, onNavegar): NavigationBar con Inicio, Citas, Resultados y Perfil.
 *  - CampoTexto(...): OutlinedTextField con etiqueta, mensaje de error y validacion.
 *  - TarjetaEspecialidad(especialidad, onClick): fila/tarjeta para LazyRow y LazyColumn.
 *  - TarjetaMedico(medico, onClick): nombre, especialidad y calificacion.
 *  - TarjetaCita(cita, onClick): fecha, hora y medico de una cita.
 *  - ChipHorario(hora, seleccionado, onClick): celda del LazyVerticalGrid de horarios.
 *  - ListaVacia(mensaje): mensaje para listas sin elementos (ej. "Aun no tienes citas").
 */
