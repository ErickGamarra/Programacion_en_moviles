package com.tecsup.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.centrotareas.model.CargaUi
import com.tecsup.centrotareas.model.PrimosUi
import com.tecsup.centrotareas.ui.theme.VerdeExito
import com.tecsup.centrotareas.ui.theme.VerdeExitoTexto
import com.tecsup.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()
    val primos by vm.primos.collectAsState()
    val carga by vm.carga.collectAsState()
    var textoN by rememberSaveable { mutableStateOf("$N_POR_DEFECTO") }
    // Prueba de vida: si deja de sumar, la pantalla está congelada
    var toques by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Tarjeta("Tarea con progreso") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(onClick = { toques++ }) { Text("Tócame: $toques") }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { vm.tareaLarga() }, enabled = !corriendo) { Text("Iniciar") }
                    OutlinedButton(onClick = { vm.cancelar() }, enabled = corriendo) { Text("Cancelar") }
                }
                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("${(progreso * 100).toInt()} %")
                Text(mensaje)
            }
            Tarjeta("Contar números primos") {
                val calculando = primos is PrimosUi.Calculando
                // Solo se acepta un número entero desde 2
                val n = textoN.toIntOrNull()?.takeIf { it >= 2 }
                OutlinedTextField(
                    value = textoN,
                    onValueChange = { nuevo -> textoN = nuevo.filter { it.isDigit() }.take(9) },
                    label = { Text("Contar primos hasta") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = !calculando,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { n?.let { vm.contar(it) } }, enabled = !calculando && n != null) {
                        Text("Contar")
                    }
                    OutlinedButton(onClick = { vm.cancelarPrimos() }, enabled = calculando) {
                        Text("Cancelar")
                    }
                    if (calculando) CircularProgressIndicator(Modifier.padding(start = 4.dp))
                }
                when (val p = primos) {
                    PrimosUi.Inicial -> Text("Escribe un número y pulsa Contar")
                    PrimosUi.Calculando -> Text("Calculando...")
                    is PrimosUi.Listo -> Recuadro(
                        "Primos encontrados: ${p.cantidad}\nTiempo: ${p.milisegundos} ms",
                        VerdeExito, VerdeExitoTexto
                    )
                    PrimosUi.Cancelado -> Text("Cancelado")
                }
            }
            Tarjeta("Dos cargas") {
                val cargando = carga is CargaUi.Cargando
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(onClick = { vm.cargarUnaPorUna() }, enabled = !cargando) {
                        Text("Una por una")
                    }
                    Button(onClick = { vm.cargarALaVez() }, enabled = !cargando) {
                        Text("A la vez")
                    }
                    if (cargando) CircularProgressIndicator(Modifier.padding(start = 4.dp))
                }
                when (val c = carga) {
                    CargaUi.Inicial -> Text("Elige cómo cargar el perfil y los pedidos")
                    CargaUi.Cargando -> Text("Cargando...")
                    is CargaUi.Exito -> Recuadro(
                        "Modo: ${c.modo}\nPerfil: ${c.perfil}\nPedidos: ${c.pedidos}\nTiempo: ${c.milisegundos} ms",
                        VerdeExito, VerdeExitoTexto
                    )
                    is CargaUi.Error -> Text(c.mensaje, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun Tarjeta(titulo: String, contenido: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                titulo,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            contenido()
        }
    }
}

// Recuadro de resultado (verde) o de error (rojo)
@Composable
fun Recuadro(texto: String, fondo: Color, colorTexto: Color) {
    Surface(color = fondo, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
        Text(texto, color = colorTexto, modifier = Modifier.padding(12.dp))
    }
}

// Valor por defecto: en el emulador tarda unos segundos, suficiente para ver que no se congela
private const val N_POR_DEFECTO = 2_500_000
