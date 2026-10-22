package com.gamarra.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gamarra.huellitasvet.VetApp

class VetViewModelFactory(private val app: VetApp) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(SesionViewModel::class.java) ->
            SesionViewModel(app.repository, app.preferencias) as T
        modelClass.isAssignableFrom(InicioViewModel::class.java) ->
            InicioViewModel(app.repository) as T
        modelClass.isAssignableFrom(MascotaViewModel::class.java) ->
            MascotaViewModel(app.repository) as T
        modelClass.isAssignableFrom(PedirCitaViewModel::class.java) ->
            PedirCitaViewModel(app.repository) as T
        modelClass.isAssignableFrom(MisCitasViewModel::class.java) ->
            MisCitasViewModel(app.repository) as T
        modelClass.isAssignableFrom(PanelViewModel::class.java) ->
            PanelViewModel(app.repository) as T
        modelClass.isAssignableFrom(AgendaViewModel::class.java) ->
            AgendaViewModel(app.repository) as T
        else -> throw IllegalArgumentException("ViewModel desconocido")
    }
}
