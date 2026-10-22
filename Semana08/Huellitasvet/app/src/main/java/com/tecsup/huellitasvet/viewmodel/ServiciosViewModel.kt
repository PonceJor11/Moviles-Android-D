package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ErroresServicio(
    val nombre: String? = null,
    val precio: String? = null
)

class ServiciosViewModel(private val repo: VetRepository) : ViewModel() {

    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _errores = MutableStateFlow(ErroresServicio())
    val errores: StateFlow<ErroresServicio> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun alternar(s: Servicio) {
        viewModelScope.launch {
            repo.cambiarActivo(s.id, !s.activo)
        }
    }

    fun guardar(
        id: Int,
        nombre: String,
        duracionMin: Int,
        precioStr: String,
        descripcion: String,
        icono: String,
        activo: Boolean,
        onExito: () -> Unit
    ) {
        val precio = precioStr.toDoubleOrNull()
        var errNombre: String? = null
        var errPrecio: String? = null

        if (nombre.trim().isEmpty()) errNombre = "El nombre es obligatorio"
        if (precio == null || precio <= 0) errPrecio = "Ingresa un precio mayor a 0"

        if (errNombre != null || errPrecio != null) {
            _errores.value = ErroresServicio(errNombre, errPrecio)
            return
        }

        _errores.value = ErroresServicio()

        viewModelScope.launch {
            val servicio = Servicio(
                id = id,
                nombre = nombre.trim(),
                descripcion = descripcion.trim(),
                duracionMin = duracionMin,
                precio = precio!!,
                activo = activo,
                icono = icono
            )
            repo.guardarServicio(servicio)
            onExito()
        }
    }

    fun eliminar(servicio: Servicio) {
        viewModelScope.launch {
            if (repo.servicioTieneCitas(servicio.id)) {
                _mensaje.value = "Un servicio con citas registradas no se puede eliminar; ha sido desactivado."
                repo.cambiarActivo(servicio.id, false)
            } else {
                repo.eliminarServicio(servicio)
                _mensaje.value = null
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}