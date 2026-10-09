package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegistro: () -> Unit
) {
    val azulOscuro = Color(0xFF0D253C)
    val azulPrincipal = Color(0xFF0066FF)
    val azulSuave = Color(0xFFE8F1FF)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Seccion del Logo y Titulo
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icono de Cruz de SaludPlus
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF0052D4), Color(0xFF4364F7), Color(0xFF6FB1FC))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Logo SaludPlus",
                        tint = Color.White,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Clínica",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = azulOscuro
                )

                Text(
                    text = "SaludPlus",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = azulOscuro,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Tu salud, nuestra prioridad",
                    fontSize = 15.sp,
                    color = Color(0xFF6B7280),
                    fontWeight = FontWeight.Medium
                )
            }

            // Ilustración Central (Médico con libreta)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .clip(CircleShape)
                        .background(azulSuave),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Médico",
                                tint = azulPrincipal,
                                modifier = Modifier.size(60.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = "Atención médica",
                            tint = azulPrincipal,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }

            // Botones de Accion ("Comenzar" e "Iniciar sesion")
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onNavigateToRegistro,
                    colors = ButtonDefaults.buttonColors(containerColor = azulPrincipal),
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "Comenzar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                OutlinedButton(
                    onClick = onNavigateToLogin,
                    shape = RoundedCornerShape(25.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.horizontalGradient(listOf(azulPrincipal, azulPrincipal))
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "Iniciar sesión",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = azulPrincipal
                    )
                }
            }
        }
    }
}