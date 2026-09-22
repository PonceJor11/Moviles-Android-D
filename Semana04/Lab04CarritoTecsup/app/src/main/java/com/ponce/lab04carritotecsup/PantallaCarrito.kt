package com.ponce.lab04carritotecsup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PantallaCarrito() {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }

    val productos = remember { mutableStateListOf<Producto>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Formulario
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                label = { Text("Cantidad") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val precioNum = precio.toDoubleOrNull() ?: 0.0
                val cantidadNum = cantidad.toIntOrNull() ?: 0

                if (nombre.isNotBlank() && precioNum > 0 && cantidadNum > 0) {
                    productos.add(Producto(nombre, precioNum, cantidadNum))
                    nombre = ""
                    precio = ""
                    cantidad = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("AGREGAR")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Etapa 3: Lista de productos en LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(productos) { producto ->
                TarjetaProducto(
                    producto = producto,
                    onEliminar = { productos.remove(producto) }
                )
            }
        }
    }
}

// Composable TarjetaProducto completado
@Composable
fun TarjetaProducto(producto: Producto, onEliminar: () -> Unit) {
    val importe = producto.precio * producto.cantidad

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Text nombre (titleMedium, negrita)
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                // Text "S/ precio x cantidad" (gris)
                Text(
                    text = "S/ ${String.format("%.2f", producto.precio)} x ${producto.cantidad}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            // Text del importe (precio x cantidad, 2 decimales)
            Text(
                text = "S/ ${String.format("%.2f", importe)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp)
            )

            // Botón de eliminar (Usando TextButton o Button para evitar conflicto de librería de íconos)
            TextButton(
                onClick = onEliminar,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Eliminar", fontWeight = FontWeight.Bold)
            }
        }
    }
}