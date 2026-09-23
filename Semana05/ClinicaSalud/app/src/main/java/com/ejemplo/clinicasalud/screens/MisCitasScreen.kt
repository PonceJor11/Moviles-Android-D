package com.ejemplo.clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ejemplo.clinicasalud.model.Cita

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCitasScreen(
    citas: List<Cita>,
    onOpenDrawer: () -> Unit,
    onCancelarCita: (Cita) -> Unit
) {
    var citaParaCancelar by remember { mutableStateOf<Cita?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onOpenDrawer) {
                Icon(Icons.Default.Menu, contentDescription = "Menú")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Mis citas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(citas) { cita ->
                TarjetaCitaEstiloImagen(
                    cita = cita,
                    onCancelar = { citaParaCancelar = cita }
                )
            }
        }

        // Diálogo de Confirmación para Cancelar Cita
        citaParaCancelar?.let { cita ->
            AlertDialog(
                onDismissRequest = { citaParaCancelar = null },
                title = { Text("Cancelar Cita", fontWeight = FontWeight.Bold) },
                text = { Text("¿Deseas cancelar tu cita con ${cita.medicoNombre} para el ${cita.fecha}?") },
                confirmButton = {
                    Button(
                        onClick = {
                            onCancelarCita(cita)
                            citaParaCancelar = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Sí, cancelar")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { citaParaCancelar = null }) {
                        Text("No")
                    }
                }
            )
        }
    }
}

@Composable
fun TarjetaCitaEstiloImagen(
    cita: Cita,
    onCancelar: () -> Unit
) {
    val esConfirmada = cita.estado == "Confirmada"
    val colorBordeIzquierdo = if (esConfirmada) Color(0xFF5E248F) else Color.Transparent

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F2F9)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Línea morada indicadora a la izquierda (como en la figura 2)
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(colorBordeIzquierdo)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = cita.medicoNombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "${cita.fecha}, ${cita.hora}",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Badge / Chip de Estado
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (esConfirmada) Color(0xFFE2F3EB) else Color(0xFFEFEFEF)
                    ) {
                        Text(
                            text = cita.estado,
                            color = if (esConfirmada) Color(0xFF0F8A5F) else Color.Gray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    // Botón para cancelar si está confirmada
                    if (esConfirmada) {
                        IconButton(onClick = onCancelar, modifier = Modifier.size(24.dp)) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Cancelar",
                                tint = Color.Red.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}