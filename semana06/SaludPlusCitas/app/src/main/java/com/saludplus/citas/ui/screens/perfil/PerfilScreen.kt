package com.saludplus.citas.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
fun PerfilScreen(
    onCerrarSesion: () -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    val usuario = Repositorio.usuarioActual

    val azulOscuro = Color(0xFF0D1B2A)
    val azulPrincipal = Color(0xFF0066FF)
    val rojoCancelar = Color(0xFFDC2626)

    val iniciales = usuario?.nombre?.split(" ")
        ?.take(2)
        ?.mapNotNull { it.firstOrNull()?.toString() }
        ?.joinToString("") ?: "U"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Perfil de Usuario",
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
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Avatar con Iniciales
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F1FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iniciales,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = azulPrincipal
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = usuario?.nombre ?: "Usuario",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = azulOscuro
                )

                Text(
                    text = "Paciente registrado",
                    fontSize = 13.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Tarjeta con Datos
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
                        // Nombre
                        ItemDatoPerfil(
                            icono = Icons.Default.Person,
                            etiqueta = "Nombre completo",
                            valor = usuario?.nombre ?: "-"
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Correo
                        ItemDatoPerfil(
                            icono = Icons.Default.Email,
                            etiqueta = "Correo electrónico",
                            valor = usuario?.correo ?: "-"
                        )

                        if (usuario?.telefono?.isNotEmpty() == true) {
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                            Spacer(modifier = Modifier.height(16.dp))

                            // Teléfono
                            ItemDatoPerfil(
                                icono = Icons.Default.Phone,
                                etiqueta = "Teléfono",
                                valor = usuario.telefono
                            )
                        }
                    }
                }
            }

            // Botón Cerrar Sesión
            OutlinedButton(
                onClick = {
                    Repositorio.cerrarSesion()
                    onCerrarSesion()
                },
                shape = RoundedCornerShape(25.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(rojoCancelar)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Cerrar Sesión",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = rojoCancelar
                )
            }
        }
    }
}

@Composable
private fun ItemDatoPerfil(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    etiqueta: String,
    valor: String
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
                tint = Color(0xFF0066FF),
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = etiqueta,
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