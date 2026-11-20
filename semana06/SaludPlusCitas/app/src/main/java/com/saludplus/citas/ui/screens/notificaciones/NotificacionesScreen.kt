package com.saludplus.citas.ui.screens.notificaciones

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio

@Composable
fun NotificacionesScreen() {
    val usuario = Repositorio.usuarioActual
    val citas = usuario?.let { Repositorio.citasDelUsuario(it.id) } ?: emptyList()

    val notificaciones = citas.map { cita ->
        val med = Repositorio.obtenerMedico(cita.medicoId)
        "Recordatorio: Tienes una cita programada con ${med?.nombre ?: "tu médico"} el ${cita.fecha} a las ${cita.hora}."
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Notificaciones", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        if (notificaciones.isEmpty()) {
            Text("No tienes notificaciones pendientes.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(notificaciones) { mensaje ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Text("🔔 ", style = MaterialTheme.typography.titleMedium)
                            Text(mensaje, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}