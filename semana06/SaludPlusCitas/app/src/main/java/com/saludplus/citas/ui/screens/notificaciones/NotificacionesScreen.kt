package com.saludplus.citas.ui.screens.notificaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.outlined.Notifications
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
fun NotificacionesScreen(
    onNavigateBack: () -> Unit = {}
) {
    val usuario = Repositorio.usuarioActual
    val citas = usuario?.let { Repositorio.citasDelUsuario(it.id) } ?: emptyList()

    val notificaciones = citas.map { cita ->
        val med = Repositorio.obtenerMedico(cita.medicoId)
        "Recordatorio: Tienes una cita programada con ${med?.nombre ?: "tu médico"} el ${cita.fecha} a las ${cita.hora}."
    }

    val azulOscuro = Color(0xFF0D1B2A)
    val azulPrincipal = Color(0xFF0066FF)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Notificaciones",
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
            if (notificaciones.isEmpty()) {
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
                                imageVector = Icons.Default.NotificationsNone,
                                contentDescription = null,
                                tint = azulPrincipal,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No tienes notificaciones",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = azulOscuro
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Los recordatorios de tus citas aparecerán aquí.",
                            fontSize = 14.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(notificaciones) { mensaje ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFFE2E8F0),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE8F1FF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Notifications,
                                        contentDescription = null,
                                        tint = azulPrincipal,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Text(
                                    text = mensaje,
                                    fontSize = 14.sp,
                                    color = Color(0xFF334155),
                                    lineHeight = 20.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}