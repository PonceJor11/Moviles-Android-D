package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.model.Horarios
import com.tecsup.huellitasvet.viewmodel.PedirCitaViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val ColorVerdePrinicpal = Color(0xFF00966D)
private val ColorVerdeClaro = Color(0xFFE8F5E9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedirCitaScreen(
    vm: PedirCitaViewModel,
    onVolver: () -> Unit,
    onCitaConfirmada: () -> Unit
) {
    val mascotas by vm.mascotas.collectAsState()
    val servicios by vm.serviciosActivos.collectAsState()
    val mascotaSel by vm.mascotaSeleccionada.collectAsState()
    val servicioSel by vm.servicioSeleccionado.collectAsState()
    val fechaSel by vm.fechaSeleccionada.collectAsState()
    val horaSel by vm.horaSeleccionada.collectAsState()
    val horasOcupadas by vm.horasOcupadas.collectAsState()
    val errorMsg by vm.error.collectAsState()

    val hoy = remember { LocalDate.now() }
    val proximosDias = remember { (0..6).map { hoy.plusDays(it.toLong()) } }
    val horaActual = remember { LocalTime.now() }

    val puedeConfirmar = mascotaSel != null && servicioSel != null && fechaSel.isNotEmpty() && horaSel != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedir cita", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 1. ¿Quién viene?
            Text(text = "¿Quién viene?", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(mascotas, key = { it.id }) { m ->
                    val selected = m.id == mascotaSel?.id
                    Row(
                        modifier = Modifier
                            .background(
                                color = if (selected) ColorVerdeClaro else Color.White,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (selected) ColorVerdePrinicpal else Color.LightGray,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { vm.seleccionarMascota(m) }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Pets,
                            contentDescription = null,
                            tint = if (selected) ColorVerdePrinicpal else Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = m.nombre,
                            fontWeight = FontWeight.Bold,
                            color = if (selected) ColorVerdePrinicpal else Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Servicio
            Text(text = "Servicio", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                servicios.forEach { s ->
                    val selected = s.id == servicioSel?.id
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selected) ColorVerdeClaro else Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = if (selected) ColorVerdePrinicpal else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { vm.seleccionarServicio(s) }
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = s.nombre, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text(text = "${s.duracionMin} min", fontSize = 12.sp, color = Color.Gray)
                            }
                            Text(
                                text = "S/ ${s.precio.toInt()}",
                                fontWeight = FontWeight.Bold,
                                color = ColorVerdePrinicpal,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Fecha
            Text(text = "Fecha", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(proximosDias) { d ->
                    val fechaStr = d.toString()
                    val selected = fechaStr == fechaSel
                    val diaSemana = d.format(DateTimeFormatter.ofPattern("EEE", Locale("es", "ES"))).replaceFirstChar { it.uppercase() }
                    val diaNum = d.dayOfMonth.toString()

                    Column(
                        modifier = Modifier
                            .width(60.dp)
                            .height(70.dp)
                            .background(
                                color = if (selected) ColorVerdePrinicpal else Color.White,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .border(1.dp, if (selected) ColorVerdePrinicpal else Color.LightGray, RoundedCornerShape(14.dp))
                            .clickable { vm.seleccionarFecha(fechaStr) },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = diaSemana,
                            fontSize = 12.sp,
                            color = if (selected) Color.White.copy(alpha = 0.8f) else Color.Gray
                        )
                        Text(
                            text = diaNum,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selected) Color.White else Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Hora
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Hora", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "Ocupado = gris", fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Rejilla de Horarios
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Horarios.LISTA.chunked(4).forEach { fila ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        fila.forEach { h ->
                            val estaOcupado = horasOcupadas.contains(h)
                            val esHoy = fechaSel == hoy.toString()
                            val yaPaso = esHoy && try {
                                LocalTime.parse(h).isBefore(horaActual)
                            } catch (e: Exception) {
                                false
                            }
                            val deshabilitado = estaOcupado || yaPaso
                            val selected = h == horaSel

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                                    .background(
                                        color = when {
                                            deshabilitado -> Color(0xFFF0F0F0)
                                            selected -> ColorVerdePrinicpal
                                            else -> Color.White
                                        },
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = when {
                                            deshabilitado -> Color.Transparent
                                            selected -> ColorVerdePrinicpal
                                            else -> Color.LightGray
                                        },
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable(enabled = !deshabilitado) {
                                        vm.seleccionarHora(h)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = h,
                                    fontSize = 13.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                    color = when {
                                        deshabilitado -> Color.LightGray
                                        selected -> Color.White
                                        else -> Color.Black
                                    },
                                    textDecoration = if (deshabilitado) TextDecoration.LineThrough else TextDecoration.None
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Error
            if (errorMsg != null) {
                Text(
                    text = errorMsg!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Tarjeta Resumen
            if (puedeConfirmar) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorVerdeClaro),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "${mascotaSel?.nombre} · ${servicioSel?.nombre}",
                            color = ColorVerdePrinicpal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "$fechaSel · $horaSel · S/ ${servicioSel?.precio?.toInt()}",
                            color = Color.DarkGray,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Botón Confirmar
            Button(
                onClick = { vm.confirmarCita(onExito = onCitaConfirmada) },
                enabled = puedeConfirmar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorVerdePrinicpal,
                    disabledContainerColor = Color(0xFFA5D6A7)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Confirmar cita",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}