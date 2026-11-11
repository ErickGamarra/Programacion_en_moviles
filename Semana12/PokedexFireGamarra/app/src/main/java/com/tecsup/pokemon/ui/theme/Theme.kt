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
    onSurfaceVariant = Color(0xFF5A4740),
    // Tonos cálidos para tarjetas y paneles (por defecto Material usa lila)
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFFFF1EA),
    surfaceContainer = Color(0xFFFBEAE2),
    surfaceContainerHigh = Color(0xFFF6E3DA),
    surfaceContainerHighest = Color(0xFFF0DCD2)
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
    onSurfaceVariant = Color(0xFFD8C2BA),
    surfaceContainerLowest = Color(0xFF140E0C),
    surfaceContainerLow = Color(0xFF221A17),
    surfaceContainer = Color(0xFF2A211E),
    surfaceContainerHigh = Color(0xFF342A26),
    surfaceContainerHighest = Color(0xFF3F3430)
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
