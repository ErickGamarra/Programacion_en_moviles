package com.tecsup.campusgps.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.tecsup.campusgps.R
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.model.UbicacionUi

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercanoId: Int?,
    radioMetros: Int,
    permisoConcedido: Boolean,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val oscuro = isSystemInDarkTheme()

    val estiloMapa = remember(oscuro) {
        if (oscuro) {
            MapStyleOptions.loadRawResourceStyle(contexto, R.raw.mapa_oscuro)
        } else {
            null
        }
    }

    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }

    // Cuando llega la ubicación, la cámara viaja hasta el usuario
    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            val destino = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            camara.animate(CameraUpdateFactory.newLatLngZoom(destino, 16f))
        }
    }

    // Al tocar una tarjeta, la cámara se acerca al lugar elegido
    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar ?: return@LaunchedEffect
        camara.animate(CameraUpdateFactory.newLatLngZoom(LatLng(lugar.latitud, lugar.longitud), 17f))
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara,
        properties = MapProperties(
            isMyLocationEnabled = permisoConcedido,
            mapStyleOptions = estiloMapa
        ),
        uiSettings = MapUiSettings(
            myLocationButtonEnabled = permisoConcedido,
            zoomControlsEnabled = false
        )
    ) {
        // Círculo de cercanía y marcador propio: rojo (el marcador por defecto)
        if (estado is UbicacionUi.Lista) {
            val centroUsuario = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            Circle(
                center = centroUsuario,
                radius = radioMetros.toDouble(),
                strokeColor = Color.Blue.copy(alpha = 0.5f),
                fillColor = Color.Blue.copy(alpha = 0.12f)
            )
            Marker(
                state = rememberUpdatedMarkerState(
                    position = centroUsuario
                ),
                title = "Estás aquí"
            )
        }

        // Azul para los lugares y verde para el más cercano; el composable solo dibuja lo que recibe
        lugares.forEach { item ->
            val lugar = item.lugar
            key(lugar.id) {
                Marker(
                    state = rememberUpdatedMarkerState(
                        position = LatLng(lugar.latitud, lugar.longitud)
                    ),
                    title = lugar.nombre,
                    snippet = lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(
                        if (lugar.id == masCercanoId) BitmapDescriptorFactory.HUE_GREEN
                        else BitmapDescriptorFactory.HUE_AZURE
                    )
                )
            }
        }
    }
}
