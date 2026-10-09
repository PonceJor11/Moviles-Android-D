package com.saludplus.citas.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio

@Composable
fun HomeScreen(
    onNavigateToEspecialidades: () -> Unit,
    onNavigateToMedicos: (String) -> Unit,
    onNavigateToNotificaciones: () -> Unit
) {
    val usuario = Repositorio.usuarioActual
    val destacadas = Repositorio.especialidadesDestacadas()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Hola,", style = MaterialTheme.typography.bodyMedium)
                Text(
                    usuario?.nombre ?: "Paciente",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            IconButton(onClick = onNavigateToNotificaciones) {
                Text("🔔")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("¿Necesitas una cita?", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onNavigateToEspecialidades) {
                    Text("Agendar Cita Ahora")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Especialidades Destacadas", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(destacadas) { item ->
                Card(
                    modifier = Modifier
                        .width(140.dp)
                        .clickable { onNavigateToMedicos(item.id) }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(item.nombre, style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            item.descripcion,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}