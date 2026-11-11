package com.tecsup.pokemon.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.tecsup.pokemon.AppContainer
import com.tecsup.pokemon.data.auth.AuthRepository
import com.tecsup.pokemon.domain.model.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado de la pantalla de login
data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val errorCorreo: String? = null,
    val errorClave: String? = null,
    val errorGeneral: String? = null,
    val cargando: Boolean = false,
    val revisandoSesion: Boolean = true,     // al abrir la app se busca una sesión guardada
    val sesion: Sesion? = null               // distinto de null = ya ingresó
)

class LoginViewModel(
    private val auth: AuthRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado.asStateFlow()

    init {
        // Si ya había sesión, entra directo
        viewModelScope.launch {
            val sesion = auth.sesionActual()
            _estado.update { it.copy(revisandoSesion = false, sesion = sesion) }
        }
    }

    fun cambiarCorreo(valor: String) =
        _estado.update { it.copy(correo = valor, errorCorreo = null, errorGeneral = null) }

    fun cambiarClave(valor: String) =
        _estado.update { it.copy(clave = valor, errorClave = null, errorGeneral = null) }

    fun ingresar() {
        val actual = _estado.value
        val correo = actual.correo.trim()
        // Validación de campos vacíos: si falla, no se llama a Firebase
        val errorCorreo = if (correo.isEmpty()) "Ingresa tu correo" else null
        val errorClave = if (actual.clave.isEmpty()) "Ingresa tu contraseña" else null
        if (errorCorreo != null || errorClave != null) {
            _estado.update { it.copy(errorCorreo = errorCorreo, errorClave = errorClave) }
            return
        }
        viewModelScope.launch {
            _estado.update { it.copy(cargando = true, errorGeneral = null) }
            auth.iniciarSesion(correo, actual.clave)
                .onSuccess { sesion -> _estado.update { it.copy(cargando = false, sesion = sesion) } }
                .onFailure { e -> _estado.update { it.copy(cargando = false, errorGeneral = mensajeDe(e)) } }
        }
    }

    fun cerrarSesion() {
        auth.cerrarSesion()
        _estado.value = LoginUiState(revisandoSesion = false)
    }

    private fun mensajeDe(e: Throwable) = when (e) {
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException -> "Correo o contraseña incorrectos"
        is FirebaseNetworkException -> "No hay conexión a internet"
        else -> "No se pudo iniciar sesión. Intenta de nuevo"
    }

    companion object {
        fun factory(container: AppContainer) = viewModelFactory {
            initializer { LoginViewModel(container.authRepository) }
        }
    }
}
