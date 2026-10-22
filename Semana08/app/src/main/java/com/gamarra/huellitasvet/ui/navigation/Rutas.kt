package com.gamarra.huellitasvet.ui.navigation

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val INICIO = "inicio"
    const val MASCOTAS = "mascotas"
    const val FICHA = "ficha/{id}"
    const val FORMULARIO = "formulario?id={id}"
    const val PEDIR_CITA = "pedirCita?mascotaId={mascotaId}"
    const val PANEL = "panel"

    fun ficha(id: Int) = "ficha/$id"
    fun formulario(id: Int? = null) = if (id == null) "formulario" else "formulario?id=$id"
    fun pedirCita(mascotaId: Int? = null) = if (mascotaId == null) "pedirCita" else "pedirCita?mascotaId=$mascotaId"
}
