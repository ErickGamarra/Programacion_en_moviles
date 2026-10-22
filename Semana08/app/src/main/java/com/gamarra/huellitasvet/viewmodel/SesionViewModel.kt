package com.gamarra.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamarra.huellitasvet.data.local.entity.Usuario
import com.gamarra.huellitasvet.data.prefs.PreferenciasStore
import com.gamarra.huellitasvet.data.repository.VetRepository
import com.gamarra.huellitasvet.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: VetRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {
    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val notificaciones: StateFlow<Boolean> = prefs.notificaciones
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val aceptarCitas: StateFlow<Boolean> = prefs.aceptarCitas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun cambiarModoOscuro(valor: Boolean) {
        viewModelScope.launch { prefs.guardarModoOscuro(valor) }
    }

    fun cambiarNotificaciones(valor: Boolean) {
        viewModelScope.launch { prefs.guardarNotificaciones(valor) }
    }

    fun cambiarAceptarCitas(valor: Boolean) {
        viewModelScope.launch { prefs.guardarAceptarCitas(valor) }
    }

    fun login(correo: String, clave: String, rolSeleccionado: Rol, onExito: (Rol) -> Unit) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Completa el correo y la contraseña"
            return
        }
        viewModelScope.launch {
            val usuario = repo.login(correo.trim(), clave)
            when {
                usuario == null -> _error.value = "Correo o contraseña incorrectos"
                usuario.rol != rolSeleccionado.name ->
                    _error.value = "Este usuario no pertenece al perfil seleccionado"
                else -> {
                    prefs.guardarSesion(usuario.rol, usuario.id)
                    _usuarioActual.value = usuario
                    _error.value = null
                    onExito(rolSeleccionado)
                }
            }
        }
    }

    suspend fun destinoInicial(): Rol? {
        val perfil = prefs.perfilActivo.first()
        if (perfil.isBlank()) return null
        val usuario = repo.obtenerUsuario(prefs.usuarioId.first())
        if (usuario == null) {
            prefs.cerrarSesion()
            return null
        }
        _usuarioActual.value = usuario
        return Rol.valueOf(perfil)
    }

    fun cerrarSesion(onCerrada: () -> Unit) {
        viewModelScope.launch {
            prefs.cerrarSesion()
            _usuarioActual.value = null
            onCerrada()
        }
    }

    fun cambiarPerfil(onCambio: (Rol) -> Unit) {
        val actual = _usuarioActual.value?.let { Rol.valueOf(it.rol) } ?: return
        val otro = if (actual == Rol.CLIENTE) Rol.ADMIN else Rol.CLIENTE
        viewModelScope.launch {
            prefs.guardarSesion(otro.name, 0)
            _usuarioActual.value = null
            _error.value = null
            onCambio(otro)
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}
