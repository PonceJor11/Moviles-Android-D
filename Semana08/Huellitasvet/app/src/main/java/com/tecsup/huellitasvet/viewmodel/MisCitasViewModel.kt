package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class MisCitasViewModel(
    private val repo: VetRepository,
    val duenoId: Int
) : ViewModel() {

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    // Citas Próximas (PENDIENTE o CONFIRMADA y con fecha/hora futura)
    val proximas: StateFlow<List<CitaConDetalle>> = repo.observarCitasDeDueno(duenoId)
        .map { lista ->
            val ahora = LocalDateTime.now()
            lista.filter { item ->
                val estado = item.cita.estado
                val esActiva = estado == EstadoCita.PENDIENTE.name || estado == EstadoCita.CONFIRMADA.name
                if (!esActiva) return@filter false

                try {
                    val fechaCita = LocalDate.parse(item.cita.fecha)
                    val horaCita = LocalTime.parse(item.cita.hora)
                    val fechaHoraCita = LocalDateTime.of(fechaCita, horaCita)
                    fechaHoraCita.isAfter(ahora) || fechaHoraCita.isEqual(ahora)
                } catch (e: Exception) {
                    false
                }
            }.sortedBy { item ->
                val fechaCita = LocalDate.parse(item.cita.fecha)
                val horaCita = LocalTime.parse(item.cita.hora)
                LocalDateTime.of(fechaCita, horaCita)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Historial de Citas (ATENDIDA, CANCELADA o pasadas en tiempo)
    val historial: StateFlow<List<CitaConDetalle>> = repo.observarCitasDeDueno(duenoId)
        .map { lista ->
            val ahora = LocalDateTime.now()
            lista.filter { item ->
                val estado = item.cita.estado
                if (estado == EstadoCita.ATENDIDA.name || estado == EstadoCita.CANCELADA.name) {
                    return@filter true
                }
                try {
                    val fechaCita = LocalDate.parse(item.cita.fecha)
                    val horaCita = LocalTime.parse(item.cita.hora)
                    val fechaHoraCita = LocalDateTime.of(fechaCita, horaCita)
                    fechaHoraCita.isBefore(ahora)
                } catch (e: Exception) {
                    false
                }
            }.sortedByDescending { item ->
                val fechaCita = LocalDate.parse(item.cita.fecha)
                val horaCita = LocalTime.parse(item.cita.hora)
                LocalDateTime.of(fechaCita, horaCita)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Verificar si se permite cancelar según la regla de 2 horas
    fun puedeCancelar(citaConDetalle: CitaConDetalle): Boolean {
        val estado = citaConDetalle.cita.estado
        if (estado != EstadoCita.PENDIENTE.name && estado != EstadoCita.CONFIRMADA.name) {
            return false
        }
        return try {
            val ahora = LocalDateTime.now()
            val fechaCita = LocalDate.parse(citaConDetalle.cita.fecha)
            val horaCita = LocalTime.parse(citaConDetalle.cita.hora)
            val fechaHoraCita = LocalDateTime.of(fechaCita, horaCita)

            val diferenciaHoras = Duration.between(ahora, fechaHoraCita).toHours()
            diferenciaHoras >= 2
        } catch (e: Exception) {
            false
        }
    }

    fun cancelarCita(citaConDetalle: CitaConDetalle) {
        if (!puedeCancelar(citaConDetalle)) {
            _mensajeError.value = "Ya no puedes cancelar esta cita (debe ser con más de 2 horas de anticipación)"
            return
        }

        viewModelScope.launch {
            repo.cambiarEstadoCita(citaConDetalle.cita.id, EstadoCita.CANCELADA)
            _mensajeError.value = null
        }
    }

    fun limpiarMensaje() {
        _mensajeError.value = null
    }
}