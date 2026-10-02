package com.gamarra.lab06.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gamarra.lab06.data.Producto

@Composable
fun TarjetaProducto(
    producto: Producto,
    onAgregarFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                    Text(producto.precioFormateado, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Favoritos") },
                        leadingIcon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                        onClick = {
                            expanded = false
                            onAgregarFavorito()
                            Toast.makeText(context, "Añadido a Favoritos", Toast.LENGTH_SHORT).show()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Compartir") },
                        leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) },
                        onClick = {
                            expanded = false
                            Toast.makeText(context, "Compartir ${producto.nombre}", Toast.LENGTH_SHORT).show()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Reportar") },
                        leadingIcon = { Icon(Icons.Default.Warning, contentDescription = null) },
                        onClick = {
                            expanded = false
                            Toast.makeText(context, "Reportar ${producto.nombre}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}