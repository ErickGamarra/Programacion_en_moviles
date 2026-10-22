package com.gamarra.huellitasvet.model

import java.time.LocalTime

object Horarios {
    val delDia: List<String> = (0 until 18).map { LocalTime.of(9, 0).plusMinutes(30L * it).toString() }
}
