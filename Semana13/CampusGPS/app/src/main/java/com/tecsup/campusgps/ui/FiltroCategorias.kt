package com.tecsup.campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FiltroCategorias(
    categorias: List<String>,
    seleccionada: String?,
    onSeleccionar: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            val esTodosSeleccionado = seleccionada == null
            FilterChip(
                selected = esTodosSeleccionado,
                onClick = { onSeleccionar(null) },
                label = { Text("Todos") },
                leadingIcon = if (esTodosSeleccionado) {
                    { Icon(imageVector = Icons.Filled.Check, contentDescription = null) }
                } else null
            )
        }
        items(categorias) { cat ->
            val esSeleccionado = seleccionada == cat
            FilterChip(
                selected = esSeleccionado,
                onClick = { onSeleccionar(cat) },
                label = { Text(cat) },
                leadingIcon = if (esSeleccionado) {
                    { Icon(imageVector = Icons.Filled.Check, contentDescription = null) }
                } else null
            )
        }
    }
}
