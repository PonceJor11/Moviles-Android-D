package com.saludplus.citas.ui.screens.resultados

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ResultadoLaboratorio(
    val id: String,
    val examen: String,
    val fecha: String,
    val estado: String
)

@Composable
fun ResultadosScreen() {
    val examenes = listOf(
        ResultadoLaboratorio("1", "Hemograma Completo", "10/09/2026", "Disponible"),
        ResultadoLaboratorio("2", "Perfil Lipídico", "15/08/2026", "Disponible"),
        ResultadoLaboratorio("3", "Glucosa en Ayunas", "02/07/2026", "Disponible")
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Resultados de Laboratorio", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(examenes) { res ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(res.examen, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Fecha del examen: ${res.fecha}")
                        Text("Estado: ${res.estado}", color = MaterialTheme.colorScheme.tertiary)
                    }
                }
            }
        }
    }
}