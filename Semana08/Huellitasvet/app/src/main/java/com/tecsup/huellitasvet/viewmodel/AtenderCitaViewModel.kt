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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ErroresAtencion(
    val diagnostico: String? = null,
    val peso: String? = null
)

class AtenderCitaViewModel(
    private val repo: VetRepository,
    val citaId: Int
) : ViewModel() {

    val cita: StateFlow<CitaConDetalle?> = repo.observarCita(citaId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _errores = MutableStateFlow(ErroresAtencion())
    val errores: StateFlow<ErroresAtencion> = _errores.asStateFlow()

    fun registrarAtencion(
        diagnostico: String,
        tratamiento: String,
        pesoStr: String,
        onExito: () -> Unit
    ) {
        val peso = pesoStr.toDoubleOrNull()
        var errDiag: String? = null
        var errPeso: String? = null

        if (diagnostico.trim().length < 5) {
            errDiag = "El diagnóstico debe tener al menos 5 caracteres"
        }
        if (peso == null || peso <= 0) {
            errPeso = "Ingresa un peso válido mayor a 0"
        }

        if (errDiag != null || errPeso != null) {
            _errores.value = ErroresAtencion(errDiag, errPeso)
            return
        }

        _errores.value = ErroresAtencion()

        viewModelScope.launch {
            repo.registrarAtencion(
                citaId = citaId,
                diagnostico = diagnostico.trim(),
                tratamiento = tratamiento.trim(),
                peso = peso!!
            )
            onExito()
        }
    }

    fun cancelarCita(onExito: () -> Unit) {
        viewModelScope.launch {
            repo.cambiarEstadoCita(citaId, EstadoCita.CANCELADA)
            onExito()
        }
    }
}