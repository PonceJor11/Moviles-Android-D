package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.ConteoServicio
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class PanelViewModel(private val repo: VetRepository) : ViewModel() {

    private val hoy = LocalDate.now().toString()
    private val inicioMes = LocalDate.now().withDayOfMonth(1).toString()

    val citasHoy: StateFlow<Int> = repo.observarCitasDeFecha(hoy)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val pendientes: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoCita.PENDIENTE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val atendidas: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoCita.ATENDIDA)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val ingresosMes: StateFlow<Double> = repo.observarIngresosDesde(inicioMes)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val porServicio: StateFlow<List<ConteoServicio>> = repo.observarCitasPorServicio()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val porEstado: StateFlow<Map<String, Int>> = combine(
        repo.observarCantidadPorEstado(EstadoCita.PENDIENTE),
        repo.observarCantidadPorEstado(EstadoCita.CONFIRMADA),
        repo.observarCantidadPorEstado(EstadoCita.ATENDIDA),
        repo.observarCantidadPorEstado(EstadoCita.CANCELADA)
    ) { p, c, a, ca ->
        mapOf(
            EstadoCita.PENDIENTE.name to p,
            EstadoCita.CONFIRMADA.name to c,
            EstadoCita.ATENDIDA.name to a,
            EstadoCita.CANCELADA.name to ca
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
}