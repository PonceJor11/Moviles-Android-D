package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal

@Composable
fun ConfirmarCitaScreen(
    especialidadId: String,
    medicoId: String,
    fecha: String,
    hora: String,
    onConfirmado: (String) -> Unit
) {
    val esp = Repositorio.obtenerEspecialidad(especialidadId)
    val med = Repositorio.obtenerMedico(medicoId)
    val usuario = Repositorio.usuarioActual

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Resumen de tu Cita", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Paciente: ${usuario?.nombre ?: ""}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Especialidad: ${esp?.nombre ?: ""}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Médico: ${med?.nombre ?: ""}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Fecha: $fecha")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Hora: $hora")
                }
            }
        }

        BotonPrincipal(
            texto = "Confirmar y Agendar",
            onClick = {
                val nuevaCitaId = System.currentTimeMillis().toString()
                val nuevaCita = Cita(
                    id = nuevaCitaId,
                    usuarioId = usuario?.id ?: "",
                    medicoId = medicoId,
                    especialidadId = especialidadId,
                    fecha = fecha,
                    hora = hora
                )
                Repositorio.agendarCita(nuevaCita)
                onConfirmado(nuevaCitaId)
            }
        )
    }
}