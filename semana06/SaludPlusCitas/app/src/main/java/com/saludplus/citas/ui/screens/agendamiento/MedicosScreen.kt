package com.saludplus.citas.ui.screens.agendamiento

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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Search
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
fun MedicosScreen(
    especialidadId: String,
    onSeleccionarMedico: (String) -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    val especialidad = Repositorio.obtenerEspecialidad(especialidadId)
    val medicos = Repositorio.medicosPorEspecialidad(especialidadId).filter {
        it.nombre.contains(query, ignoreCase = true) || it.biografia.contains(query, ignoreCase = true)
    }

    val azulOscuro = Color(0xFF0D1B2A)
    val azulPrincipal = Color(0xFF1976D2)
    val grisCampo = Color(0xFFF1F5F9)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Médicos de ${especialidad?.nombre ?: "Especialidad"}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = azulOscuro
                    )
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
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Outlined.Search,
                            contentDescription = "Buscar",
                            tint = azulOscuro
                        )
                    }
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
            // Buscador estilizado
            TextField(
                value = query,
                onValueChange = { query = it },
                placeholder = {
                    Text(
                        "Buscar especialista...",
                        color = Color(0xFF94A3B8),
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Search,
                        contentDescription = "Buscar",
                        tint = Color(0xFF64748B)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = grisCampo,
                    unfocusedContainerColor = grisCampo,
                    disabledContainerColor = grisCampo,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Médicos
            if (medicos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No se encontraron médicos disponibles.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(medicos) { med ->
                        val disponibilidadTexto = when (med.id) {
                            "m1", "m2" -> "Disponible hoy"
                            "m3", "m4" -> "Disponible mañana"
                            else -> "Disponible esta semana"
                        }

                        val disponibilidadFondo = when (disponibilidadTexto) {
                            "Disponible hoy" -> Color(0xFFE8F5E9)
                            "Disponible mañana" -> Color(0xFFE0F2F1)
                            else -> Color(0xFFE8F5E9)
                        }

                        val disponibilidadTextoColor = Color(0xFF2E7D32)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFFF1F5F9),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { onSeleccionarMedico(med.id) },
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Avatar circular con iniciales estilizadas
                                Box(
                                    modifier = Modifier
                                        .size(68.dp)
                                        .clip(CircleShape)
                                        .background(azulPrincipal.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = med.nombre.split(" ")
                                            .take(2)
                                            .mapNotNull { it.firstOrNull()?.toString() }
                                            .joinToString(""),
                                        color = azulPrincipal,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = med.nombre,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = azulOscuro
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // Calificación con estrella
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFFFB300),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${med.calificacion}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = azulOscuro
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "(120)",
                                            fontSize = 12.sp,
                                            color = Color(0xFF94A3B8)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Badge de Disponibilidad
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(disponibilidadFondo)
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = disponibilidadTexto,
                                            color = disponibilidadTextoColor,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
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