package com.gamarra.lab02carritokotlin

import java.util.Scanner

// ==========================================
// CONSTANTES TARIFARIAS Y REGLAS DE NEGOCIO
// ==========================================
const val TARIFA_MOTO = 2.0
const val TARIFA_AUTO = 4.0
const val TARIFA_CAMIONETA = 10.0
const val TARIFA_TRAILER = 20.0

// Tramos estándar (Moto, Auto, Camioneta)
const val RECARGO_ESTANDAR_TRAMO_2 = 0.20 // 20% (Horas 3 a 5)
const val RECARGO_ESTANDAR_TRAMO_3 = 0.50 // 50% (Hora 6 en adelante)

// Tramos específicos para Tráiler
const val RECARGO_TRAILER_TRAMO_2 = 0.20 // 20% (Horas 3 a 5)
const val RECARGO_TRAILER_TRAMO_3 = 0.40 // 40% (Horas 6 a 10)
const val RECARGO_TRAILER_TRAMO_4 = 0.50 // 50% (Hora 11 en adelante)

// Reglas de descuento e impuestos
const val DESCUENTO_FRECUENTE = 0.10          // 10%
const val UMBRAL_DESCUENTO_GLOBAL = 500.0     // Superar los S/ 500.00
const val DESCUENTO_GLOBAL_TASA = 0.20        // 20%
const val TASA_ITV = 0.18                     // 18%

// ==========================================
// MODELOS DE DATOS
// ==========================================
data class RegistroHora(
    val hora: Int,
    val tarifaBase: Double,
    val porcentajeRecargo: Int,
    val recargoMonto: Double,
    val importe: Double
)

data class ResumenLiquidacion(
    val subtotalBruto: Double,
    val descuentoFrecuente: Double,
    val descuentoGlobal: Double,
    val subtotalNeto: Double,
    val impuestoITV: Double,
    val totalPagar: Double
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
        val (porcentaje, recargoMonto) = if (tipoVehiculo == "TRAILER") {
            when {
                h <= 2 -> Pair(0, 0.0)
                h in 3..5 -> Pair(20, tarifaBase * RECARGO_TRAILER_TRAMO_2)
                h in 6..10 -> Pair(40, tarifaBase * RECARGO_TRAILER_TRAMO_3)
                else -> Pair(50, tarifaBase * RECARGO_TRAILER_TRAMO_4)
            }
        } else {
            when {
                h <= 2 -> Pair(0, 0.0)
                h in 3..5 -> Pair(20, tarifaBase * RECARGO_ESTANDAR_TRAMO_2)
                else -> Pair(50, tarifaBase * RECARGO_ESTANDAR_TRAMO_3)
            }
        }

        val importe = tarifaBase + recargoMonto
        desglose.add(RegistroHora(h, tarifaBase, porcentaje, recargoMonto, importe))
    }
    return desglose
}

fun calcularLiquidacion(desglose: List<RegistroHora>, esFrecuente: Boolean): ResumenLiquidacion {
    val subtotalBruto = desglose.sumOf { it.importe }
    val descuentoFrecuente = if (esFrecuente) subtotalBruto * DESCUENTO_FRECUENTE else 0.0
    val baseParaDescuentoGlobal = subtotalBruto - descuentoFrecuente

    // Descuento global del 20% si el total supera los S/ 500.00
    val descuentoGlobal = if (baseParaDescuentoGlobal > UMBRAL_DESCUENTO_GLOBAL) {
        baseParaDescuentoGlobal * DESCUENTO_GLOBAL_TASA
    } else {
        0.0
    }

    val subtotalNeto = baseParaDescuentoGlobal - descuentoGlobal
    val impuestoITV = subtotalNeto * TASA_ITV
    val totalPagar = subtotalNeto + impuestoITV

    return ResumenLiquidacion(
        subtotalBruto = subtotalBruto,
        descuentoFrecuente = descuentoFrecuente,
        descuentoGlobal = descuentoGlobal,
        subtotalNeto = subtotalNeto,
        impuestoITV = impuestoITV,
        totalPagar = totalPagar
    )
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
    liquidacion: ResumenLiquidacion
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
    println(String.format("%-40s | S/ %-9.2f", "SUBTOTAL BRUTO", liquidacion.subtotalBruto))

    if (esFrecuente) {
        println(String.format("%-40s | -S/ %-8.2f", "DESCUENTO CLIENTE FRECUENTE (10%)", liquidacion.descuentoFrecuente))
    }

    if (liquidacion.descuentoGlobal > 0.0) {
        println(String.format("%-40s | -S/ %-8.2f", "DESCUENTO GLOBAL VOLUMEN > S/500 (20%)", liquidacion.descuentoGlobal))
    }

    println(String.format("%-40s | S/ %-9.2f", "SUBTOTAL NETO (BASE IMPONIBLE)", liquidacion.subtotalNeto))
    println(String.format("%-40s | S/ %-9.2f", "IMPUESTO ITV (18%)", liquidacion.impuestoITV))
    println("==================================================================")
    println(String.format("%-40s | S/ %-9.2f", "TOTAL FINAL A PAGAR", liquidacion.totalPagar))
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
    val liquidacion = calcularLiquidacion(desglose, esFrecuente)

    // 3. Renderizado final
    mostrarComprobante(cliente, placa, tipoVehiculo, tarifaBase, horas, esFrecuente, desglose, liquidacion)
}
