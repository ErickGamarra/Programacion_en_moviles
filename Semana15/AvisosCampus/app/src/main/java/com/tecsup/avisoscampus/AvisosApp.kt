package com.tecsup.avisoscampus

import android.app.Application
import com.tecsup.avisoscampus.notificacion.Notificador

class AvisosApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // El canal se crea una sola vez, antes de que se abra cualquier pantalla
        Notificador.crearCanal(this)
    }
}
