package com.ponce.lab03registronotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                PantallaPrincipal()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Registro de Notas",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        PantallaRegistroNotas(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun PantallaRegistroNotas(modifier: Modifier = Modifier) {
    // Estados para las 4 notas
    var nota1 by remember { mutableFloatStateOf(0f) }
    var nota2 by remember { mutableFloatStateOf(0f) }
    var nota3 by remember { mutableFloatStateOf(0f) }
    var nota4 by remember { mutableFloatStateOf(0f) }

    val degradado = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            MaterialTheme.colorScheme.surface
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(degradado)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Notas del ciclo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Desliza para asignar cada nota (0 a 20)",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Sliders para los 4 cursos
            CursoSliderItem("Fundamentos de Programación", 20, nota1) { nota1 = it }
            CursoSliderItem("Programación Orientada a Objetos", 25, nota2) { nota2 = it }
            CursoSliderItem("Programación en Móviles", 30, nota3) { nota3 = it }
            CursoSliderItem("Base de Datos", 25, nota4) { nota4 = it }
        }

        Text(
            text = "Desarrollado por: Jordy Ponce Huarancca",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        )
    }
}

@Composable
fun CursoSliderItem(
    nombre: String,
    peso: Int,
    nota: Float,
    onNotaChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 2.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Text(text = nombre, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "($peso%)", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            }
            Box(
                modifier = Modifier
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "${nota.toInt()}",
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Slider(
            value = nota,
            onValueChange = onNotaChange,
            valueRange = 0f..20f,
            steps = 19
        )
    }
}