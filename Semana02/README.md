// Laboratorio 02 - Carrito de Compras en Kotlin

Erick Gamarra  
Programacion en Moviles

// Es un carrito de compras en consola hecho con Kotlin. Registra productos, saca el subtotal, el IGV y el total a pagar, muestra el producto mas caro y aplica un descuento con when dependiendo del monto total.

// Funciones que se hicieron
- Producto: data class para guardar el nombre, precio y cantidad.
- calcularSubtotal: suma los precios por cantidad de todos los productos.
- calcularIGV: saca el 18% del subtotal.
- calcularTotal: suma el subtotal y el IGV.
- mostrarDetalle: imprime la tabla de productos ordenada en columnas con 2 decimales.
- maxByOrNull: busca el producto con el precio mas alto.
- calcularDescuento: da 5% de descuento si pasa los 3000 y 10% si pasa los 5000.

// Diferencia entre val y var
- val: no cambia, se usa para valores fijos que no se van a modificar una vez creados (como el nombre o el precio).
- var: si cambia, se usa para variables a las que se les puede reasignar un nuevo valor (como la cantidad o los acumuladores).

