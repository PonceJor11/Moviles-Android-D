package com.saludplus.citas.ui.screens.resultados

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ResultadoLaboratorio(
    val id: String,
    val examen: String,
    val fecha: String,
    val estado: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultadosScreen(
    onNavigateBack: () -> Unit = {}
) {
    val examenes = listOf(
        ResultadoLaboratorio("1", "Hemograma Completo", "10/09/2026", "Disponible"),
        ResultadoLaboratorio("2", "Perfil Lipídico", "15/08/2026", "Disponible"),
        ResultadoLaboratorio("3", "Glucosa en Ayunas", "02/07/2026", "Disponible")
    )

    val azulOscuro = Color(0xFF0D1B2A)
    val naranjaResultado = Color(0xFFE65100)
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
                            text = "Resultados de Laboratorio",
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
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(examenes) { res ->
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
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Ícono estilizado
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFF3E0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Description,
                                    contentDescription = null,
                                    tint = naranjaResultado,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            // Información del examen
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = res.examen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = azulOscuro
                                    )

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(verdeFondo)
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = res.estado,
                                            color = verdeEstado,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint = Color(0xFF64748B),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Fecha del examen: ${res.fecha}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF64748B)
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