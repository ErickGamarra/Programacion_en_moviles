package com.gamarra.lab02carritokotlin

import java.util.Scanner

// ==========================================
// CONSTANTES TARIFARIAS Y REGLAS DE NEGOCIO
// ==========================================
const val TARIFA_MOTO = 2.0
const val TARIFA_AUTO = 4.0
const val TARIFA_CAMIONETA = 10.0
const val TARIFA_TRAILER = 20.0 // Nueva categoría incorporada

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
        println("4. Tráiler    (S/ $TARIFA_TRAILER / hora)")
        print("Seleccione una opción (1-4): ")

        val entrada = scanner.nextLine().trim()
        when (entrada.toIntOrNull()) {
            1 -> return Pair("MOTO", TARIFA_MOTO)
            2 -> return Pair("AUTO", TARIFA_AUTO)
            3 -> return Pair("CAMIONETA", TARIFA_CAMIONETA)
            4 -> return Pair("TRAILER", TARIFA_TRAILER)
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

fun calcularDesgloseHoras(tipoVehiculo: String, horas: Int, tarifaBase: Double): List<RegistroHora> {
    val desglose = mutableListOf<RegistroHora>()

    for (h in 1..horas) {
        // Para Tráiler se mantiene tarifa plana (0% recargo) hasta definir su modelo en el siguiente commit
        val (porcentaje, recargoMonto) = if (tipoVehiculo == "TRAILER") {
            Pair(0, 0.0)
        } else {
            when {
                h <= 2 -> Pair(0, 0.0)
                h in 3..5 -> Pair(20, tarifaBase * RECARGO_TRAMO_2)
                else -> Pair(50, tarifaBase * RECARGO_TRAMO_3)
            }
        }

        val importe = tarifaBase + recargoMonto
        desglose.add(RegistroHora(h, tarifaBase, porcentaje, recargoMonto, importe))
    }
    return desglose
}

fun calcularLiquidacion(desglose: List<RegistroHora>, esFrecuente: Boolean): Triple<Double, Double, Double> {
    val subtotal = desglose.sumOf { it.importe }
    val descuento = if (esFrecuente) subtotal * DESCUENTO_FRECUENTE else 0.0
    val totalPagar = subtotal - descuento
    return Triple(subtotal, descuento, totalPagar)
}

// ==========================================
// FUNCIONES DE REPORTE Y VISUALIZACIÓN
// ==========================================

fun mostrarComprobante(
    cliente: String,
    placa: String,
    tipoVehiculo: String,
    tarifaBase: Double,
    horas: Int,
    esFrecuente: Boolean,
    desglose: List<RegistroHora>,
    subtotal: Double,
    descuento: Double,
    totalPagar: Double
) {
    println("\n==================================================================")
    println("              RESUMEN DE LIQUIDACIÓN DE SERVICIO                  ")
    println("==================================================================")
    println(String.format("%-25s : %s", "Cliente", cliente))
    println(String.format("%-25s : %s", "Placa de Vehículo", placa))
    println(String.format("%-25s : %s", "Tipo de Vehículo", tipoVehiculo))
    println(String.format("%-25s : %d horas", "Tiempo Total", horas))
    println(String.format("%-25s : %s", "Cliente Frecuente", if (esFrecuente) "SÍ (10% Desc.)" else "NO"))
    println("==================================================================")

    println("TARIFA BÁSICA: (S/ %.2f POR %s)".format(tarifaBase, tipoVehiculo))
    println("------------------------------------------------------------------")
    println(String.format("%-8s | %-14s | %-14s | %-12s", "Hora", "Tarifa Base", "Recargo (%)", "Importe"))
    println("------------------------------------------------------------------")

    for (item in desglose) {
        println(
            String.format(
                "%-8d | S/ %-11.2f | %-13s | S/ %-9.2f",
                item.hora,
                item.tarifaBase,
                "${item.porcentajeRecargo}%",
                item.importe
            )
        )
    }

    println("------------------------------------------------------------------")
    println(String.format("%-40s | S/ %-9.2f", "TOTAL IMPORTE (SUBTOTAL)", subtotal))
    if (esFrecuente) {
        println(String.format("%-40s | -S/ %-8.2f", "DESCUENTO CLIENTE FRECUENTE (10%)", descuento))
    }
    println("==================================================================")
    println(String.format("%-40s | S/ %-9.2f", "TOTAL FINAL A PAGAR", totalPagar))
    println("==================================================================")
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

    // 2. Procesamiento de cálculos
    val desglose = calcularDesgloseHoras(tipoVehiculo, horas, tarifaBase)
    val (subtotal, descuento, totalPagar) = calcularLiquidacion(desglose, esFrecuente)

    // 3. Renderizado final
    mostrarComprobante(cliente, placa, tipoVehiculo, tarifaBase, horas, esFrecuente, desglose, subtotal, descuento, totalPagar)
}