package com.tecsup.pokemon.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = Fuego,
    onPrimary = Color.White,
    primaryContainer = BrasaClara,
    onPrimaryContainer = FuegoOscuro,
    secondary = Brasa,
    onSecondary = Color.White,
    tertiary = Chispa,
    onTertiary = Ceniza,
    background = Crema,
    onBackground = Ceniza,
    surface = Crema,
    onSurface = Ceniza,
    surfaceVariant = Color(0xFFF6E3DA),
    onSurfaceVariant = Color(0xFF5A4740)
)

private val EsquemaOscuro = darkColorScheme(
    primary = FuegoClaro,
    onPrimary = Color(0xFF5F1500),
    primaryContainer = FuegoOscuro,
    onPrimaryContainer = BrasaClara,
    secondary = Brasa,
    onSecondary = Color(0xFF3B1800),
    tertiary = Chispa,
    onTertiary = Ceniza,
    background = NocheVolcan,
    onBackground = Color(0xFFF1DFD8),
    surface = NocheVolcan,
    onSurface = Color(0xFFF1DFD8),
    surfaceVariant = SuperficieOscura,
    onSurfaceVariant = Color(0xFFD8C2BA)
)

@Composable
fun PokedexFireGamarraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Sin colores dinámicos: la app siempre usa la paleta de fuego.
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
        typography = Typography,
        content = content
    )
}
