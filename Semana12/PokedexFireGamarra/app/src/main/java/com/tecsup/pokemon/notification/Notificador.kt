package com.tecsup.pokemon.notification

// El ViewModel pide la notificación con esta interface, sin conocer el Context.
interface Notificador {
    fun mostrar(titulo: String, texto: String)
}
