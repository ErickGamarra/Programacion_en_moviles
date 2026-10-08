package com.saludplus.citas.ui.auth

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.saludplus.citas.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.CampoTexto

@Composable
fun RegistroScreen(onRegistroExitoso: () -> Unit, onVerTerminos: () -> Unit, onVolver: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var intentado by remember { mutableStateOf(false) }
    var errorRegistro by remember { mutableStateOf<String?>(null) }

    val nombreValido = nombre.isNotBlank()
    val correoValido = Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()
    val telefonoValido = telefono.length == 9 && telefono.all { it.isDigit() }
    val contrasenaValida = contrasena.length >= 6

    val errorNombre = if (intentado && !nombreValido) "Ingresa tu nombre" else null
    val errorCorreo = if (intentado && !correoValido) "Ingresa un correo válido" else null
    val errorTelefono = if (intentado && !telefonoValido) "El teléfono debe tener 9 dígitos" else null
    val errorContrasena = if (intentado && !contrasenaValida) "Mínimo 6 caracteres" else null

    Scaffold(
        topBar = { BarraSuperior("Crear cuenta", onVolver) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CampoTexto(nombre, { nombre = it }, "Nombre completo", errorNombre)
            CampoTexto(correo, { correo = it; errorRegistro = null }, "Correo", errorCorreo, tipoTeclado = KeyboardType.Email)
            CampoTexto(telefono, { telefono = it }, "Teléfono", errorTelefono, tipoTeclado = KeyboardType.Phone)
            CampoTexto(contrasena, { contrasena = it }, "Contraseña", errorContrasena, esContrasena = true)

            if (errorRegistro != null) {
                Text(errorRegistro!!, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    intentado = true
                    val valido = nombreValido && correoValido && telefonoValido && contrasenaValida
                    if (valido) {
                        if (Repositorio.registrarUsuario(nombre, correo, telefono, contrasena)) {
                            onRegistroExitoso()
                        } else {
                            errorRegistro = "Ya existe una cuenta con ese correo"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }

            TextButton(onClick = onVerTerminos, modifier = Modifier.fillMaxWidth()) {
                Text("Ver términos y condiciones")
            }
        }
    }
}
