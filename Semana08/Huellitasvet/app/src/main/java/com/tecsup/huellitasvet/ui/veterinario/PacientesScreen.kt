package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import com.tecsup.huellitasvet.viewmodel.PacientesViewModel
import java.time.LocalDate

private val ColorAzulVet = Color(0xFF1D51D8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacientesScreen(
    vm: PacientesViewModel,
    onVerFicha: (Int) -> Unit
) {
    val busqueda by vm.busqueda.collectAsState()
    val filtroEspecie by vm.filtroEspecie.collectAsState()
    val pacientes by vm.pacientes.collectAsState()

    val anioActual = remember { LocalDate.now().year }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pacientes", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Buscador
            OutlinedTextField(
                value = busqueda,
                onValueChange = { vm.cambiarBusqueda(it) },
                placeholder = { Text("Buscar mascota o dueño...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Chips Filtro Especie
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val opciones = listOf(
                    "Todos" to "TODOS",
                    "Perros" to "PERRO",
                    "Gatos" to "GATO",
                    "Otros" to "OTRO"
                )
                items(opciones) { (label, clave) ->
                    val selected = filtroEspecie == clave
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (selected) ColorAzulVet else Color(0xFFF2F2F2),
                        modifier = Modifier.clickable { vm.cambiarFiltroEspecie(clave) }
                    ) {
                        Text(
                            text = label,
                            color = if (selected) Color.White else Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista Pacientes
            if (pacientes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No se encontraron pacientes", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(pacientes, key = { it.mascota.id }) { item ->
                        val m = item.mascota
                        val d = item.dueno
                        val edad = (anioActual - m.anioNacimiento).coerceAtLeast(0)

                        Card(
                            shape = RoundedCornerShape(16.dp),
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
                                        .size(48.dp)
                                        .background(Color(0xFFE8F0FE), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Pets, contentDescription = null, tint = ColorAzulVet, modifier = Modifier.size(24.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = m.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text(text = "${m.raza} · $edad años", fontSize = 12.sp, color = Color.Gray)
                                    Text(text = "Dueño: ${d.nombre}", fontSize = 12.sp, color = Color.Gray)
                                }
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}