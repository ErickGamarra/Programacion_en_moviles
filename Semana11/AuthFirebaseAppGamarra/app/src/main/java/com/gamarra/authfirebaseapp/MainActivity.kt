package com.gamarra.authfirebaseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.gamarra.authfirebaseapp.ui.theme.AuthFirebaseAppGamarraTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Firebase
        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()
        setContent {
            AuthFirebaseAppGamarraTheme {
                Surface(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
                    AuthApp() // Llamamos a nuestra app con navegación
                }
            }
        }
    }
}
