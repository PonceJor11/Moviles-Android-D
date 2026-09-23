package com.ejemplo.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ejemplo.clinicasalud.model.Medico

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilMedicoScreen(
    medico: Medico,
    onBack: () -> Unit,
    onAgendar: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del médico") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(medico.nombre, style = MaterialTheme.typography.headlineSmall)
            Text("${medico.especialidad} · ${medico.experiencia}")
            Text("★ ${medico.calificacion} (${medico.resenas} reseñas)")

            Spacer(modifier = Modifier.height(24.dp))
            Text(medico.descripcion, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onAgendar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agendar cita")
            }
        }
    }
}