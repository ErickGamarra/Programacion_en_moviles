package com.gamarra.lab02carritokotlin

// PARTE 1: DEFINICIÓN DEL MODELO DE DATOS
data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

// PARTE 3: FUNCIONES DE CÁLCULO
fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0
    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }
    return subtotal
}

fun calcularIGV(subtotal: Double): Double {
    return subtotal * 0.18
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}

// PARTE 4: REPORTE CON FORMATO
fun mostrarDetalle(productos: List<Producto>) {
    println("----------------- DETALLE DEL CARRITO -----------------")
    var i = 1
    for (p in productos) {
        val importe = p.precio * p.cantidad
        println(String.format("%d. %-20s x%-2d S/ %8.2f", i, p.nombre, p.cantidad, importe))
        i++
    }
    println("-------------------------------------------------------")
}

// PARTE 5: LÓGICA ADICIONAL (DESCUENTO CON WHEN)
fun calcularDescuento(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}

// RETO ADICIONAL: BUSCAR PRODUCTO CON FIND
fun buscarProducto(productos: List<Producto>, nombre: String): Producto? {
    return productos.find { it.nombre.equals(nombre, ignoreCase = true) }
}

// FUNCIÓN PRINCIPAL (MAIN)
fun main() {
    // PARTE 2: INICIALIZACIÓN Y REGISTRO DE PRODUCTOS
    println("=======================================================")
    println("          CARRITO DE COMPRAS - TIENDA TECSUP           ")
    println("=======================================================")

    val nombreCliente = "Erick Gamarra"
    val carrito = mutableListOf<Producto>()

    println("Cliente: $nombreCliente")

    carrito.add(Producto("Laptop HP", 2500.0, 1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Audifonos Sony", 120.0, 1))
    carrito.add(Producto("USB Kingston 64GB", 25.0, 3))

    println("Cantidad de productos: ${carrito.size}\n")

    // PARTE 4: MOSTRAR REPORTE ALINEADO
    mostrarDetalle(carrito)

    // PARTE 5: PRODUCTO MÁS CARO
    val masCaro = carrito.maxByOrNull { it.precio }
    if (masCaro != null) {
        println("Producto mas caro: ${masCaro.nombre} " + String.format("(S/ %.2f)", masCaro.precio))
        println("-------------------------------------------------------")
    }

    // PARTE 3 & 4: CÁLCULOS Y TOTALES FORMATEADOS
    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    println(String.format("%-25s S/ %8.2f", "Subtotal:", subtotal))
    println(String.format("%-25s S/ %8.2f", "IGV (18%):", igv))
    println(String.format("%-25s S/ %8.2f", "Total:", total))

    // PARTE 5: CÁLCULO Y REPORTE DE DESCUENTO
    val descuento = calcularDescuento(total)
    val totalConDescuento = total - descuento

    val textoDescuento = when {
        total > 5000 -> "Descuento (10%):"
        total > 3000 -> "Descuento (5%):"
        else -> "Descuento (0%):"
    }

    println(String.format("%-25s S/ %8.2f", textoDescuento, descuento))
    println("=======================================================")
    println(String.format("%-25s S/ %8.2f", "TOTAL CON DESCUENTO:", totalConDescuento))
    println("=======================================================")

    // ==========================================
    // RETO ADICIONAL: BUSCAR Y ELIMINAR
    // ==========================================
    println("\n----------------- RETO ADICIONAL -----------------")

    // 1. Buscar producto
    val productoABuscar = "Mouse Logitech"
    val encontrado = buscarProducto(carrito, productoABuscar)
    if (encontrado != null) {
        println("Producto encontrado: ${encontrado.nombre} (Precio: S/ ${encontrado.precio}, Cantidad: ${encontrado.cantidad})")
    } else {
        println("Producto no encontrado.")
    }

    // 2. Eliminar producto con removeIf
    println("\nEliminando '$productoABuscar' del carrito...")
    carrito.removeIf { it.nombre.equals(productoABuscar, ignoreCase = true) }

    // 3. Mostrar carrito y totales actualizados
    println("\nCarrito actualizado tras la eliminacion:")
    mostrarDetalle(carrito)

    val subtotalActualizado = calcularSubtotal(carrito)
    val igvActualizado = calcularIGV(subtotalActualizado)
    val totalActualizado = calcularTotal(subtotalActualizado, igvActualizado)
    val descuentoActualizado = calcularDescuento(totalActualizado)
    val finalActualizado = totalActualizado - descuentoActualizado

    println(String.format("%-25s S/ %8.2f", "Subtotal:", subtotalActualizado))
    println(String.format("%-25s S/ %8.2f", "IGV (18%):", igvActualizado))
    println(String.format("%-25s S/ %8.2f", "Total:", totalActualizado))
    println(String.format("%-25s S/ %8.2f", "Descuento:", descuentoActualizado))
    println("=======================================================")
    println(String.format("%-25s S/ %8.2f", "NUEVO TOTAL A PAGAR:", finalActualizado))
    println("=======================================================")
}