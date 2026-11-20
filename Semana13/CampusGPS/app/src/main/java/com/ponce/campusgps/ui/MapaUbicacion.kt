package com.ponce.campusgps.ui

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
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.ponce.campusgps.R
import com.ponce.campusgps.model.Lugar
import com.ponce.campusgps.model.LugarConDistancia
import com.ponce.campusgps.model.UbicacionUi
import kotlin.math.cos

private const val ZOOM_CIUDAD = 12f
private const val ZOOM_USUARIO = 16f
private const val ZOOM_LUGAR = 17f
private const val RELLENO_LIMITES_PX = 96
private const val METROS_POR_GRADO = 111_320.0
private val ColorRadio = Color(0xFF1E88E5)

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercano: Lugar?,
    radioMetros: Int?,
    permisoConcedido: Boolean,
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), ZOOM_CIUDAD)
    }

    // Cuando llega la ubicación (o cambia el radio) la cámara se encuadra sobre el usuario.
    LaunchedEffect(estado, radioMetros) {
        if (estado is UbicacionUi.Lista) {
            val centro = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            val ajusteSimple = CameraUpdateFactory.newLatLngZoom(centro, ZOOM_USUARIO)
            if (radioMetros == null) {
                camara.animate(ajusteSimple)
            } else {
                try {
                    camara.animate(
                        CameraUpdateFactory.newLatLngBounds(
                            limitesDelRadio(centro, radioMetros),
                            RELLENO_LIMITES_PX
                        )
                    )
                } catch (e: IllegalStateException) {
                    // El mapa aún no tiene tamaño: se usa el zoom por defecto.
                    camara.animate(ajusteSimple)
                }
            }
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

    // El punto azul nativo y su botón solo se activan con el permiso concedido;
    // sin él, Google Maps lanzaría una SecurityException.
    // El estilo oscuro del mapa acompaña al tema del sistema.
    val contexto = LocalContext.current
    val modoOscuro = isSystemInDarkTheme()
    val propiedades = remember(permisoConcedido, modoOscuro) {
        MapProperties(
            isMyLocationEnabled = permisoConcedido,
            mapStyleOptions = if (modoOscuro) {
                MapStyleOptions.loadRawResourceStyle(contexto, R.raw.mapa_oscuro)
            } else {
                null
            }
        )
    }
    val ajustes = remember(permisoConcedido) {
        MapUiSettings(myLocationButtonEnabled = permisoConcedido, zoomControlsEnabled = true)
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara,
        properties = propiedades,
        uiSettings = ajustes
    ) {
        if (estado is UbicacionUi.Lista) {
            Marker(
                state = rememberMarkerState (
                    position = LatLng(estado.posicion.latitud, estado.posicion.longitud)
                ),
                title = "Estás aquí"
            )
            if (radioMetros != null) {
                Circle(
                    center = LatLng(estado.posicion.latitud, estado.posicion.longitud),
                    radius = radioMetros.toDouble(),
                    fillColor = ColorRadio.copy(alpha = 0.18f),
                    strokeColor = ColorRadio,
                    strokeWidth = 4f
                )
            }
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

/** Rectángulo que contiene el círculo de [metros] alrededor de [centro]. */
private fun limitesDelRadio(centro: LatLng, metros: Int): LatLngBounds {
    val deltaLatitud = metros / METROS_POR_GRADO
    val deltaLongitud = metros / (METROS_POR_GRADO * cos(Math.toRadians(centro.latitude)))
    return LatLngBounds(
        LatLng(centro.latitude - deltaLatitud, centro.longitude - deltaLongitud),
        LatLng(centro.latitude + deltaLatitud, centro.longitude + deltaLongitud)
    )
}

@Composable
@GoogleMapComposable
private fun MarcadorLugar(lugar: Lugar, esMasCercano: Boolean, seleccionado: Boolean) {
    val marcador = rememberMarkerState(position = LatLng(lugar.latitud, lugar.longitud))
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
