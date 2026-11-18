package com.tecsup.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tecsup.campusgps.model.UbicacionUi
import com.tecsup.campusgps.util.formatearDistancia
import com.tecsup.campusgps.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val lugaresVisibles by vm.lugaresVisibles.collectAsState()
    val categoriaSeleccionada by vm.categoria.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val radio by vm.radio.collectAsState()
    val siguiendo by vm.siguiendo.collectAsState()

    val contexto = LocalContext.current
    var accionPendiente by remember { mutableStateOf<(() -> Unit)?>(null) }

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) {
            accionPendiente?.invoke()
            accionPendiente = null
        } else {
            vm.permisoDenegado()
            accionPendiente = null
        }
    }

    fun tienePermiso(permiso: String) =
        ContextCompat.checkSelfPermission(contexto, permiso) == PackageManager.PERMISSION_GRANTED

    fun ejecutarConPermiso(accion: () -> Unit) {
        if (tienePermiso(FINE) || tienePermiso(COARSE)) {
            accion()
        } else {
            accionPendiente = accion
            lanzador.launch(arrayOf(FINE, COARSE))
        }
    }

    fun alPulsar() {
        ejecutarConPermiso { vm.buscar() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CampusGPS", fontWeight = FontWeight.Bold) },
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
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FiltroCategorias(
                categorias = vm.categorias,
                seleccionada = categoriaSeleccionada,
                onSeleccionar = { vm.filtrarPor(it) },
                modifier = Modifier.fillMaxWidth()
            )

            // El mapa ocupa todo el espacio libre de arriba
            MapaUbicacion(
                estado = estado,
                lugares = lugaresVisibles,
                seleccionado = seleccionado,
                masCercanoId = masCercano?.id,
                radioMetros = radio,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Button(
                    onClick = { alPulsar() },
                    enabled = estado !is UbicacionUi.Buscando
                ) {
                    Text("Obtener mi ubicación")
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { masCercano?.let { vm.seleccionar(it.id) } },
                        enabled = masCercano != null
                    ) {
                        Text("Ir al más cercano")
                    }

                    FilledTonalButton(
                        onClick = {
                            if (siguiendo) {
                                vm.detenerSeguimiento()
                            } else {
                                ejecutarConPermiso { vm.iniciarSeguimiento() }
                            }
                        }
                    ) {
                        Text(if (siguiendo) "Detener seguimiento" else "Iniciar seguimiento")
                    }
                }

                if (siguiendo) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color.Red, shape = CircleShape)
                        )
                        Text(
                            text = "En vivo",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                SelectorRadio(
                    radios = vm.radiosDisponibles,
                    seleccionado = radio,
                    onCambiar = { vm.cambiarRadio(it) }
                )

                masCercano?.let { cercano ->
                    // Se busca en la lista completa: el más cercano puede estar oculto por el filtro
                    val metros = lugares.firstOrNull { it.lugar.id == cercano.id }?.metros
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Más cercano: ") }
                            append(cercano.nombre)
                            metros?.let { append(" (${formatearDistancia(it)})") }
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                when (val e = estado) {
                    UbicacionUi.Inicial -> Text("Pulsa el botón para empezar")
                    UbicacionUi.Buscando -> CircularProgressIndicator()
                    is UbicacionUi.Lista -> Unit
                    is UbicacionUi.Error -> MensajeError(
                        error = e,
                        onReintentar = { alPulsar() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Lista con altura fija debajo del mapa
            ListaLugares(
                lugares = lugaresVisibles,
                seleccionado = seleccionado,
                masCercanoId = masCercano?.id,
                radioMetros = radio,
                onClick = { vm.seleccionar(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }
    }
}

private const val FINE = Manifest.permission.ACCESS_FINE_LOCATION
private const val COARSE = Manifest.permission.ACCESS_COARSE_LOCATION
