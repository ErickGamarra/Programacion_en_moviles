package com.tecsup.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.tecsup.campusgps.model.Posicion
import kotlinx.coroutines.tasks.await

/**
 * Implementación con FusedLocationProviderClient.
 * Es la única clase que conoce Google Play Services; el ViewModel solo ve la interface.
 */
class UbicacionRepositoryGoogle(context: Context) : UbicacionRepository {

    private val cliente = LocationServices.getFusedLocationProviderClient(context.applicationContext)

    // El permiso se pide en la pantalla antes de llamar a buscar();
    // si falta, getCurrentLocation lanza SecurityException y la devolvemos como failure.
    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> = runCatching {
        val token = CancellationTokenSource()
        val location = cliente
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, token.token)
            .await()
            ?: error("No se pudo obtener la ubicación")
        Posicion(location.latitude, location.longitude)
    }
}
