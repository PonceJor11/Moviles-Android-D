package com.ponce.avisoscampus.ui

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.ponce.avisoscampus.model.PermisoUi
import com.ponce.avisoscampus.notificacion.Notificador
import com.ponce.avisoscampus.receiver.ModoAvionReceiver
import com.ponce.avisoscampus.ui.theme.GrisFondo
import com.ponce.avisoscampus.ui.theme.GrisNeutro
import com.ponce.avisoscampus.ui.theme.Marron
import com.ponce.avisoscampus.ui.theme.MarronClaro
import com.ponce.avisoscampus.ui.theme.MarronOscuro
import com.ponce.avisoscampus.ui.theme.RojoError
import com.ponce.avisoscampus.ui.theme.RojoFondo
import com.ponce.avisoscampus.ui.theme.VerdeFondo
import com.ponce.avisoscampus.ui.theme.VerdeOk
import com.ponce.avisoscampus.viewmodel.AvisosViewModel

private const val SEGUNDOS_RECORDATORIO = 10

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current
    val avisos by vm.avisos.collectAsState()
    val permiso by vm.permiso.collectAsState()
    val faltan by vm.faltan.collectAsState()
    val escuchando by vm.escuchandoAvion.collectAsState()
    val mensaje by vm.mensaje.collectAsState()

    // Paso 2: permiso
    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido -> vm.permisoRespondido(concedido) }

    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) vm.permisoRespondido(true)
    }

    // Paso 3 y 8: el ViewModel pide, la pantalla (con Context) muestra
    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            if (!mostrado) vm.noSePudoMostrar()
        }
    }

    // Paso 6: registrar y quitar el receptor según el Switch
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

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("AvisosCampus", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Encabezado(totalAvisos = avisos.size, permiso = permiso)

            // Permiso
            SeccionCard("Permiso de notificaciones", Icons.Filled.Lock) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Estado actual",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    EtiquetaPermiso(permiso)
                }
                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            vm.permisoRespondido(true)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Permitir notificaciones") }
            }

            // Mensaje de error (Paso 8)
            mensaje?.let { texto ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    border = BorderStroke(1.dp, RojoError.copy(alpha = 0.25f))
                ) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(Icons.Filled.Warning, contentDescription = null, tint = RojoError)
                            Text(texto, color = RojoError, style = MaterialTheme.typography.bodyMedium)
                        }
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                                    .putExtra(Settings.EXTRA_APP_PACKAGE, contexto.packageName)
                                contexto.startActivity(intent)
                            },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, RojoError),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = RojoError)
                        ) { Text("Abrir ajustes") }
                    }
                }
            }

            // Avisos
            SeccionCard("Avisos", Icons.Filled.Notifications) {
                Text(
                    "Genera una notificación al instante y queda anotada en el historial.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(
                    onClick = { vm.avisarAhora() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Mostrar aviso") }
            }

            // Recordatorio
            SeccionCard("Recordatorio", Icons.Filled.DateRange) {
                val restante = faltan
                Text(
                    if (restante != null) "Faltan $restante s" else "Sin recordatorio activo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (restante != null) MarronOscuro else MaterialTheme.colorScheme.onSurfaceVariant
                )
                LinearProgressIndicator(
                    progress = {
                        if (restante == null) 0f
                        else (SEGUNDOS_RECORDATORIO - restante) / SEGUNDOS_RECORDATORIO.toFloat()
                    },
                    modifier = Modifier.fillMaxWidth().clip(CircleShape),
                    color = Marron,
                    trackColor = MaterialTheme.colorScheme.primaryContainer
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { vm.recordarEn(SEGUNDOS_RECORDATORIO) },
                        enabled = restante == null,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Recordar en 10 s") }
                    OutlinedButton(
                        onClick = { vm.cancelarRecordatorio() },
                        enabled = restante != null,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if (restante != null) Marron else MarronClaro.copy(alpha = 0.5f))
                    ) { Text("Cancelar") }
                }
            }

            // Receptor
            SeccionCard("Receptor de anuncios", Icons.Filled.Phone) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Escuchar el modo avión", fontWeight = FontWeight.Medium)
                        Text(
                            if (escuchando) "Escuchando cambios del sistema" else "Receptor apagado",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = escuchando,
                        onCheckedChange = { vm.cambiarEscucha(it) },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = Marron,
                            checkedThumbColor = Color.White
                        )
                    )
                }
            }

            // Historial
            HistorialAvisos(avisos)
        }
    }
}

/** Tarjeta contenedora con ícono y título; la comparten todas las secciones. */
@Composable
internal fun SeccionCard(
    titulo: String,
    icono: ImageVector,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icono, contentDescription = null, tint = Marron, modifier = Modifier.size(20.dp))
                }
                Text(
                    titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Marron
                )
            }
            contenido()
        }
    }
}

@Composable
private fun Encabezado(totalAvisos: Int, permiso: PermisoUi) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(Color(0xFFB88A66), Color(0xFF8B5E3C))))
            .padding(22.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                "Centro de avisos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                "Notificaciones locales y escucha del sistema, todo en un solo lugar.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
            Text(
                "$totalAvisos ${if (totalAvisos == 1) "aviso registrado" else "avisos registrados"}" +
                    if (permiso is PermisoUi.Concedido) " · notificaciones activas" else "",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White
            )
        }
    }
}

@Composable
private fun EtiquetaPermiso(permiso: PermisoUi) {
    val (texto, fondo, color) = when (permiso) {
        PermisoUi.SinPedir -> Triple("Sin pedir", GrisFondo, GrisNeutro)
        PermisoUi.Concedido -> Triple("Permitido", VerdeFondo, VerdeOk)
        PermisoUi.Denegado -> Triple("Denegado", RojoFondo, RojoError)
    }
    Surface(shape = CircleShape, color = fondo) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(Modifier.size(8.dp).background(color, CircleShape))
            Text(texto, color = color, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelLarge)
        }
    }
}
