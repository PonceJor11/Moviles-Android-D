package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import java.time.LocalDate

private val ColorVerdePrinicpal = Color(0xFF00966D)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotasScreen(
    vm: InicioViewModel,
    onVerFicha: (Int) -> Unit,
    onAgregarMascota: () -> Unit
) {
    val mascotas by vm.mascotas.collectAsState()
    val anioActual = LocalDate.now().year

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Mascotas", fontWeight = FontWeight.Bold) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAgregarMascota,
                containerColor = ColorVerdePrinicpal
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Mascota", tint = Color.White)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mascotas, key = { it.id }) { m ->
                val edad = (anioActual - m.anioNacimiento).coerceAtLeast(0)
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onVerFicha(m.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color(0xFFE8F5E9), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Pets, contentDescription = null, tint = ColorVerdePrinicpal)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = m.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "${m.especie} · ${m.raza}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "$edad años",
                                fontSize = 12.sp,
                                color = ColorVerdePrinicpal,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}