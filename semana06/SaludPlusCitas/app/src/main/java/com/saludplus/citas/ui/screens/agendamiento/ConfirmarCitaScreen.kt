package com.saludplus.citas.ui.screens.agendamiento

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.repository.Repositorio
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmarCitaScreen(
    especialidadId: String,
    medicoId: String,
    fecha: String,
    hora: String,
    onNavigateToExitosa: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val medico = Repositorio.medicos.find { it.id == medicoId }
    val especialidad = Repositorio.especialidades.find { it.id == especialidadId }
    val usuarioActual = Repositorio.usuarioActual

    val fechaTextoEspanol = remember(fecha) {
        try {
            val date = LocalDate.parse(fecha)
            val formatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM yyyy", Locale("es", "ES"))
            date.format(formatter).replaceFirstChar { it.uppercase() }
        } catch (e: Exception) {
            fecha
        }
    }

    var motivo by remember { mutableStateOf("") }

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
                            text = "Confirmar cita",
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
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

                Spacer(modifier = Modifier.height(24.dp))

                // Detalle de Fecha
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F1FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = azulPrincipal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Fecha", fontSize = 12.sp, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = fechaTextoEspanol,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = azulOscuro
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Detalle de Hora
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F1FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            tint = azulPrincipal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Hora", fontSize = 12.sp, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = hora,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = azulOscuro
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Detalle de Sede
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F1FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = azulPrincipal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Sede de atención", fontSize = 12.sp, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Sede Centro - Av. Los Olivos 123",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = azulOscuro
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Campo Motivo de Consulta con texto negro al escribir
                OutlinedTextField(
                    value = motivo,
                    onValueChange = { motivo = it },
                    placeholder = { Text("Motivo de consulta (opcional)", color = Color(0xFF94A3B8)) },
                    textStyle = TextStyle(color = azulOscuro, fontSize = 15.sp),
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = grisFondoCampo,
                        unfocusedContainerColor = grisFondoCampo,
                        focusedIndicatorColor = azulPrincipal,
                        unfocusedIndicatorColor = grisBorde,
                        focusedTextColor = azulOscuro,
                        unfocusedTextColor = azulOscuro
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val nuevaCitaId = UUID.randomUUID().toString().take(8)
                    val nuevaCita = Cita(
                        id = nuevaCitaId,
                        usuarioId = usuarioActual?.id ?: "u1",
                        medicoId = medicoId,
                        especialidadId = especialidadId,
                        fecha = fecha,
                        hora = hora,
                    )
                    val ok = Repositorio.agendarCita(nuevaCita)
                    if (ok) {
                        onNavigateToExitosa(nuevaCitaId)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = azulPrincipal),
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Agendar cita",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}