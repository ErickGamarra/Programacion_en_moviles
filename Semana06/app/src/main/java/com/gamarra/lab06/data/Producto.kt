package com.gamarra.lab06.data

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double
) {
    val precioFormateado: String
        get() = "S/ %.2f".format(precio)
}