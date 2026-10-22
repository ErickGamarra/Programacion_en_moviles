package com.gamarra.huellitasvet.ui.navigation

import com.gamarra.huellitasvet.model.Rol

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login?rol={rol}"
    const val INICIO = "inicio"
    const val MASCOTAS = "mascotas"
    const val CITAS = "citas"
    const val PERFIL = "perfil"
    const val FICHA = "ficha/{id}"
    const val FORMULARIO = "formulario?id={id}"
    const val PEDIR_CITA = "pedirCita?mascotaId={mascotaId}"
    const val PANEL = "panel"
    const val AGENDA = "agenda"
    const val PACIENTES = "pacientes"
    const val PACIENTE = "paciente/{id}"
    const val ATENDER = "atender/{citaId}"
    const val SERVICIOS = "servicios"
    const val SERVICIO = "servicio?id={id}"
    const val AJUSTES = "ajustes"

    fun login(rol: Rol = Rol.CLIENTE) = "login?rol=${rol.name}"
    fun ficha(id: Int) = "ficha/$id"
    fun formulario(id: Int? = null) = if (id == null) "formulario" else "formulario?id=$id"
    fun paciente(id: Int) = "paciente/$id"
    fun atender(citaId: Int) = "atender/$citaId"
    fun servicio(id: Int? = null) = if (id == null) "servicio" else "servicio?id=$id"
    fun pedirCita(mascotaId: Int? = null) = if (mascotaId == null) "pedirCita" else "pedirCita?mascotaId=$mascotaId"
}
