package com.tecsup.huellitasvet.ui.veterinario // o com.tecsup.huellitasvet.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ColorVerdeAdmin = Color(0xFF00966D)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminGestionScreen(
    onVolver: () -> Unit,
    onIrAServicios: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administración", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorVerdeAdmin)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF6F8FB))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(text = "Gestión Global del Sistema", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = "Opciones exclusivas para el rol Administrador", fontSize = 13.sp, color = Color.Gray)
            }

            // Módulo 1: Catálogo y ComboBox de Servicios
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MedicalServices, contentDescription = null, tint = ColorVerdeAdmin, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Catálogo de Servicios", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("Gestión de ComboBox y Precios", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                        Button(
                            onClick = onIrAServicios,
                            colors = ButtonDefaults.buttonColors(containerColor = ColorVerdeAdmin)
                        ) {
                            Text("Abrir")
                        }
                    }
                }
            }

            // Módulo 2: Usuarios y Clientes
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Group, contentDescription = null, tint = ColorVerdeAdmin, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Usuarios Registrados", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("Gestión de clientes y datos de contacto", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• Usuarios activos: 3 registrados", fontSize = 13.sp, color = Color.DarkGray)
                        Text("• Admin Principal: admin@huellitas.com", fontSize = 13.sp, color = Color.DarkGray)
                    }
                }
            }

            // Módulo 3: Resumen Operativo
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Pets, contentDescription = null, tint = ColorVerdeAdmin)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Resumen General", fontWeight = FontWeight.Bold, color = ColorVerdeAdmin)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• Pacientes activos: Bobby, Michi", fontSize = 13.sp)
                        Text("• Estado del sistema: Base de datos Room v2", fontSize = 13.sp)
                    }
                }
            }
        }
    }
}