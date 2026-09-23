package com.ejemplo.clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ejemplo.clinicasalud.model.Medico

@Composable
fun AgendarCitaScreen(
    medico: Medico,
    onBack: () -> Unit,
    onConfirmar: (String, String) -> Unit
) {
    var fechaSeleccionada by remember { mutableStateOf("Viernes 27") }
    var horaSeleccionada by remember { mutableStateOf("10:30 am") }

    val fechas = listOf(
        Pair("Jue", "26"),
        Pair("Vie", "27"),
        Pair("Sáb", "28")
    )

    val horas = listOf("9:00", "10:30", "3:00")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Encabezado
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Atrás",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Agendar cita",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Selecciona fecha", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.dp))

        // Botones de Fecha estilo Card/Chip
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            fechas.forEach { (dia, num) ->
                val labelCompleto = if (dia == "Jue") "Jueves 26" else if (dia == "Vie") "Viernes 27" else "Sábado 28"
                val esSel = fechaSeleccionada == labelCompleto

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (esSel) Color(0xFF5E248F) else Color(0xFFF2EFF6)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { fechaSeleccionada = labelCompleto }
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dia,
                            fontSize = 12.sp,
                            color = if (esSel) Color.White else Color.Gray
                        )
                        Text(
                            text = num,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (esSel) Color.White else Color.Black
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(text = "Selecciona hora", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.dp))

        // Botones de Hora
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            horas.forEach { hora ->
                val horaTexto = if (hora == "9:00") "9:00 am" else if (hora == "10:30") "10:30 am" else "3:00 pm"
                val esSel = horaSeleccionada == horaTexto

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (esSel) Color(0xFF5E248F) else Color(0xFFF2EFF6)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { horaSeleccionada = horaTexto }
                ) {
                    Text(
                        text = hora,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (esSel) Color.White else Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 14.dp).fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón Confirmar
        Button(
            onClick = { onConfirmar(fechaSeleccionada, horaSeleccionada) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E248F))
        ) {
            Text(
                text = "Confirmar cita",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}