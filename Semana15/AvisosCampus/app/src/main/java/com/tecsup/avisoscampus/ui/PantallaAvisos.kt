package com.tecsup.avisoscampus.ui

import androidx.compose.foundation.BorderStroke
import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.model.PermisoUi
import com.tecsup.avisoscampus.notificacion.Notificador
import com.tecsup.avisoscampus.receiver.ModoAvionReceiver
import com.tecsup.avisoscampus.ui.theme.BordeTarjeta
import com.tecsup.avisoscampus.ui.theme.GrisSinPedir
import com.tecsup.avisoscampus.ui.theme.RojoDenegado
import com.tecsup.avisoscampus.ui.theme.VerdePermitido
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current
    val permiso by vm.permiso.collectAsState()
    val faltan by vm.faltan.collectAsState()
    val escuchando by vm.escuchandoAvion.collectAsState()
    val avisos by vm.avisos.collectAsState()

    // El receptor solo está registrado mientras el switch está activo
    DisposableEffect(escuchando) {
        if (escuchando) {
            val receptor = ModoAvionReceiver { activo -> vm.alCambiarModoAvion(activo) }
            ContextCompat.registerReceiver(
                contexto,
                receptor,
                IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            onDispose { contexto.unregisterReceiver(receptor) }
        } else {
            onDispose { }
        }
    }

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        vm.permisoRespondido(concedido)
    }

    // Si el permiso ya estaba concedido (por ejemplo, al volver a abrir la app), se refleja al iniciar
    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) vm.permisoRespondido(true)
    }

    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            // TODO Paso 8: si no se mostró, avisar al ViewModel
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("AvisosCampus", fontWeight = FontWeight.Bold) },
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
            Tarjeta("Permiso de notificaciones") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Estado: ")
                    EtiquetaPermiso(permiso)
                }
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        vm.permisoRespondido(true)
                    }
                    }
                ) {
                    Text("Permitir notificaciones", fontWeight = FontWeight.Bold)
                }
            }
            Tarjeta("Avisos") {
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = { vm.avisarAhora() }
                ) {
                    Text("Mostrar aviso", fontWeight = FontWeight.Bold)
                }
            }
            Tarjeta("Recordatorio") {
                val corriendo = faltan != null
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    Button(onClick = { vm.recordarEn(10) }, enabled = !corriendo) {
                        Text("Recordar en 10 s", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = { vm.cancelarRecordatorio() },
                        enabled = corriendo,
                        border = BorderStroke(
                            1.5.dp,
                            if (corriendo) MaterialTheme.colorScheme.primary else BordeTarjeta
                        )
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.Bold)
                    }
                }
                faltan?.let { s ->
                    Text(
                        "Faltan $s s",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
            Tarjeta("Receptor de anuncios") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Escuchar el modo avión", modifier = Modifier.weight(1f))
                    Switch(checked = escuchando, onCheckedChange = { vm.cambiarEscucha(it) })
                }
                Text(
                    "Activa o desactiva el modo avión del celular",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            HistorialAvisos(avisos)
        }
    }
}

@Composable
fun Tarjeta(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, BordeTarjeta)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            contenido()
        }
    }
}

@Composable
fun EtiquetaPermiso(permiso: PermisoUi) {
    val (texto, color) = when (permiso) {
        PermisoUi.SinPedir -> "Sin pedir" to GrisSinPedir
        PermisoUi.Concedido -> "Permitido" to VerdePermitido
        PermisoUi.Denegado -> "Denegado" to RojoDenegado
    }
    // Etiqueta suave: fondo claro y texto del mismo color
    Text(
        texto,
        color = color,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 2.dp)
    )
}
