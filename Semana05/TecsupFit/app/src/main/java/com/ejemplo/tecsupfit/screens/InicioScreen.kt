package com.ejemplo.tecsupfit.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ejemplo.tecsupfit.data.DatosFuente
import com.ejemplo.tecsupfit.model.ClaseGimnasio

@Composable
fun InicioScreen(onClaseSelect: (Int) -> Unit) {
    var filtroSeleccionado by remember { mutableStateOf("Hoy") }
    val filtros = listOf("Hoy", "Esta semana")

    val clasesFiltradas = DatosFuente.listaClases.filter {
        if (filtroSeleccionado == "Hoy") it.filtro == "Hoy" else true
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "TECSUP Fit", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(text = "Hola, Diego", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // LazyRow con chips simples
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtros) { filtro ->
                FilterChip(
                    selected = filtro == filtroSeleccionado,
                    onClick = { filtroSeleccionado = filtro },
                    label = { Text(filtro) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Clases disponibles", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        // LazyColumn con la lista de clases
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(clasesFiltradas) { clase ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onClaseSelect(clase.id) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = clase.nombre, fontWeight = FontWeight.Bold)
                        Text(text = "${clase.horario} · ${clase.sala}")
                    }
                }
            }
        }
    }
}