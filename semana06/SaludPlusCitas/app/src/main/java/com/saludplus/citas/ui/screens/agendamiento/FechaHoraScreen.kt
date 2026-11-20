package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal

@Composable
fun FechaHoraScreen(
    medicoId: String,
    onContinuar: (String, String) -> Unit
) {
    var fechaSeleccionada by remember { mutableStateOf("2026-10-15") }
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    val horarios = Repositorio.horariosDisponibles(medicoId, fechaSeleccionada)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Selecciona Fecha y Hora", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = fechaSeleccionada,
            onValueChange = { fechaSeleccionada = it },
            label = { Text("Fecha (AAAA-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Horarios Disponibles:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(horarios) { hora ->
                FilterChip(
                    selected = horaSeleccionada == hora,
                    onClick = { horaSeleccionada = hora },
                    label = { Text(hora) }
                )
            }
        }

        BotonPrincipal(
            texto = "Continuar",
            enabled = horaSeleccionada != null,
            onClick = {
                horaSeleccionada?.let { hora ->
                    onContinuar(fechaSeleccionada, hora)
                }
            }
        )
    }
}