package com.tecsup.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
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

    private val appContext = context.applicationContext
    private val cliente = LocationServices.getFusedLocationProviderClient(appContext)
    private val locationManager = appContext.getSystemService(LocationManager::class.java)

    // El permiso se pide en la pantalla antes de llamar a buscar();
    // si falta, getCurrentLocation lanza SecurityException y la devolvemos como failure.
    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> = runCatching {
        // Con la ubicación del sistema apagada, Fused devuelve null: avisamos antes con un error claro
        if (!LocationManagerCompat.isLocationEnabled(locationManager)) {
            throw UbicacionNoDisponibleException()
        }
        val token = CancellationTokenSource()
        val location = cliente
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, token.token)
            .await()
            ?: throw UbicacionNoDisponibleException()
        Posicion(location.latitude, location.longitude)
    }

    override fun playServicesDisponible(): Boolean =
        GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(appContext) == ConnectionResult.SUCCESS
}
