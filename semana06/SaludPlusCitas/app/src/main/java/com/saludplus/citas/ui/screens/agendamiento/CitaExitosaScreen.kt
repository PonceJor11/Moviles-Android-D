package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.ui.components.BotonPrincipal

@Composable
fun CitaExitosaScreen(
    citaId: String,
    onIrAMisCitas: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("¡Cita Agendada con Éxito!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("El código de confirmación es: $citaId")
        Spacer(modifier = Modifier.height(32.dp))

        BotonPrincipal(
            texto = "Ver Mis Citas",
            onClick = onIrAMisCitas
        )
    }
}