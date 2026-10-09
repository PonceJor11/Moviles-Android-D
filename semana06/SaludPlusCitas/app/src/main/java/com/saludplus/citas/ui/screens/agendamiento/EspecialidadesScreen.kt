package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.CampoTextoSaludPlus

@Composable
fun EspecialidadesScreen(
    onSeleccionarEspecialidad: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val lista = Repositorio.buscarEspecialidades(query)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Selecciona una Especialidad", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

        CampoTextoSaludPlus(
            value = query,
            onValueChange = { query = it },
            label = "Buscar especialidad..."
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(lista) { esp ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSeleccionarEspecialidad(esp.id) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(esp.nombre, style = MaterialTheme.typography.titleMedium)
                        Text(esp.descripcion, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}