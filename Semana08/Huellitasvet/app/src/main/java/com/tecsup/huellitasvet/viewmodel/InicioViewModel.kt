package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class InicioViewModel(
    private val repo: VetRepository,
    val duenoId: Int
) : ViewModel() {

    // Lista de mascotas del dueño
    val mascotas: StateFlow<List<Mascota>> = repo.observarMascotasDeDueno(duenoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cálculo de la próxima cita activa
    val proximaCita: StateFlow<CitaConDetalle?> = repo.observarCitasDeDueno(duenoId)
        .map { lista ->
            val ahora = LocalDateTime.now()
            lista.filter { item ->
                val estado = item.cita.estado
                val esValida = estado == EstadoCita.PENDIENTE.name || estado == EstadoCita.CONFIRMADA.name
                if (!esValida) return@filter false

                try {
                    val fechaCita = LocalDate.parse(item.cita.fecha)
                    val horaCita = LocalTime.parse(item.cita.hora)
                    val fechaHoraCita = LocalDateTime.of(fechaCita, horaCita)
                    fechaHoraCita.isAfter(ahora) || fechaHoraCita.isEqual(ahora)
                } catch (e: Exception) {
                    false
                }
            }.minByOrNull { item ->
                val fechaCita = LocalDate.parse(item.cita.fecha)
                val horaCita = LocalTime.parse(item.cita.hora)
                LocalDateTime.of(fechaCita, horaCita)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}