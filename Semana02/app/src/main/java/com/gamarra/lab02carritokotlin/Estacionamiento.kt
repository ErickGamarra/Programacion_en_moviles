package com.gamarra.lab02carritokotlin

import java.util.Scanner

// ==========================================
// CONSTANTES TARIFARIAS BASE
// ==========================================
const val TARIFA_MOTO = 2.0
const val TARIFA_AUTO = 4.0
const val TARIFA_CAMIONETA = 10.0

// ==========================================
// FUNCIONES DE ENTRADA DE DATOS (INPUTS)
// ==========================================

fun leerCliente(scanner: Scanner): String {
    while (true) {
        print("Ingrese el nombre del cliente: ")
        val entrada = scanner.nextLine().trim()
        if (entrada.isNotEmpty()) {
            return entrada
        }
        println("Dato incorrecto, inserte una cantidad válida")
    }
}

fun leerPlaca(scanner: Scanner): String {
    while (true) {
        print("Ingrese el número de placa: ")
        val entrada = scanner.nextLine().trim().uppercase()
        if (entrada.isNotEmpty()) {
            return entrada
        }
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

        if (horas != null && horas >= 1) {
            return horas
        }
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
// EJECUCIÓN PRINCIPAL (COMMIT 1)
// ==========================================

fun main() {
    val scanner = Scanner(System.`in`)

    println("==================================================")
    println("      SISTEMA DE CONTROL DE ESTACIONAMIENTO       ")
    println("==================================================")

    // Captura de datos
    val cliente: String = leerCliente(scanner)
    val placa: String = leerPlaca(scanner)
    val (tipoVehiculo: String, tarifaBase: Double) = leerTipoVehiculo(scanner)
    val horas: Int = leerHorasEstacionamiento(scanner)
    val esFrecuente: Boolean = leerClienteFrecuente(scanner)

    // Confirmación de captura
    println("\n[REGISTRO COMPLETADO]")
    println("Cliente: $cliente | Placa: $placa | Tipo: $tipoVehiculo | Tarifa Base: S/ $tarifaBase | Horas: $horas | Frecuente: $esFrecuente")
}