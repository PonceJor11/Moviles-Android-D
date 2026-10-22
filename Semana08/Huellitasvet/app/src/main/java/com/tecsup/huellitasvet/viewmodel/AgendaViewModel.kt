package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class AgendaViewModel(private val repo: VetRepository) : ViewModel() {

    private val _fechaSeleccionada = MutableStateFlow(LocalDate.now().toString())
    val fechaSeleccionada: StateFlow<String> = _fechaSeleccionada.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val citas: StateFlow<List<CitaConDetalle>> = _fechaSeleccionada
        .flatMapLatest { fecha -> repo.observarCitasPorFecha(fecha) }
        .map { lista ->
            lista.sortedBy { item ->
                try {
                    LocalTime.parse(item.cita.hora)
                } catch (e: Exception) {
                    LocalTime.MIN
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun seleccionarFecha(fecha: String) {
        _fechaSeleccionada.value = fecha
    }

    fun confirmar(citaId: Int) {
        viewModelScope.launch {
            repo.cambiarEstadoCita(citaId, EstadoCita.CONFIRMADA)
        }
    }

    fun cancelar(citaId: Int) {
        viewModelScope.launch {
            repo.cambiarEstadoCita(citaId, EstadoCita.CANCELADA)
        }
    }
}