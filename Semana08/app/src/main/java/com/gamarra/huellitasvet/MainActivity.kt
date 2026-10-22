package com.gamarra.huellitasvet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gamarra.huellitasvet.model.Rol
import com.gamarra.huellitasvet.ui.navigation.NavVet
import com.gamarra.huellitasvet.ui.theme.VetTheme
import com.gamarra.huellitasvet.viewmodel.SesionViewModel
import com.gamarra.huellitasvet.viewmodel.VetViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val factory = VetViewModelFactory(application as VetApp)
        setContent {
            val sesion: SesionViewModel = viewModel(factory = factory)
            val oscuro by sesion.modoOscuro.collectAsState()
            val usuario by sesion.usuarioActual.collectAsState()
            VetTheme(rol = usuario?.let { Rol.valueOf(it.rol) }, darkTheme = oscuro) {
                NavVet(sesion, factory)
            }
        }
    }
}
