package com.ejemplo.tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Color corporativo de TECSUP
val TecsupGreen = Color(0xFF0D6951)
val TecsupGreenBg = Color(0xFFE6F2EF)

data class Reserva(
    val id: Int,
    val disciplina: String,
    val horario: String,
    val instructor: String,
    val estado: String
)

@Composable
fun ReservasScreen() {
    val listaReservas = listOf(
        Reserva(1, "Spinning Fit", "08:00 AM - 09:00 AM", "Coach Carlos", "Confirmada"),
        Reserva(2, "Musculación & Pesas", "10:00 AM - 11:30 AM", "Coach Maria", "Confirmada"),
        Reserva(3, "Crossfit Tecsup", "05:00 PM - 06:00 PM", "Coach Alex", "Pendiente")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Reservas de Gimnasio",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TecsupGreen,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listaReservas) { reserva ->
                TarjetaReservaMejorada(reserva)
            }
        }
    }
}

@Composable
fun TarjetaReservaMejorada(reserva: Reserva) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador lateral verde para la mejora con IA
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
                    .background(if (reserva.estado == "Confirmada") TecsupGreen else Color.Gray)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = reserva.disciplina,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Horario: ${reserva.horario}",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "Instructor: ${reserva.instructor}",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            // Chip de Estado de Reserva
            Surface(
                color = if (reserva.estado == "Confirmada") TecsupGreenBg else Color(0xFFF0F0F0),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text(
                    text = reserva.estado,
                    color = if (reserva.estado == "Confirmada") TecsupGreen else Color.DarkGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}