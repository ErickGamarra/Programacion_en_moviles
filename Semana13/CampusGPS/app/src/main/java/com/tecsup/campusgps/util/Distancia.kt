package com.tecsup.campusgps.util

import android.location.Location
import com.tecsup.campusgps.model.Lugar
import com.tecsup.campusgps.model.Posicion

/**
 * Distancia en línea recta (metros) entre el usuario y un lugar.
 * Location.distanceBetween recibe (lat1, lon1, lat2, lon2, resultado) y deja el valor en resultado[0].
 */
fun distanciaMetros(desde: Posicion, hasta: Lugar): Float {
    val resultado = FloatArray(1)
    Location.distanceBetween(
        desde.latitud, desde.longitud,
        hasta.latitud, hasta.longitud,
        resultado
    )
    return resultado[0]
}
