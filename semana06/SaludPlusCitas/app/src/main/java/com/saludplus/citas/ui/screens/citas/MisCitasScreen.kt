package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun MisCitasScreen(
    onVerDetalle: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val usuario = Repositorio.usuarioActual
    val lista = usuario?.let { Repositorio.citasDelUsuario(it.id) } ?: emptyList()

    val azulOscuro = Color(0xFF0D1B2A)
    val azulPrincipal = Color(0xFF0066FF)
    val verdeEstado = Color(0xFF2E7D32)
    val verdeFondo = Color(0xFFE8F5E9)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Mis Citas",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = azulOscuro
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
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
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            if (lista.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F1FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EventAvailable,
                                contentDescription = null,
                                tint = azulPrincipal,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No tienes citas agendadas",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = azulOscuro
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Las citas que reserves aparecerán en esta sección.",
                            fontSize = 14.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(lista) { cita ->
                        val med = Repositorio.obtenerMedico(cita.medicoId)
                        val esp = Repositorio.obtenerEspecialidad(cita.especialidadId)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFFE2E8F0),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { onVerDetalle(cita.id) },
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                // Cabecera: Especialidad + Badge de Estado
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = esp?.nombre ?: "Especialidad Médica",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = azulOscuro
                                    )

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(verdeFondo)
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Confirmada",
                                            color = verdeEstado,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Nombre del Médico
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = azulPrincipal,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = med?.nombre ?: "Médico Asignado",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF334155)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                                Spacer(modifier = Modifier.height(12.dp))

                                // Fecha y Hora organizados en columnas
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarToday,
                                            contentDescription = null,
                                            tint = Color(0xFF64748B),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = cita.fecha,
                                            fontSize = 13.sp,
                                            color = Color(0xFF475569),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.AccessTime,
                                            contentDescription = null,
                                            tint = Color(0xFF64748B),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = cita.hora,
                                            fontSize = 13.sp,
                                            color = Color(0xFF475569),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}