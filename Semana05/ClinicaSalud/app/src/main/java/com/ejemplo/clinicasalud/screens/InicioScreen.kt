package com.ejemplo.clinicasalud.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ejemplo.clinicasalud.data.DatosFuente

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    onOpenDrawer: () -> Unit,
    onMedicoSelect: (Int) -> Unit
) {
    var especialidadSeleccionada by remember { mutableStateOf("Cardiología") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clínica Salud+\nHola, Juan") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listOf("Cardiología", "Pediatría", "Dermatología")) { esp ->
                    FilterChip(
                        selected = (especialidadSeleccionada == esp),
                        onClick = { especialidadSeleccionada = esp },
                        label = { Text(esp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Médicos disponibles", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(DatosFuente.listaMedicos) { medico ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMedicoSelect(medico.id) }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(medico.nombre, style = MaterialTheme.typography.titleMedium)
                                Text(medico.especialidad, style = MaterialTheme.typography.bodyMedium)
                            }
                            Text("★ ${medico.calificacion}")
                        }
                    }
                }
            }
        }
    }
}