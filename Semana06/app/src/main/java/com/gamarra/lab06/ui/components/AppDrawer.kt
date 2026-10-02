package com.gamarra.lab06.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawerContent(
    favoritosCount: Int,
    onDestinationClick: (String) -> Unit
) {
    ModalDrawerSheet {
        // Encabezado con Avatar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("MR", style = MaterialTheme.typography.titleMedium)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Maria Rojas", style = MaterialTheme.typography.titleMedium)
                Text("maria@tecsup.edu.pe", style = MaterialTheme.typography.bodySmall)
            }
        }

        HorizontalDivider()
        Spacer(modifier = Modifier.height(12.dp))

        NavigationDrawerItem(
            label = { Text("Inicio") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            selected = false,
            onClick = { onDestinationClick("Inicio") }
        )
        NavigationDrawerItem(
            label = { Text("Mis pedidos") },
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
            selected = true,
            onClick = { onDestinationClick("Mis pedidos") }
        )
        NavigationDrawerItem(
            label = { Text("Favoritos") },
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
            badge = {
                if (favoritosCount > 0) {
                    Badge { Text(favoritosCount.toString()) }
                }
            },
            selected = false,
            onClick = { onDestinationClick("Favoritos") }
        )
        NavigationDrawerItem(
            label = { Text("Perfil") },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            selected = false,
            onClick = { onDestinationClick("Perfil") }
        )
        NavigationDrawerItem(
            label = { Text("Cerrar sesión") },
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
            selected = false,
            onClick = { onDestinationClick("Cerrar sesión") }
        )
    }
}