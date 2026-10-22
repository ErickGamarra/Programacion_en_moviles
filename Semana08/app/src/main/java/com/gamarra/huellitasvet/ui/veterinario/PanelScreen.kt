package com.gamarra.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gamarra.huellitasvet.viewmodel.SesionViewModel

@Composable
fun PanelScreen(sesion: SesionViewModel) {
    val usuario by sesion.usuarioActual.collectAsState()
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Panel de ${usuario?.nombre ?: ""}")
    }
}
