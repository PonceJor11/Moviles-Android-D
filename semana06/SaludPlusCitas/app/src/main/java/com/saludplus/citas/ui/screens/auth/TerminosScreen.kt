package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminosScreen(
    onVolver: () -> Unit
) {
    val scrollState = rememberScrollState()

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
                            text = "Términos y Condiciones",
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
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE2E8F0),
                        shape = RoundedCornerShape(20.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(20.dp)
                ) {
                    Text(
                        text = "1. Uso del Servicio",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = azulOscuro
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Al registrarse en Clínica SaludPlus, usted acepta que esta aplicación gestiona agendamientos simulados en memoria para pruebas de laboratorio.",
                        fontSize = 13.sp,
                        color = Color(0xFF475569),
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "2. Privacidad de Datos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = azulOscuro
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Los datos ingresados (nombre, correo, teléfono) se almacenan localmente durante la sesión actual de la aplicación y no son transmitidos a servidores externos ni bases de datos persistentes.",
                        fontSize = 13.sp,
                        color = Color(0xFF475569),
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "3. Cancelación de Citas",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = azulOscuro
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "El usuario es libre de agendar y cancelar citas médicas según la disponibilidad de horarios informada en el sistema.",
                        fontSize = 13.sp,
                        color = Color(0xFF475569),
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "4. Notificaciones",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = azulOscuro
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Las alertas enviadas por la app corresponden a recordatorios automáticos generados localmente a partir de la agenda activa.",
                        fontSize = 13.sp,
                        color = Color(0xFF475569),
                        lineHeight = 19.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onVolver,
                colors = ButtonDefaults.buttonColors(containerColor = azulPrincipal),
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Aceptar y Volver",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}