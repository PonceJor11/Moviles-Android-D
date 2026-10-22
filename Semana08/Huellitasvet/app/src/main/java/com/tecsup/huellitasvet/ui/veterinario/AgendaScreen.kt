package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.dueno.EstadoCitaChip
import com.tecsup.huellitasvet.viewmodel.AgendaViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val ColorAzulVet = Color(0xFF1D51D8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    vm: AgendaViewModel,
    onAtenderCita: (Int) -> Unit
) {
    val fechaSel by vm.fechaSeleccionada.collectAsState()
    val citas by vm.citas.collectAsState()

    val hoy = remember { LocalDate.now() }
    val proximosDias = remember { (0..6).map { hoy.plusDays(it.toLong()) } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agenda", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Chips de días
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(proximosDias) { d ->
                    val fechaStr = d.toString()
                    val selected = fechaStr == fechaSel
                    val label = if (d.isEqual(hoy)) "Hoy ${d.dayOfMonth}" else "${d.format(DateTimeFormatter.ofPattern("EEE", Locale("es", "ES")))} ${d.dayOfMonth}"

                    Box(
                        modifier = Modifier
                            .background(
                                color = if (selected) ColorAzulVet else Color.White,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .border(1.dp, if (selected) ColorAzulVet else Color.LightGray, RoundedCornerShape(20.dp))
                            .clickable { vm.seleccionarFecha(fechaStr) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = label.replaceFirstChar { it.uppercase() },
                            color = if (selected) Color.White else Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Citas del día en línea de tiempo
            if (citas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No hay citas para esta fecha", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(citas, key = { it.cita.id }) { item ->
                        FilaAgendaCita(
                            item = item,
                            onConfirmar = { vm.confirmar(item.cita.id) },
                            onAtender = { onAtenderCita(item.cita.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilaAgendaCita(
    item: CitaConDetalle,
    onConfirmar: () -> Unit,
    onAtender: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Columna Hora
        Text(
            text = item.cita.hora,
            fontWeight = FontWeight.Bold,
            color = ColorAzulVet,
            fontSize = 13.sp,
            modifier = Modifier.width(50.dp)
        )

        // Tarjeta de la cita
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.weight(1f)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFE8F0FE), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Pets, contentDescription = null, tint = ColorAzulVet, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "${item.mascota.mascota.nombre} · ${item.servicio.nombre}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Dueño: ${item.dueno.nombre}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EstadoCitaChip(estadoStr = item.cita.estado)

                    when (item.cita.estado) {
                        EstadoCita.PENDIENTE.name -> {
                            Button(
                                onClick = onConfirmar,
                                colors = ButtonDefaults.buttonColors(containerColor = ColorAzulVet),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                            ) {
                                Text(text = "Confirmar", fontSize = 12.sp)
                            }
                        }
                        EstadoCita.CONFIRMADA.name -> {
                            Button(
                                onClick = onAtender,
                                colors = ButtonDefaults.buttonColors(containerColor = ColorAzulVet),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                            ) {
                                Text(text = "Atender", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}