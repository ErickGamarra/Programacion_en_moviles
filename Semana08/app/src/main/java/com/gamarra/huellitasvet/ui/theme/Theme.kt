package com.gamarra.huellitasvet.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.gamarra.huellitasvet.model.Rol

private val EsquemaDuenoClaro = lightColorScheme(
    primary = VerdeDueno,
    onPrimary = Color.White,
    primaryContainer = VerdeContenedor,
    onPrimaryContainer = Color(0xFF064E3B),
    background = VerdeFondo,
    surface = Color.White
)

private val EsquemaVetClaro = lightColorScheme(
    primary = AzulVet,
    onPrimary = Color.White,
    primaryContainer = AzulContenedor,
    onPrimaryContainer = Color(0xFF1E3A8A),
    background = AzulFondo,
    surface = Color.White
)

private val EsquemaDuenoOscuro = darkColorScheme(
    primary = Color(0xFF34D399),
    onPrimary = Color(0xFF022C22),
    primaryContainer = Color(0xFF065F46),
    onPrimaryContainer = VerdeContenedor
)

private val EsquemaVetOscuro = darkColorScheme(
    primary = Color(0xFF60A5FA),
    onPrimary = Color(0xFF0B1F4D),
    primaryContainer = Color(0xFF1E40AF),
    onPrimaryContainer = AzulContenedor
)

fun esquemaVet(rol: Rol?, oscuro: Boolean): ColorScheme = when {
    rol == Rol.ADMIN && oscuro -> EsquemaVetOscuro
    rol == Rol.ADMIN -> EsquemaVetClaro
    oscuro -> EsquemaDuenoOscuro
    else -> EsquemaDuenoClaro
}

@Composable
fun VetTheme(
    rol: Rol? = null,
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = esquemaVet(rol, darkTheme),
        typography = Typography,
        content = content
    )
}
