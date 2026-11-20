package com.ponce.campusgps.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.ponce.campusgps.model.UbicacionUi
import com.ponce.campusgps.util.formatearDistancia
import com.ponce.campusgps.viewmodel.MapaViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugaresVisibles.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val categoriaActiva by vm.categoriaActiva.collectAsState()
    val radioActivo by vm.radioActivo.collectAsState()
    val idsEnRadio by vm.idsEnRadio.collectAsState()

    val contexto = LocalContext.current

    // Estado del permiso para la capa nativa del mapa; se vuelve a leer al regresar
    // a la app (por ejemplo, después de concederlo desde Ajustes).
    var permisoConcedido by remember { mutableStateOf(tienePermisoUbicacion(contexto)) }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        permisoConcedido = tienePermisoUbicacion(contexto)
    }

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        permisoConcedido = resultado.values.any { it }
        if (permisoConcedido) vm.buscar() else vm.permisoDenegado()
    }

    fun alPulsar() {
        if (tienePermisoUbicacion(contexto)) {
            vm.buscar()
        } else {
            lanzador.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CampusGPS") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                MapaUbicacion(
                    estado = estado,
                    lugares = lugares,
                    seleccionado = seleccionado,
                    masCercano = masCercano,
                    radioMetros = radioActivo,
                    permisoConcedido = permisoConcedido,
                    modifier = Modifier.fillMaxSize()
                )
                FiltrosMapa(
                    categorias = vm.categorias,
                    categoriaActiva = categoriaActiva,
                    onCategoria = { vm.filtrarPorCategoria(it) },
                    radios = vm.radios,
                    radioActivo = radioActivo,
                    onRadio = { vm.cambiarRadio(it) },
                    mostrarRadios = estado is UbicacionUi.Lista,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                (estado as? UbicacionUi.Lista)?.let { lista ->
                    Text(
                        text = String.format(
                            Locale.US, "Lat: %.5f   Lon: %.5f",
                            lista.posicion.latitud, lista.posicion.longitud
                        ),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Button(
                    onClick = { alPulsar() },
                    enabled = estado !is UbicacionUi.Buscando
                ) {
                    Text(if (estado is UbicacionUi.Buscando) "Buscando…" else "Obtener mi ubicación")
                }

                (estado as? UbicacionUi.Error)?.let { error ->
                    MensajeError(error = error, onReintentar = { alPulsar() })
                }

                OutlinedButton(
                    onClick = { masCercano?.let { vm.seleccionar(it.id) } },
                    enabled = masCercano != null
                ) {
                    Text("Ir al más cercano")
                }

                masCercano?.let { cercano ->
                    val metros = lugares.firstOrNull { it.lugar.id == cercano.id }?.metros
                    if (metros != null) {
                        Text(
                            text = "Más cercano: ${cercano.nombre} (${formatearDistancia(metros)})",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            ListaLugares(
                lugares = lugares,
                seleccionado = seleccionado,
                masCercanoId = masCercano?.id,
                dentroDelRadio = idsEnRadio,
                onClick = { vm.seleccionar(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }
    }
}

private fun tienePermisoUbicacion(contexto: Context): Boolean =
    listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ).any {
        ContextCompat.checkSelfPermission(contexto, it) == PackageManager.PERMISSION_GRANTED
    }
