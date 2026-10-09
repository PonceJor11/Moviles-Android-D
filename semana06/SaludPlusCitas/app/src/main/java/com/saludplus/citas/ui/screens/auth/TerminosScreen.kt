package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TerminosScreen(
    onVolver: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Términos y Condiciones", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = """
                    1. Uso del Servicio:
                    Al registrarse en Clínica SaludPlus, usted acepta que esta aplicación gestiona agendamientos simulados en memoria para pruebas de laboratorio.
                    
                    2. Privacidad de Datos:
                    Los datos ingresados (nombre, correo) se almacenan localmente durante la sesión actual de la aplicación y no son transmitidos a servidores externos ni bases de datos persistentes.
                    
                    3. Cancelación de Citas:
                    El usuario es libre de agendar y cancelar citas médicas según la disponibilidad de horarios informada en el sistema.
                    
                    4. Notificaciones:
                    Las alertas enviadas por la app corresponden a recordatorios automáticos generados localmente a partir de la agenda activa.
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Aceptar y Volver")
        }
    }
}