package com.tecsup.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.util.formatearDistancia

// Mismo verde que HUE_GREEN del marcador
private val VerdeCercano = Color(0xFF2E9E44)

/**
 * Lista de lugares ordenada. No conoce el ViewModel: recibe los datos y avisa el clic con el id.
 */
@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercanoId: Int?,
    radioMetros: Int,
    onClick: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estadoLista = rememberLazyListState()

    // Si cambia el primer lugar (nueva ubicación), volvemos arriba para que el más cercano quede a la vista;
    // con key() la lista conserva el scroll del elemento anterior y lo dejaría oculto
    LaunchedEffect(lugares.firstOrNull()?.lugar?.id) {
        estadoLista.scrollToItem(0)
    }

    LazyColumn(
        modifier = modifier,
        state = estadoLista,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(lugares, key = { it.lugar.id }) { item ->
            TarjetaLugar(
                item = item,
                esSeleccionado = item.lugar.id == seleccionado,
                esMasCercano = item.lugar.id == masCercanoId,
                radioMetros = radioMetros,
                onClick = { onClick(item.lugar.id) }
            )
        }
    }
}

@Composable
private fun TarjetaLugar(
    item: LugarConDistancia,
    esSeleccionado: Boolean,
    esMasCercano: Boolean,
    radioMetros: Int,
    onClick: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (esSeleccionado) colores.primaryContainer else colores.surfaceVariant
        ),
        border = if (esSeleccionado) BorderStroke(1.5.dp, colores.primary) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Punto del mismo color que el marcador del mapa
            Spacer(
                Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (esMasCercano) VerdeCercano else colores.primary)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(item.lugar.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                Text(item.lugar.categoria, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                if (item.metros != null && item.metros <= radioMetros) {
                    Surface(
                        shape = CircleShape,
                        color = colores.secondaryContainer,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = "Dentro del radio",
                            color = colores.onSecondaryContainer,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            Text(
                text = item.metros?.let { formatearDistancia(it) } ?: "Sin calcular",
                color = colores.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
