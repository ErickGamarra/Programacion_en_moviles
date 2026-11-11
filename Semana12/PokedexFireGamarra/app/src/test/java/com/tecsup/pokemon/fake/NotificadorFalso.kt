package com.tecsup.pokemon.fake

import com.tecsup.pokemon.notification.Notificador

// Guarda los avisos en una lista en vez de mostrarlos.
class NotificadorFalso : Notificador {
    val mostrados = mutableListOf<Pair<String, String>>()
    override fun mostrar(titulo: String, texto: String) {
        mostrados += titulo to texto
    }
}
