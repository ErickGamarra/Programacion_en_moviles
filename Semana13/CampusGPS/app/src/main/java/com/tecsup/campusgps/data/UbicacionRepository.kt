package com.tecsup.campusgps.data

import com.tecsup.campusgps.model.Posicion

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>

    /** true si el dispositivo tiene Google Play Services listo para usar. */
    fun playServicesDisponible(): Boolean
}

/** El GPS está apagado o el sistema no devolvió ninguna ubicación. */
class UbicacionNoDisponibleException : Exception("No se pudo obtener la ubicación. Activa el GPS e intenta de nuevo")
