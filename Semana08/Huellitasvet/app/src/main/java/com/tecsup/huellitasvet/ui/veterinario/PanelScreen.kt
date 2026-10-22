package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
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
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.viewmodel.PanelViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val ColorAzulVet = Color(0xFF1D51D8)

@Composable
fun PanelScreen(
    vm: PanelViewModel,
    onVerAgenda: () -> Unit
) {
    val citasHoy by vm.citasHoy.collectAsState()
    val pendientes by vm.pendientes.collectAsState()
    val atendidas by vm.atendidas.collectAsState()
    val ingresos by vm.ingresosMes.collectAsState()
    val porEstado by vm.porEstado.collectAsState()
    val porServicio by vm.porServicio.collectAsState()

    val fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("EEE d MMM", Locale("es", "ES")))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Cabecera
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Panel de la clínica", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "Hoy · $fechaActual", fontSize = 13.sp, color = Color.Gray)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3 Contadores principales
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TarjetitaContador("Citas hoy", citasHoy.toString(), Color(0xFFE8F0FE), ColorAzulVet, Modifier.weight(1f))
            TarjetitaContador("Pendientes", pendientes.toString(), Color(0xFFFFF8E1), Color(0xFFF57F17), Modifier.weight(1f))
            TarjetitaContador("Atendidas", atendidas.toString(), Color(0xFFECEFF1), Color(0xFF455A64), Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta azul de ingresos del mes
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ColorAzulVet),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "Ingresos del mes", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "S/ ${String.format(Locale.US, "%.2f", ingresos)}",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "$atendidas atenciones",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Citas por Estado (Resumen Simplificado)
        Text(text = "Citas por estado", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ItemEstadoFila("Pendiente", porEstado[EstadoCita.PENDIENTE.name] ?: 0, Color(0xFFE65100))
                ItemEstadoFila("Confirmada", porEstado[EstadoCita.CONFIRMADA.name] ?: 0, Color(0xFF00966D))
                ItemEstadoFila("Atendida", porEstado[EstadoCita.ATENDIDA.name] ?: 0, Color(0xFF455A64))
                ItemEstadoFila("Cancelada", porEstado[EstadoCita.CANCELADA.name] ?: 0, Color(0xFFC62828))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Citas por Servicio
        Text(text = "Citas por servicio", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (porServicio.isEmpty()) {
                    Text(text = "Sin registros de servicios", fontSize = 12.sp, color = Color.Gray)
                } else {
                    porServicio.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = item.servicio, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            Text(text = "${item.cantidad}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ColorAzulVet)
                        }
                    }
                }
            }
        }

        // Alerta de Citas Pendientes
        if (pendientes > 0) {
            Spacer(modifier = Modifier.height(20.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFE65100))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "$pendientes citas por confirmar", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFE65100))
                            Text(text = "para hoy y próximos días", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                    Button(
                        onClick = onVerAgenda,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(text = "Ver", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetitaContador(label: String, valor: String, bg: Color, textColor: Color, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bg),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = valor, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = textColor)
            Text(text = label, fontSize = 11.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ItemEstadoFila(label: String, cantidad: Int, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, fontSize = 13.sp)
        }
        Text(text = "$cantidad", fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}