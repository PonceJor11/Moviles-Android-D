package com.tecsup.huellitasvet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import com.tecsup.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class BuscarPacientesViewModel(private val repository: VetRepository) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _especie = MutableStateFlow("")
    val especie: StateFlow<String> = _especie.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val pacientes: StateFlow<List<MascotaConDueno>> = combine(_query, _especie) { q, e ->
        Pair(q, e)
    }.flatMapLatest { (q, e) ->
        repository.buscarPacientes(q, e)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onQueryChanged(newQuery: String) { _query.value = newQuery }
    fun onEspecieChanged(newEspecie: String) { _especie.value = newEspecie }
}