package com.tecsup.murocampus.ui.theme

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
    secondaryContainer = AzulCampusClaro,
    error = RojoError,
    errorContainer = RojoErrorClaro
)

private val DarkColorScheme = darkColorScheme(
    primary = AzulCampusOscuro,
    onPrimary = Color(0xFF0B2E5E)
)

// Sin color dinámico: la barra se ve azul en cualquier dispositivo
@Composable
fun MuroCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
