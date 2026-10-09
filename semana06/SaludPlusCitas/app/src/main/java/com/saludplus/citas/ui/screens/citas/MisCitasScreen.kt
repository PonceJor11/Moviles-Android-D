package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio

@Composable
fun MisCitasScreen(
    onVerDetalle: (String) -> Unit
) {
    val usuario = Repositorio.usuarioActual
    val lista = usuario?.let { Repositorio.citasDelUsuario(it.id) } ?: emptyList()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mis Citas Agendadas", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        if (lista.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No tienes citas registradas.")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(lista) { cita ->
                    val med = Repositorio.obtenerMedico(cita.medicoId)
                    val esp = Repositorio.obtenerEspecialidad(cita.especialidadId)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onVerDetalle(cita.id) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(esp?.nombre ?: "", style = MaterialTheme.typography.titleMedium)
                            Text(med?.nombre ?: "")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Fecha: ${cita.fecha} - Hora: ${cita.hora}")
                        }
                    }
                }
            }
        }
    }
}