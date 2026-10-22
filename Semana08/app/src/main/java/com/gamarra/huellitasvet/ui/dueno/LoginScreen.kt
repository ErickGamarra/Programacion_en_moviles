package com.gamarra.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamarra.huellitasvet.model.Rol
import com.gamarra.huellitasvet.ui.components.VetButton
import com.gamarra.huellitasvet.ui.components.VetTextField
import com.gamarra.huellitasvet.ui.theme.VetTheme
import com.gamarra.huellitasvet.viewmodel.SesionViewModel

@Composable
fun LoginScreen(vm: SesionViewModel, onLogin: (Rol) -> Unit) {
    var rol by remember { mutableStateOf(Rol.CLIENTE) }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    val error by vm.error.collectAsState()
    val oscuro by vm.modoOscuro.collectAsState()

    VetTheme(rol = rol, darkTheme = oscuro) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 48.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Bienvenido", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text("Ingresa para continuar", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))

            val opciones = listOf(Rol.CLIENTE to "Dueño", Rol.ADMIN to "Veterinario")
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                opciones.forEachIndexed { indice, (valor, texto) ->
                    SegmentedButton(
                        selected = rol == valor,
                        onClick = {
                            rol = valor
                            vm.limpiarError()
                        },
                        shape = SegmentedButtonDefaults.itemShape(indice, opciones.size),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = MaterialTheme.colorScheme.primary,
                            activeContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) { Text(texto) }
                }
            }

            VetTextField(
                valor = correo,
                onCambio = { correo = it; vm.limpiarError() },
                etiqueta = "Correo",
                icono = Icons.Outlined.Email,
                tipoTeclado = KeyboardType.Email
            )
            VetTextField(
                valor = clave,
                onCambio = { clave = it; vm.limpiarError() },
                etiqueta = "Contraseña",
                icono = Icons.Outlined.Lock,
                esClave = true,
                error = error
            )
            VetButton(texto = "Ingresar", onClick = { vm.login(correo, clave, rol, onLogin) })
        }
    }
}
