package com.gamarra.huellitasvet.ui.components

import com.gamarra.huellitasvet.model.Especie
import com.gamarra.huellitasvet.model.Sexo
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val espanol = Locale.forLanguageTag("es-PE")

fun nombreEspecie(especie: String) = when (especie) {
    Especie.PERRO.name -> "Perro"
    Especie.GATO.name -> "Gato"
    else -> "Otro"
}

fun nombreSexo(sexo: String) = if (sexo == Sexo.MACHO.name) "Macho" else "Hembra"

fun edadTexto(anioNacimiento: Int): String {
    val anios = LocalDate.now().year - anioNacimiento
    return when {
        anios < 1 -> "Menos de 1 año"
        anios == 1 -> "1 año"
        else -> "$anios años"
    }
}

fun pesoTexto(peso: Double) = "${"%.1f".format(espanol, peso)} kg"

fun precioTexto(precio: Double) = "S/ ${"%.2f".format(espanol, precio)}"

fun fechaLarga(fecha: String): String =
    LocalDate.parse(fecha).format(DateTimeFormatter.ofPattern("d MMM yyyy", espanol))
