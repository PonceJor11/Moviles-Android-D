package com.ponce.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.ponce.campusgps.model.Lugar
import com.ponce.campusgps.model.LugarConDistancia
import com.ponce.campusgps.model.UbicacionUi

private const val ZOOM_CIUDAD = 12f
private const val ZOOM_USUARIO = 16f
private const val ZOOM_LUGAR = 17f

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercano: Lugar?,
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), ZOOM_CIUDAD)
    }

    // Cuando llega la ubicación, la cámara viaja hasta el usuario.
    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            val destino = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            camara.animate(CameraUpdateFactory.newLatLngZoom(destino, ZOOM_USUARIO))
        }
    }

    // Cuando se selecciona un lugar (lista o botón), la cámara viaja hasta él.
    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar
            ?: return@LaunchedEffect
        camara.animate(
            CameraUpdateFactory.newLatLngZoom(LatLng(lugar.latitud, lugar.longitud), ZOOM_LUGAR)
        )
    }

    GoogleMap(modifier = modifier, cameraPositionState = camara) {
        if (estado is UbicacionUi.Lista) {
            Marker(
                state = rememberUpdatedMarkerState(
                    position = LatLng(estado.posicion.latitud, estado.posicion.longitud)
                ),
                title = "Estás aquí"
            )
        }

        lugares.forEach { item ->
            key(item.lugar.id) {
                MarcadorLugar(
                    lugar = item.lugar,
                    esMasCercano = item.lugar.id == masCercano?.id,
                    seleccionado = item.lugar.id == seleccionado
                )
            }
        }
    }
}

@Composable
@GoogleMapComposable
private fun MarcadorLugar(lugar: Lugar, esMasCercano: Boolean, seleccionado: Boolean) {
    val marcador = rememberUpdatedMarkerState(position = LatLng(lugar.latitud, lugar.longitud))
    val icono = remember(esMasCercano) {
        val tono = if (esMasCercano) BitmapDescriptorFactory.HUE_GREEN else BitmapDescriptorFactory.HUE_AZURE
        BitmapDescriptorFactory.defaultMarker(tono)
    }

    Marker(
        state = marcador,
        title = lugar.nombre,
        snippet = lugar.categoria,
        icon = icono
    )

    LaunchedEffect(seleccionado) {
        if (seleccionado) marcador.showInfoWindow()
    }
}
