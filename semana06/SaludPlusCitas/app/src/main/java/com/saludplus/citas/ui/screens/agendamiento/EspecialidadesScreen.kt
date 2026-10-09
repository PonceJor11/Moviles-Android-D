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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EspecialidadesScreen(
    onSeleccionarEspecialidad: (String) -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    val lista = Repositorio.buscarEspecialidades(query)

    val azulOscuro = Color(0xFF0D1B2A)
    val grisCampo = Color(0xFFF1F5F9)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Especialidades",
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
            TextField(
                value = query,
                onValueChange = { query = it },
                placeholder = {
                    Text(
                        "Buscar especialidad...",
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

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(lista) { esp ->
                    val (colorIcono, colorFondo, icono) = obtenerEstiloEspecialidad(esp.nombre)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                width = 1.dp,
                                color = Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { onSeleccionarEspecialidad(esp.id) },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(CircleShape)
                                        .background(colorFondo),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icono,
                                        contentDescription = esp.nombre,
                                        tint = colorIcono,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text(
                                        text = esp.nombre,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = azulOscuro
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = esp.descripcion,
                                        fontSize = 12.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = Color(0xFF1976D2),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun obtenerEstiloEspecialidad(nombre: String): Triple<Color, Color, ImageVector> {
    return when {
        nombre.contains("General", ignoreCase = true) ->
            Triple(Color(0xFF0288D1), Color(0xFFE1F5FE), Icons.Default.Groups)
        nombre.contains("Pediatría", ignoreCase = true) ->
            Triple(Color(0xFFF57C00), Color(0xFFFFF3E0), Icons.Default.ChildCare)
        nombre.contains("Ginecología", ignoreCase = true) ->
            Triple(Color(0xFFE91E63), Color(0xFFFCE4EC), Icons.Default.Female)
        nombre.contains("Cardiología", ignoreCase = true) ->
            Triple(Color(0xFFE53935), Color(0xFFFFEBEE), Icons.Default.Favorite)
        nombre.contains("Dermatología", ignoreCase = true) ->
            Triple(Color(0xFFFF9800), Color(0xFFFFF3E0), Icons.Default.Face)
        nombre.contains("Traumatología", ignoreCase = true) ->
            Triple(Color(0xFF0288D1), Color(0xFFE1F5FE), Icons.Default.Healing)
        nombre.contains("Oftalmología", ignoreCase = true) ->
            Triple(Color(0xFF1976D2), Color(0xFFE8F1FF), Icons.Default.Visibility)
        else ->
            Triple(Color(0xFF1976D2), Color(0xFFE8F1FF), Icons.Default.MedicalServices)
    }
}