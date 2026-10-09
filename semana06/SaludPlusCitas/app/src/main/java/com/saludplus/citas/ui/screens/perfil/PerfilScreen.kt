package com.saludplus.citas.ui.screens.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio

@Composable
fun PerfilScreen(
    onCerrarSesion: () -> Unit
) {
    val usuario = Repositorio.usuarioActual

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Perfil de Usuario", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))

            Text("Nombre:", style = MaterialTheme.typography.labelLarge)
            Text(usuario?.nombre ?: "", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Correo:", style = MaterialTheme.typography.labelLarge)
            Text(usuario?.correo ?: "", style = MaterialTheme.typography.bodyLarge)
        }

        Button(
            onClick = {
                Repositorio.cerrarSesion()
                onCerrarSesion()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar Sesión")
        }
    }
}