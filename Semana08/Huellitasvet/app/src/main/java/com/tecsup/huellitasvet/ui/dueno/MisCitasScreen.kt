package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.viewmodel.MisCitasViewModel

private val ColorVerdePrincipal = Color(0xFF00966D)
private val ColorVerdeClaro = Color(0xFFE8F5E9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCitasScreen(
    vm: MisCitasViewModel,
    onPedirCita: () -> Unit
) {
    var pestañaSeleccionada by remember { mutableIntStateOf(0) } // 0: Próximas, 1: Historial
    val proximas by vm.proximas.collectAsState()
    val historial by vm.historial.collectAsState()
    val mensajeError by vm.mensajeError.collectAsState()

    var citaACancelar by remember { mutableStateOf<CitaConDetalle?>(null) }

    // Diálogo de confirmación de cancelación
    if (citaACancelar != null) {
        AlertDialog(
            onDismissRequest = { citaACancelar = null },
            title = { Text("Cancelar cita", fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro de que deseas cancelar la cita para ${citaACancelar!!.mascota.mascota.nombre}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.cancelarCita(citaACancelar!!)
                        citaACancelar = null
                    }
                ) {
                    Text("Sí, cancelar", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { citaACancelar = null }) {
                    Text("No, mantener")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis citas", fontWeight = FontWeight.Bold) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onPedirCita,
                containerColor = ColorVerdePrincipal
            ) {
                Icon(Icons.Default.Add, contentDescription = "Pedir cita", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Pestañas Próximas / Historial
            TabRow(
                selectedTabIndex = pestañaSeleccionada,
                containerColor = Color(0xFFF2F2F2),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = pestañaSeleccionada == 0,
                    onClick = { pestañaSeleccionada = 0 },
                    text = { Text("Próximas", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = pestañaSeleccionada == 1,
                    onClick = { pestañaSeleccionada = 1 },
                    text = { Text("Historial", fontWeight = FontWeight.Bold) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error de cancelación
            if (mensajeError != null) {
                Text(
                    text = mensajeError!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Contenido según la pestaña activa
            val listaMostrar = if (pestañaSeleccionada == 0) proximas else historial

            if (listaMostrar.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (pestañaSeleccionada == 0) "No tienes citas próximas" else "No hay citas en el historial",
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(listaMostrar, key = { it.cita.id }) { item ->
                        CitaCard(
                            item = item,
                            puedeCancelar = vm.puedeCancelar(item),
                            onSolicitarCancelar = { citaACancelar = item }
                        )
                    }
                }
            }

            // Aviso permanente en la parte inferior si está en "Próximas"
            if (pestañaSeleccionada == 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = Color(0xFFF57F17), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Puedes cancelar hasta 2 horas antes",
                            fontSize = 13.sp,
                            color = Color(0xFFF57F17),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CitaCard(
    item: CitaConDetalle,
    puedeCancelar: Boolean,
    onSolicitarCancelar: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(ColorVerdeClaro, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    val icono = when {
                        item.servicio.nombre.contains("Vacuna", ignoreCase = true) -> Icons.Default.MedicalServices
                        item.servicio.nombre.contains("Baño", ignoreCase = true) || item.servicio.nombre.contains("Corte", ignoreCase = true) -> Icons.Default.ContentCut
                        else -> Icons.Default.Favorite
                    }
                    Icon(icono, contentDescription = null, tint = ColorVerdePrincipal)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = item.servicio.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(
                        text = "${item.mascota.mascota.nombre}  ·  ${item.cita.fecha}  ${item.cita.hora}",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EstadoCitaChip(estadoStr = item.cita.estado)

                if (puedeCancelar) {
                    TextButton(onClick = onSolicitarCancelar) {
                        Text(text = "Cancelar", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun EstadoCitaChip(estadoStr: String) {
    val (fondo, texto, label) = when (estadoStr) {
        EstadoCita.CONFIRMADA.name -> Triple(Color(0xFFE8F5E9), ColorVerdePrincipal, "Confirmada")
        EstadoCita.PENDIENTE.name -> Triple(Color(0xFFFFF3E0), Color(0xFFE65100), "Pendiente")
        EstadoCita.ATENDIDA.name -> Triple(Color(0xFFE3F2FD), Color(0xFF1565C0), "Atendida")
        else -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "Cancelada")
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = fondo
    ) {
        Text(
            text = label,
            color = texto,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}