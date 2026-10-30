package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun GraficoPesosCanvas(pesos: List<Double>, modifier: Modifier = Modifier) {
    if (pesos.size < 2) {
        Text("Se necesitan al menos 2 atenciones con peso registrado para el gráfico.")
        return
    }

    val lineColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier.fillMaxWidth().height(150.dp)) {
        val maxPeso = (pesos.maxOrNull() ?: 1.0).toFloat()
        val minPeso = (pesos.minOrNull() ?: 0.0).toFloat()
        val range = if (maxPeso == minPeso) 1f else maxPeso - minPeso

        val widthStep = size.width / (pesos.size - 1)
        val path = Path()

        pesos.forEachIndexed { index, peso ->
            val x = index * widthStep
            val y = size.height - ((peso.toFloat() - minPeso) / range * (size.height - 40f) + 20f)

            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
            drawCircle(color = lineColor, radius = 8f, center = Offset(x, y))
        }

        drawPath(path = path, color = lineColor, style = Stroke(width = 4f))
    }
}