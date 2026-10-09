package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CitaExitosaScreen(
    citaId: String,
    onIrAMisCitas: () -> Unit
) {
    val azulOscuro = Color(0xFF0D1B2A)
    val azulPrincipal = Color(0xFF0066FF)
    val verdeExito = Color(0xFF2E7D32)
    val verdeFondo = Color(0xFFE8F5E9)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Icono e Información de Éxito
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(verdeFondo),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Cita Confirmada",
                        tint = verdeExito,
                        modifier = Modifier.size(64.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¡Cita Agendada con Éxito!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = azulOscuro,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Tu reserva se ha registrado correctamente en nuestro sistema.",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Tarjeta con el Código de Cita
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE2E8F0),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Código de confirmación",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = citaId,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = azulPrincipal,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            // Botón "Ver Mis Citas"
            Button(
                onClick = onIrAMisCitas,
                colors = ButtonDefaults.buttonColors(containerColor = azulPrincipal),
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Ver Mis Citas",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}