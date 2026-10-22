package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Horarios
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class PedirCitaViewModel(
    private val repo: VetRepository,
    val duenoId: Int
) : ViewModel() {

    // Listas cargadas
    val mascotas: StateFlow<List<Mascota>> = repo.observarMascotasDeDueno(duenoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val serviciosActivos: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selecciones de la UI
    private val _mascotaSeleccionada = MutableStateFlow<Mascota?>(null)
    val mascotaSeleccionada: StateFlow<Mascota?> = _mascotaSeleccionada.asStateFlow()

    private val _servicioSeleccionado = MutableStateFlow<Servicio?>(null)
    val servicioSeleccionado: StateFlow<Servicio?> = _servicioSeleccionado.asStateFlow()

    private val _fechaSeleccionada = MutableStateFlow(LocalDate.now().toString())
    val fechaSeleccionada: StateFlow<String> = _fechaSeleccionada.asStateFlow()

    private val _horaSeleccionada = MutableStateFlow<String?>(null)
    val horaSeleccionada: StateFlow<String?> = _horaSeleccionada.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Horas ocupadas reactivas según la fecha elegida mediante flatMapLatest
    @OptIn(ExperimentalCoroutinesApi::class)
    val horasOcupadas: StateFlow<List<String>> = _fechaSeleccionada
        .flatMapLatest { fecha -> repo.observarHorasOcupadas(fecha) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Seleccionar por defecto la primera mascota cuando se cargue la lista
        viewModelScope.launch {
            mascotas.collect { lista ->
                if (_mascotaSeleccionada.value == null && lista.isNotEmpty()) {
                    _mascotaSeleccionada.value = lista.first()
                }
            }
        }
    }

    fun seleccionarMascota(m: Mascota) {
        _mascotaSeleccionada.value = m
    }

    fun seleccionarServicio(s: Servicio) {
        _servicioSeleccionado.value = s
    }

    fun seleccionarFecha(fecha: String) {
        _fechaSeleccionada.value = fecha
        _horaSeleccionada.value = null // Limpia la hora si la fecha cambia
        _error.value = null
    }

    fun seleccionarHora(hora: String) {
        _horaSeleccionada.value = hora
        _error.value = null
    }

    fun confirmarCita(nota: String = "Cita desde app", onExito: () -> Unit) {
        val m = _mascotaSeleccionada.value
        val s = _servicioSeleccionado.value
        val f = _fechaSeleccionada.value
        val h = _horaSeleccionada.value

        if (m == null || s == null || h == null) {
            _error.value = "Por favor completa todas las selecciones"
            return
        }

        viewModelScope.launch {
            // Verificar si el horario se ocupó mientras tanto
            val ocupadasActuales = repo.observarHorasOcupadas(f).first()
            if (ocupadasActuales.contains(h)) {
                _error.value = "El horario $h ya fue reservado. Elige otra hora."
                _horaSeleccionada.value = null
                return@launch
            }

            val result = repo.crearCita(
                mascotaId = m.id,
                servicioId = s.id,
                fecha = f,
                hora = h,
                nota = nota
            )

            if (result > 0) {
                _error.value = null
                onExito()
            } else {
                _error.value = "Ocurrió un error al agendar la cita"
            }
        }
    }
}