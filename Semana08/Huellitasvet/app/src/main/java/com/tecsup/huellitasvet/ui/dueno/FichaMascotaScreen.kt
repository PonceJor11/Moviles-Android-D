package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel
import java.time.LocalDate

private val ColorVerdePrincipal = Color(0xFF00966D)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FichaMascotaScreen(
    mascotaId: Int,
    vm: MascotaViewModel,
    onVolver: () -> Unit,
    onEditar: (Int) -> Unit,
    onPedirCita: (Int) -> Unit
) {
    val mascota by vm.obtenerMascota(mascotaId).collectAsState()
    val historial by vm.historial(mascotaId).collectAsState()
    var pestañaSeleccionada by remember { mutableIntStateOf(1) } // 0: Datos, 1: Historial clínico

    val anioActual = LocalDate.now().year

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = mascota?.nombre ?: "Ficha Mascota", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        mascota?.let { m ->
            val edad = (anioActual - m.anioNacimiento).coerceAtLeast(0)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Tarjeta Cabecera
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .background(Color(0xFFE8F5E9), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Pets, contentDescription = null, tint = ColorVerdePrincipal, modifier = Modifier.size(40.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = m.nombre, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Text(text = "${m.especie} ${m.raza} · $edad años", color = Color.Gray, fontSize = 14.sp)
                            Text(text = "${m.sexo.lowercase().replaceFirstChar { it.uppercase() }} · ${m.peso} kg", color = Color.Gray, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFE8F5E9)
                            ) {
                                Text(
                                    text = "Vacunas al día",
                                    color = ColorVerdePrincipal,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botones Pedir Cita y Editar
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { onPedirCita(m.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorVerdePrincipal),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Default.EventNote, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Pedir cita", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { onEditar(m.id) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = ColorVerdePrincipal)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Editar", color = ColorVerdePrincipal, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Pestañas (Datos / Historial clínico)
                TabRow(
                    selectedTabIndex = pestañaSeleccionada,
                    containerColor = Color(0xFFF2F2F2),
                    indicator = {},
                    divider = {}
                ) {
                    Tab(
                        selected = pestañaSeleccionada == 0,
                        onClick = { pestañaSeleccionada = 0 },
                        text = { Text("Datos", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = pestañaSeleccionada == 1,
                        onClick = { pestañaSeleccionada = 1 },
                        text = { Text("Historial clínico", fontWeight = FontWeight.Bold) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Contenido de las pestañas
                if (pestañaSeleccionada == 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(text = "Información General", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Nombre: ${m.nombre}")
                        Text(text = "Especie: ${m.especie}")
                        Text(text = "Raza: ${m.raza}")
                        Text(text = "Año Nacimiento: ${m.anioNacimiento}")
                        Text(text = "Peso Actual: ${m.peso} kg")
                        Text(text = "Sexo: ${m.sexo}")
                    }
                } else {
                    if (historial.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Aún no tiene atenciones", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(historial, key = { it.cita.id }) { item ->
                                HistorialItem(item = item)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistorialItem(item: CitaConDetalle) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ColorVerdePrincipal)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = item.servicio.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = item.cita.fecha, fontSize = 12.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.cita.diagnostico.ifBlank { "Sin diagnóstico detallado" },
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Red, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Peso: ${item.cita.pesoRegistrado} kg", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}