package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FechaHoraScreen(
    especialidadId: String,
    medicoId: String,
    onNavigateToConfirmar: (String, String, String, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val medico = Repositorio.medicos.find { it.id == medicoId }
    val especialidad = Repositorio.especialidades.find { it.id == especialidadId }

    val hoy = remember { LocalDate.now() }
    var fechaBaseSemana by remember { mutableStateOf(hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))) }
    var fechaSeleccionada by remember { mutableStateOf(hoy) }
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    val inicioSemanaActual = remember(hoy) { hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)) }
    val puedeRetroceder = fechaBaseSemana.isAfter(inicioSemanaActual)

    val mesAnoFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es", "ES"))
    val fechaIsoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val fechaIsoTexto = fechaSeleccionada.format(fechaIsoFormatter)
    val horariosDisponibles = remember(fechaSeleccionada, medicoId) {
        Repositorio.horariosDisponibles(medicoId, fechaIsoTexto)
    }

    val azulOscuro = Color(0xFF0D1B2A)
    val azulPrincipal = Color(0xFF0066FF)
    val grisFondoCampo = Color(0xFFF8FAFC)
    val grisBorde = Color(0xFFE2E8F0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Seleccionar fecha y hora",
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
            medico?.let { m ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, grisBorde, RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = grisFondoCampo),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F1FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = m.nombre.take(1) + m.nombre.split(" ").getOrNull(1)?.take(1).orEmpty(),
                                color = azulPrincipal,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = m.nombre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = azulOscuro
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = especialidad?.nombre ?: "",
                                color = Color(0xFF64748B),
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFB800),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${m.calificacion}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = azulOscuro
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (puedeRetroceder) {
                            fechaBaseSemana = fechaBaseSemana.minusWeeks(1)
                        }
                    },
                    enabled = puedeRetroceder
                ) {
                    Icon(
                        Icons.Default.ChevronLeft,
                        contentDescription = "Semana anterior",
                        tint = if (puedeRetroceder) azulOscuro else Color.LightGray
                    )
                }

                Text(
                    text = fechaBaseSemana.format(mesAnoFormatter).replaceFirstChar { it.uppercase() },
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = azulOscuro
                )

                IconButton(
                    onClick = { fechaBaseSemana = fechaBaseSemana.plusWeeks(1) }
                ) {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = "Semana siguiente",
                        tint = azulOscuro
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            val diasCabecera = listOf("Lun", "Mar", "Mié", "Jue", "Vie")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                (0..4).forEach { index ->
                    val diaSemana = fechaBaseSemana.plusDays(index.toLong())
                    val esPasado = diaSemana.isBefore(hoy)
                    val esSeleccionado = diaSemana == fechaSeleccionada

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .padding(2.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                when {
                                    esSeleccionado -> azulPrincipal
                                    esPasado -> Color(0xFFF1F5F9)
                                    else -> grisFondoCampo
                                }
                            )
                            .clickable(enabled = !esPasado) {
                                fechaSeleccionada = diaSemana
                                horaSeleccionada = null
                            }
                            .padding(vertical = 10.dp)
                    ) {
                        Text(
                            text = diasCabecera[index],
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (esSeleccionado) Color.White else Color(0xFF64748B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = diaSemana.dayOfMonth.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = when {
                                esSeleccionado -> Color.White
                                esPasado -> Color.LightGray
                                else -> azulOscuro
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Horarios disponibles",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = azulOscuro
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (horariosDisponibles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay horarios disponibles para este día.",
                        color = Color(0xFF64748B),
                        fontSize = 13.sp
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(horariosDisponibles) { hora ->
                        val esSeleccionada = hora == horaSeleccionada
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (esSeleccionada) azulPrincipal else grisFondoCampo)
                                .border(
                                    width = 1.dp,
                                    color = if (esSeleccionada) azulPrincipal else grisBorde,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { horaSeleccionada = hora }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = hora,
                                fontSize = 13.sp,
                                fontWeight = if (esSeleccionada) FontWeight.Bold else FontWeight.Medium,
                                color = if (esSeleccionada) Color.White else azulOscuro
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    horaSeleccionada?.let { hora ->
                        onNavigateToConfirmar(especialidadId, medicoId, fechaIsoTexto, hora)
                    }
                },
                enabled = horaSeleccionada != null,
                colors = ButtonDefaults.buttonColors(containerColor = azulPrincipal),
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Continuar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}