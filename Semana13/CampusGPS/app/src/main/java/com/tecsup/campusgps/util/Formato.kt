package com.tecsup.campusgps.util

import java.util.Locale

/** Menos de 1000 m: "350 m". Desde 1000 m: "1.5 km" (un decimal). */
fun formatearDistancia(metros: Float): String =
    if (metros < 1000f) {
        "${metros.toInt()} m"
    } else {
        String.format(Locale.US, "%.1f km", metros / 1000f)
    }
