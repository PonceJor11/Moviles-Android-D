package com.ponce.avisoscampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ponce.avisoscampus.model.Aviso
import com.ponce.avisoscampus.model.Origen

/** Tarjeta Historial. No conoce el ViewModel: solo recibe la lista. */
@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    SeccionCard(titulo = "Historial", icono = Icons.Filled.Info) {
        if (avisos.isEmpty()) {
            Text(
                "Sin avisos todavía",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            val visibles = avisos.take(20)
            visibles.forEachIndexed { indice, aviso ->
                FilaAviso(aviso)
                if (indice < visibles.lastIndex) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}

@Composable
private fun FilaAviso(aviso: Aviso) {
    val (fondo, texto) = coloresOrigen(aviso.origen)
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .background(fondo, CircleShape)
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) {
                Text(
                    aviso.origen.texto,
                    color = texto,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                aviso.hora,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(aviso.titulo, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        Text(
            aviso.texto,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun coloresOrigen(origen: Origen): Pair<Color, Color> = when (origen) {
    Origen.BOTON -> Color(0xFFEBDACB) to Color(0xFF6F4A30)
    Origen.RECORDATORIO -> Color(0xFFFFEBC2) to Color(0xFF8A5A00)
    Origen.MODO_AVION -> Color(0xFFDDE8EE) to Color(0xFF3F5866)
}
