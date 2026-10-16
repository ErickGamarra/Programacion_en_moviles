package com.gamarra.huellitasvet

import android.app.Application
import com.gamarra.huellitasvet.data.local.DatosIniciales
import com.gamarra.huellitasvet.data.local.VetDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VetApp : Application() {
    val database: VetDatabase by lazy { VetDatabase.obtener(this) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch { DatosIniciales.sembrar(database) }
    }
}
