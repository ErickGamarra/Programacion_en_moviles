package com.tecsup.campusgps.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AzulCampus,
    onPrimary = Color.White,
    primaryContainer = AzulCampusClaro,
    onPrimaryContainer = Color(0xFF0B3A5E),
    errorContainer = ErrorFondo,
    onErrorContainer = ErrorTexto
)

private val DarkColorScheme = darkColorScheme(
    primary = AzulCampusOscuro,
    onPrimary = Color(0xFF0B3A5E),
    primaryContainer = Color(0xFF1B4F7A),
    onPrimaryContainer = AzulCampusClaro
)

// Sin color dinámico: la barra debe verse azul en cualquier dispositivo
@Composable
fun CampusGPSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
