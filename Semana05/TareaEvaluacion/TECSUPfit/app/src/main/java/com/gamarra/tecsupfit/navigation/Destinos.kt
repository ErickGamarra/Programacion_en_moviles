package com.gamarra.tecsupfit.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class DestinoBottomBar(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
) {
    object Inicio : DestinoBottomBar("inicio", "Inicio", Icons.Outlined.Home)
    object Reservas : DestinoBottomBar("reservas", "Reservas", Icons.Outlined.CheckCircleOutline)
    object Rutinas : DestinoBottomBar("rutinas", "Rutinas", Icons.Outlined.FitnessCenter)
    object Perfil : DestinoBottomBar("perfil", "Perfil", Icons.Outlined.AccountCircle)
}

object DestinosSecuenciales {
    const val DETALLE_CLASE = "detalle/{claseId}"
    const val CONFIRMACION = "confirmacion/{claseId}/{horario}"

    fun detalleClase(claseId: String) = "detalle/$claseId"
    fun confirmacion(claseId: String, horario: String) = "confirmacion/$claseId/$horario"
}