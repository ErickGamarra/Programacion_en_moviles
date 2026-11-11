package com.tecsup.pokemon

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.tecsup.pokemon.ui.navigation.AppNav
import com.tecsup.pokemon.ui.theme.PokedexFireGamarraTheme

class MainActivity : ComponentActivity() {

    // Pide el permiso de notificaciones (Android 13+). Si lo niegan, la app sigue igual.
    private val pedirPermiso = registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && savedInstanceState == null) {
            pedirPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        val container = AppContainer.obtener(applicationContext)
        setContent {
            PokedexFireGamarraTheme {
                AppNav(container)
            }
        }
    }
}
