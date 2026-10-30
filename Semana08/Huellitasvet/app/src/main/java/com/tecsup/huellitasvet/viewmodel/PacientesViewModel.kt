package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Especie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class PacientesViewModel(private val repo: VetRepository) : ViewModel() {

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _filtroEspecie = MutableStateFlow("TODOS")
    val filtroEspecie: StateFlow<String> = _filtroEspecie.asStateFlow()

    val pacientes: StateFlow<List<MascotaConDueno>> = combine(
        repo.observarTodasLasMascotas(),
        _busqueda,
        _filtroEspecie
    ) { lista, query, especie ->
        lista.filter { item ->
            val coincideTexto = item.mascota.nombre.contains(query, ignoreCase = true) ||
                    item.dueno.nombre.contains(query, ignoreCase = true)

            val coincideEspecie = when (especie) {
                "PERRO" -> item.mascota.especie == Especie.PERRO.name
                "GATO" -> item.mascota.especie == Especie.GATO.name
                "OTRO" -> item.mascota.especie == Especie.OTRO.name
                else -> true
            }

            coincideTexto && coincideEspecie
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun cambiarBusqueda(query: String) {
        _busqueda.value = query
    }

    fun cambiarFiltroEspecie(especie: String) {
        _filtroEspecie.value = especie
    }
}