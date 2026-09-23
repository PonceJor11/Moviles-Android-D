package com.ejemplo.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ejemplo.tecsupfit.model.ClaseGimnasio

@Composable
fun DetalleClaseScreen(
    clase: ClaseGimnasio,
    onBack: () -> Unit,
    onReservar: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextButton(onClick = onBack) {
            Text("<- Detalle de clase")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = clase.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(text = "${clase.horario} · ${clase.sala} · ${clase.duracion}")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = clase.descripcion)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "${clase.cuposDisponibles} de ${clase.cuposTotales} cupos disponibles")

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onReservar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reservar cupo")
        }
    }
}