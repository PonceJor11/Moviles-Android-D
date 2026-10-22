package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class ErroresMascota(
    val nombre: String? = null,
    val especie: String? = null,
    val anio: String? = null,
    val peso: String? = null,
    val general: String? = null
)

class MascotaViewModel(
    private val repo: VetRepository,
    val duenoId: Int
) : ViewModel() {

    private val _errores = MutableStateFlow(ErroresMascota())
    val errores: StateFlow<ErroresMascota> = _errores.asStateFlow()

    private val _mensajeBorrado = MutableStateFlow<String?>(null)
    val mensajeBorrado: StateFlow<String?> = _mensajeBorrado.asStateFlow()

    fun obtenerMascota(id: Int): StateFlow<Mascota?> = repo.observarMascota(id)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun historial(mascotaId: Int): StateFlow<List<CitaConDetalle>> = repo.observarHistorial(mascotaId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun guardar(
        id: Int,
        nombre: String,
        especie: String,
        raza: String,
        anioStr: String,
        pesoStr: String,
        sexo: String,
        onExito: () -> Unit
    ) {
        val anioActual = LocalDate.now().year
        val anio = anioStr.toIntOrNull()
        val peso = pesoStr.toDoubleOrNull()

        var errNombre: String? = null
        var errEspecie: String? = null
        var errAnio: String? = null
        var errPeso: String? = null

        if (nombre.trim().length < 2) errNombre = "El nombre debe tener al menos 2 caracteres"
        if (especie.isEmpty()) errEspecie = "Selecciona una especie"
        if (anio == null || anio < 1990 || anio > anioActual) errAnio = "Año entre 1990 y $anioActual"
        if (peso == null || peso <= 0) errPeso = "Peso mayor a 0"

        if (errNombre != null || errEspecie != null || errAnio != null || errPeso != null) {
            _errores.value = ErroresMascota(errNombre, errEspecie, errAnio, errPeso)
            return
        }

        _errores.value = ErroresMascota()

        viewModelScope.launch {
            val mascota = Mascota(
                id = id,
                duenoId = duenoId,
                nombre = nombre.trim(),
                especie = especie,
                raza = raza.ifBlank { "Mestizo" },
                anioNacimiento = anio!!,
                peso = peso!!,
                sexo = sexo
            )
            repo.guardarMascota(mascota)
            onExito()
        }
    }

    fun eliminar(mascota: Mascota, onExito: () -> Unit) {
        viewModelScope.launch {
            if (repo.mascotaTieneCitas(mascota.id)) {
                _mensajeBorrado.value = "No se puede eliminar la mascota porque tiene citas registradas"
            } else {
                repo.eliminarMascota(mascota)
                _mensajeBorrado.value = null
                onExito()
            }
        }
    }

    fun limpiarMensajeBorrado() {
        _mensajeBorrado.value = null
    }
}