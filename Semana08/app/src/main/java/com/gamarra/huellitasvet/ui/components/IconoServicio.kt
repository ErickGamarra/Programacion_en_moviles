package com.gamarra.huellitasvet.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.ui.graphics.vector.ImageVector

val iconosServicio = listOf("heart", "syringe", "scissors")

fun iconoServicio(icono: String): ImageVector = when (icono) {
    "heart" -> Icons.Filled.Favorite
    "syringe" -> Icons.Filled.Vaccines
    "scissors" -> Icons.Filled.ContentCut
    else -> Icons.Filled.MedicalServices
}
