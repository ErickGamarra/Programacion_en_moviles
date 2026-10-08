package com.saludplus.citas.navigation

object Rutas {
    const val ARG_ESPECIALIDAD_ID = "especialidadId"
    const val ARG_MEDICO_ID = "medicoId"
    const val ARG_FECHA = "fecha"
    const val ARG_HORA = "hora"
    const val ARG_CITA_ID = "citaId"

    const val SPLASH = "splash"
    const val REGISTRO = "registro"
    const val LOGIN = "login"
    const val TERMINOS = "terminos"
    const val HOME = "home"

    const val ESPECIALIDADES = "especialidades"
    const val MEDICOS = "medicos/{$ARG_ESPECIALIDAD_ID}"
    const val FECHA_HORA = "fechahora/{$ARG_MEDICO_ID}"
    const val CONFIRMAR_CITA = "confirmar/{$ARG_MEDICO_ID}/{$ARG_FECHA}/{$ARG_HORA}"
    const val CITA_EXITOSA = "citaexitosa"

    const val MIS_CITAS = "miscitas"
    const val DETALLE_CITA = "detallecita/{$ARG_CITA_ID}"
    const val PERFIL = "perfil"
    const val RESULTADOS = "resultados"
    const val NOTIFICACIONES = "notificaciones"

    fun medicos(especialidadId: Int) = "medicos/$especialidadId"
    fun fechaHora(medicoId: Int) = "fechahora/$medicoId"
    fun confirmarCita(medicoId: Int, fecha: String, hora: String) = "confirmar/$medicoId/$fecha/$hora"
    fun detalleCita(citaId: Int) = "detallecita/$citaId"
}
