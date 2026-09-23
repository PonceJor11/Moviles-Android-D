package com.ejemplo.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ejemplo.clinicasalud.model.Cita

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCitasScreen(
    citas: List<Cita>,
    onOpenDrawer: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis citas") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(citas) { cita ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(cita.medicoNombre, style = MaterialTheme.typography.titleMedium)
                        Text("${cita.fecha}, ${cita.hora}")
                        Spacer(modifier = Modifier.height(8.dp))
                        AssistChip(
                            onClick = {},
                            label = { Text(cita.estado) }
                        )
                    }
                }
            }
        }
    }
}