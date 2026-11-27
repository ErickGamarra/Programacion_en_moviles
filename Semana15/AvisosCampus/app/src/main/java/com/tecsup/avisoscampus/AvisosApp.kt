package com.tecsup.avisoscampus

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.tecsup.avisoscampus.data.AvisosRepository
import com.tecsup.avisoscampus.data.AvisosRepositoryDataStore
import com.tecsup.avisoscampus.notificacion.Notificador
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

// Un solo DataStore por archivo en toda la app
private val Context.almacenAvisos: DataStore<Preferences> by preferencesDataStore(name = "avisos")

class AvisosApp : Application() {

    // Alcance de la app: los guardados terminan aunque se cierre la pantalla
    private val alcance = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // Un solo Repository para toda la app
    val repositorio: AvisosRepository by lazy {
        AvisosRepositoryDataStore(almacenAvisos, alcance)
    }

    override fun onCreate() {
        super.onCreate()
        // El canal se crea una sola vez, antes de que se abra cualquier pantalla
        Notificador.crearCanal(this)
    }
}
