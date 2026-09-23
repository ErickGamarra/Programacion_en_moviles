package com.gamarra.tecsupfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gamarra.tecsupfit.ui.MainScreen
import com.gamarra.tecsupfit.ui.theme.TECSUPfitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TECSUPfitTheme {
                MainScreen()
            }
        }
    }
}