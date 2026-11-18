package com.tecsup.centrotareas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AzulTareas,
    onPrimary = Color.White,
    primaryContainer = AzulTareasClaro
)

private val DarkColorScheme = darkColorScheme(
    primary = AzulTareasOscuro,
    onPrimary = Color(0xFF0B3A5E)
)

// Sin color dinámico: la barra y los títulos se ven azules en cualquier dispositivo
@Composable
fun CentroTareasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
