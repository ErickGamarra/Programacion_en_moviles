package com.gamarra.lab02carritokotlin

import java.util.Scanner

// ==========================================
// CONSTANTES TARIFARIAS Y REGLAS DE NEGOCIO
// ==========================================
const val TARIFA_MOTO = 2.0
const val TARIFA_AUTO = 4.0
const val TARIFA_CAMIONETA = 10.0

const val RECARGO_TRAMO_2 = 0.20 // 20% (Horas 3 a 5)
const val RECARGO_TRAMO_3 = 0.50 // 50% (Hora 6 en adelante)
const val DESCUENTO_FRECUENTE = 0.10 // 10%

// ==========================================
// MODELO DE DATOS
// ==========================================
data class RegistroHora(
    val hora: Int,
    val tarifaBase: Double,
    val porcentajeRecargo: Int,
    val recargoMonto: Double,
    val importe: Double
)

// ==========================================
// FUNCIONES DE ENTRADA DE DATOS (INPUTS)
// ==========================================

fun leerCliente(scanner: Scanner): String {
    while (true) {
        print("Ingrese el nombre del cliente: ")
        val entrada = scanner.nextLine().trim()
        if (entrada.isNotEmpty()) return entrada
        println("Dato incorrecto, inserte una cantidad válida")
    }
}

fun leerPlaca(scanner: Scanner): String {
    while (true) {
        print("Ingrese el número de placa: ")
        val entrada = scanner.nextLine().trim().uppercase()
        if (entrada.isNotEmpty()) return entrada
        println("Dato incorrecto, inserte una cantidad válida")
    }
}

fun leerTipoVehiculo(scanner: Scanner): Pair<String, Double> {
    while (true) {
        println("\n--- Selección de Tipo de Vehículo ---")
        println("1. Moto       (S/ $TARIFA_MOTO / hora)")
        println("2. Auto       (S/ $TARIFA_AUTO / hora)")
        println("3. Camioneta  (S/ $TARIFA_CAMIONETA / hora)")
        print("Seleccione una opción (1-3): ")

        val entrada = scanner.nextLine().trim()
        when (entrada.toIntOrNull()) {
            1 -> return Pair("MOTO", TARIFA_MOTO)
            2 -> return Pair("AUTO", TARIFA_AUTO)
            3 -> return Pair("CAMIONETA", TARIFA_CAMIONETA)
            else -> println("Dato incorrecto, inserte una cantidad válida")
        }
    }
}

fun leerHorasEstacionamiento(scanner: Scanner): Int {
    while (true) {
        print("\nIngrese las horas de permanencia (mínimo 1): ")
        val entrada = scanner.nextLine().trim()
        val horas = entrada.toIntOrNull()

        if (horas != null && horas >= 1) return horas
        println("Dato incorrecto, inserte una cantidad válida")
    }
}

fun leerClienteFrecuente(scanner: Scanner): Boolean {
    while (true) {
        print("\n¿Es cliente frecuente? (S/N): ")
        val entrada = scanner.nextLine().trim().uppercase()

        when (entrada) {
            "S" -> return true
            "N" -> return false
            else -> println("Dato incorrecto, inserte una cantidad válida")
        }
    }
}

// ==========================================
// FUNCIONES DE LÓGICA Y CÁLCULO
// ==========================================

fun calcularDesgloseHoras(horas: Int, tarifaBase: Double): List<RegistroHora> {
    val desglose = mutableListOf<RegistroHora>()

    for (h in 1..horas) {
        val (porcentaje, recargoMonto) = when {
            h <= 2 -> Pair(0, 0.0)
            h in 3..5 -> Pair(20, tarifaBase * RECARGO_TRAMO_2)
            else -> Pair(50, tarifaBase * RECARGO_TRAMO_3)
        }
        val importe = tarifaBase + recargoMonto
        desglose.add(RegistroHora(h, tarifaBase, porcentaje, recargoMonto, importe))
    }
    return desglose
}

fun calcularTotales(desglose: List<RegistroHora>, esFrecuente: Boolean): Triple<Double, Double, Double> {
    val subtotal = desglose.sumOf { it.importe }
    val descuento = if (esFrecuente) subtotal * DESCUENTO_FRECUENTE else 0.0
    val totalPagar = subtotal - descuento
    return Triple(subtotal, descuento, totalPagar)
}

// ==========================================
// EJECUCIÓN PRINCIPAL
// ==========================================

fun main() {
    val scanner = Scanner(System.`in`)

    println("==================================================")
    println("      SISTEMA DE CONTROL DE ESTACIONAMIENTO       ")
    println("==================================================")

    // 1. Ingreso de datos
    val cliente = leerCliente(scanner)
    val placa = leerPlaca(scanner)
    val (tipoVehiculo, tarifaBase) = leerTipoVehiculo(scanner)
    val horas = leerHorasEstacionamiento(scanner)
    val esFrecuente = leerClienteFrecuente(scanner)

    // 2. Procesamiento de cálculos por tramos
    val desglose = calcularDesgloseHoras(horas, tarifaBase)
    val (subtotal, descuento, totalPagar) = calcularTotales(desglose, esFrecuente)

    // 3. Comprobación y desglose de cálculo por hora
    println("\n---------------- DESGLOSE DE CÁLCULO ----------------")
    for (item in desglose) {
        println(
            "Hora %2d: Base S/ %5.2f | Recargo: %2d%% (+S/ %4.2f) | Importe: S/ %5.2f".format(
                item.hora,
                item.tarifaBase,
                item.porcentajeRecargo,
                item.recargoMonto,
                item.importe
            )
        )
    }
    println("-----------------------------------------------------")
    println("Subtotal acumulado : S/ %.2f".format(subtotal))
    println("Descuento frecuente: S/ %.2f (10%%)".format(descuento))
    println("Total liquidado    : S/ %.2f".format(totalPagar))
}