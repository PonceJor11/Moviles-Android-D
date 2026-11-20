package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio

@Composable
fun DetalleCitaScreen(
    citaId: String,
    onVolver: () -> Unit
) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = cita?.let { Repositorio.obtenerMedico(it.medicoId) }
    val especialidad = cita?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }

    var mostrarDialogo by remember { mutableStateOf(false) }

    if (cita == null) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("No se encontró la cita especificada.")
            Button(onClick = onVolver) { Text("Volver") }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Detalle de la Cita", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Especialidad: ${especialidad?.nombre ?: "-"}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Médico: ${medico?.nombre ?: "-"}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Fecha: ${cita.fecha}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Hora: ${cita.hora}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Estado: Confirmada", color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Button(
            onClick = { mostrarDialogo = true },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar Cita")
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("¿Cancelar esta cita?") },
            text = { Text("Esta acción eliminará el agendamiento y liberará el horario.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        Repositorio.cancelarCita(citaId)
                        mostrarDialogo = false
                        onVolver()
                    }
                ) {
                    Text("Sí, cancelar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Mantener cita")
                }
            }
        )
    }
}