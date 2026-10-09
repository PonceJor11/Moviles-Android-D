package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio

@Composable
fun MedicosScreen(
    especialidadId: String,
    onSeleccionarMedico: (String) -> Unit
) {
    val medicos = Repositorio.medicosPorEspecialidad(especialidadId)
    val esp = Repositorio.obtenerEspecialidad(especialidadId)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Médicos en ${esp?.nombre ?: ""}",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(medicos) { med ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSeleccionarMedico(med.id) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(med.nombre, style = MaterialTheme.typography.titleMedium)
                        Text(med.biografia, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("⭐ ${med.calificacion} - Horario: ${med.horarioAtencion}")
                    }
                }
            }
        }
    }
}