package com.ejemplo.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ejemplo.tecsupfit.model.Reserva

@Composable
fun ReservasScreen(reservas: List<Reserva>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Mis reservas", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(reservas) { reserva ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = reserva.nombreClase, fontWeight = FontWeight.Bold)
                        Text(text = reserva.horario)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Estado: ${reserva.estado}",
                            color = if (reserva.estado == "Confirmada") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}