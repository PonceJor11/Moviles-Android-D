package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCitaScreen(
    citaId: String,
    onVolver: () -> Unit
) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = cita?.let { Repositorio.obtenerMedico(it.medicoId) }
    val especialidad = cita?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }

    var mostrarDialogo by remember { mutableStateOf(false) }

    val azulOscuro = Color(0xFF0D1B2A)
    val azulPrincipal = Color(0xFF0066FF)
    val rojoCancelar = Color(0xFFDC2626)
    val verdeEstado = Color(0xFF2E7D32)
    val verdeFondo = Color(0xFFE8F5E9)

    if (cita == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalle de Cita", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                    navigationIcon = {
                        IconButton(onClick = onVolver) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No se encontró la cita especificada.", color = Color.Gray, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onVolver) { Text("Volver") }
                }
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Detalle de Cita",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = azulOscuro
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = azulOscuro
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE2E8F0),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Estado
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Estado de la cita",
                                fontSize = 13.sp,
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.Medium
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(verdeFondo)
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Confirmada",
                                    color = verdeEstado,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Especialidad
                        ItemDetalleCita(
                            icono = Icons.Default.MedicalServices,
                            titulo = "Especialidad",
                            valor = especialidad?.nombre ?: "-",
                            colorIcono = azulPrincipal
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Médico
                        ItemDetalleCita(
                            icono = Icons.Default.Person,
                            titulo = "Médico Tratante",
                            valor = medico?.nombre ?: "-",
                            colorIcono = azulPrincipal
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Fecha
                        ItemDetalleCita(
                            icono = Icons.Default.CalendarToday,
                            titulo = "Fecha",
                            valor = cita.fecha,
                            colorIcono = azulPrincipal
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Hora
                        ItemDetalleCita(
                            icono = Icons.Default.AccessTime,
                            titulo = "Hora",
                            valor = cita.hora,
                            colorIcono = azulPrincipal
                        )
                    }
                }
            }

            // Botón Cancelar
            OutlinedButton(
                onClick = { mostrarDialogo = true },
                shape = RoundedCornerShape(25.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(rojoCancelar)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Cancelar Cita",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = rojoCancelar
                )
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            title = {
                Text(
                    text = "¿Cancelar esta cita?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = azulOscuro
                )
            },
            text = {
                Text(
                    text = "Esta acción eliminará la reserva y liberará el horario de atención.",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        Repositorio.cancelarCita(citaId)
                        mostrarDialogo = false
                        onVolver()
                    }
                ) {
                    Text("Sí, cancelar", color = rojoCancelar, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Mantener cita", color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                }
            }
        )
    }
}

@Composable
private fun ItemDetalleCita(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    titulo: String,
    valor: String,
    colorIcono: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F1FF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = titulo,
                fontSize = 12.sp,
                color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = valor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D1B2A)
            )
        }
    }
}