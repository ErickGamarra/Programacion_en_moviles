package com.gamarra.huellitasvet

import android.app.Application
import com.gamarra.huellitasvet.data.local.DatosIniciales
import com.gamarra.huellitasvet.data.local.VetDatabase
import com.gamarra.huellitasvet.data.prefs.PreferenciasStore
import com.gamarra.huellitasvet.data.repository.VetRepository
import com.gamarra.huellitasvet.data.repository.VetRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VetApp : Application() {
    val database: VetDatabase by lazy { VetDatabase.obtener(this) }
    val preferencias: PreferenciasStore by lazy { PreferenciasStore(this) }
    val repository: VetRepository by lazy { VetRepositoryRoom(database) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch { DatosIniciales.sembrar(database) }
    }
}
