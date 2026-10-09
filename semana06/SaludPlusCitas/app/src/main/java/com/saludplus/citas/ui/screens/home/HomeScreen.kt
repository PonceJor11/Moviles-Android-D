package com.saludplus.citas.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToEspecialidades: () -> Unit,
    onNavigateToMedicos: (String) -> Unit,
    onNavigateToNotificaciones: () -> Unit,
    onNavigateToMisCitas: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    onNavigateToResultados: () -> Unit = {}
) {
    val usuario = Repositorio.usuarioActual
    val destacadas = Repositorio.especialidadesDestacadas()
    val azulPrincipal = Color(0xFF1976D2)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color(0xFF1A237E))
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToNotificaciones) {
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notificaciones", tint = Color(0xFF1A237E))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = azulPrincipal,
                        selectedTextColor = azulPrincipal,
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigateToResultados() },
                    icon = { Icon(Icons.Outlined.Description, contentDescription = "Historial") },
                    label = { Text("Historial", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToMisCitas,
                    icon = { Icon(Icons.Default.EventAvailable, contentDescription = "Mis citas") },
                    label = { Text("Mis citas", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToPerfil,
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = "¡Hola, ${usuario?.nombre?.split(" ")?.getOrNull(0) ?: "Juan"}!",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D1B2A)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "¿Qué deseas hacer hoy?",
                fontSize = 16.sp,
                color = Color(0xFF6C757D)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Tarjetas principales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Agendar cita (Azul)
                BotonCuadradoHome(
                    titulo = "Agendar cita",
                    icono = Icons.Default.Event,
                    colorFondo = Color(0xFFE8F1FF),
                    colorTextoEIcono = Color(0xFF1976D2),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToEspecialidades
                )
                // Mis citas (Verde) -> Navega a Mis Citas
                BotonCuadradoHome(
                    titulo = "Mis citas",
                    icono = Icons.Default.EventAvailable,
                    colorFondo = Color(0xFFE8F8F5),
                    colorTextoEIcono = Color(0xFF2E7D32),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToMisCitas
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Mis datos (Morado) -> Navega a Perfil
                BotonCuadradoHome(
                    titulo = "Mis datos",
                    icono = Icons.Default.Person,
                    colorFondo = Color(0xFFF3E5F5),
                    colorTextoEIcono = Color(0xFF7B1FA2),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToPerfil
                )
                // Historial / Resultados (Naranja)
                BotonCuadradoHome(
                    titulo = "Historial",
                    icono = Icons.Outlined.Description,
                    colorFondo = Color(0xFFFFF3E0),
                    colorTextoEIcono = Color(0xFFE65100),
                    modifier = Modifier.weight(1f),
                    onClick = {onNavigateToResultados() }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Especialidades favoritas",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D1B2A)
                )
                TextButton(onClick = onNavigateToEspecialidades) {
                    Text(
                        text = "Ver todas",
                        color = azulPrincipal,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(destacadas) { esp ->
                    val (colorIcono, colorFondo, icono) = when {
                        esp.nombre.contains("General", ignoreCase = true) -> Triple(Color(0xFF0288D1), Color(0xFFE1F5FE), Icons.Default.Groups)
                        esp.nombre.contains("Pediatría", ignoreCase = true) -> Triple(Color(0xFFF57C00), Color(0xFFFFF3E0), Icons.Default.ChildCare)
                        else -> Triple(Color(0xFFE53935), Color(0xFFFFEBEE), Icons.Default.Favorite)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(85.dp)
                            .clickable { onNavigateToMedicos(esp.id) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(colorFondo),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icono,
                                contentDescription = esp.nombre,
                                tint = colorIcono,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = esp.nombre,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D1B2A),
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BotonCuadradoHome(
    titulo: String,
    icono: ImageVector,
    colorFondo: Color,
    colorTextoEIcono: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icono,
                contentDescription = titulo,
                tint = colorTextoEIcono,
                modifier = Modifier.size(42.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = titulo,
                color = colorTextoEIcono,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}