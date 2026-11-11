package com.tecsup.pokemon.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.R
import com.tecsup.pokemon.domain.model.Sesion
import com.tecsup.pokemon.ui.components.Cargando

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onIngreso: (Sesion) -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    // Cuando hay sesión se pasa al catálogo
    LaunchedEffect(estado.sesion) {
        estado.sesion?.let(onIngreso)
    }

    if (estado.revisandoSesion || estado.sesion != null) {
        Cargando("Verificando sesión...")
        return
    }

    var verClave by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.background)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(48.dp))
            Surface(shape = RoundedCornerShape(32.dp), color = Color.White.copy(alpha = 0.18f)) {
                Image(
                    painter = painterResource(R.drawable.ic_llama),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(18.dp)
                        .size(72.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "Pokédex de Fuego",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                "Ingresa para ver los Pokémon de tipo fuego",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
            Spacer(Modifier.height(28.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Iniciar sesión", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = estado.correo,
                        onValueChange = viewModel::cambiarCorreo,
                        label = { Text("Correo") },
                        leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                        isError = estado.errorCorreo != null,
                        supportingText = estado.errorCorreo?.let { { Text(it) } },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = estado.clave,
                        onValueChange = viewModel::cambiarClave,
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { verClave = !verClave }) {
                                Icon(
                                    if (verClave) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                    contentDescription = if (verClave) "Ocultar contraseña" else "Ver contraseña"
                                )
                            }
                        },
                        visualTransformation = if (verClave) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = estado.errorClave != null,
                        supportingText = estado.errorClave?.let { { Text(it) } },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { viewModel.ingresar() }),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (estado.errorGeneral != null) {
                        Spacer(Modifier.height(8.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                estado.errorGeneral!!,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = viewModel::ingresar,
                        enabled = !estado.cargando,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        if (estado.cargando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Ingresar", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}
