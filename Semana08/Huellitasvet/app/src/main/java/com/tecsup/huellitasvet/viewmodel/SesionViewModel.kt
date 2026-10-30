package com.tecsup.huellitasvet.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: VetRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val perfilActivo: StateFlow<String> = prefs.perfilActivo
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val usuarioId: StateFlow<Int> = prefs.usuarioId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun login(
        correo: String,
        clave: String,
        rolSeleccionado: Rol,
        onExito: (String) -> Unit
    ) {
        val correoLimpio = correo.trim()

        // Validaciones rigurosas
        if (correoLimpio.isEmpty() || clave.isEmpty()) {
            _error.value = "Por favor completa todos los campos"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correoLimpio).matches()) {
            _error.value = "Ingresa un correo electrónico válido"
            return
        }

        if (clave.length < 4) {
            _error.value = "La contraseña debe tener al menos 4 caracteres"
            return
        }

        _error.value = null

        viewModelScope.launch {
            val usuario = repo.login(correoLimpio, clave)
            if (usuario == null) {
                _error.value = "Correo o contraseña incorrectos"
                return@launch
            }

            // Validar si el rol en DB coincide con el elegido en el selector
            if (usuario.rol != rolSeleccionado.name) {
                _error.value = "El usuario existe pero no pertenece al rol de ${rolSeleccionado.name}"
                return@launch
            }

            // Guardar sesión en DataStore y actualizar estado
            prefs.guardarSesion(usuario.rol, usuario.id)
            _usuarioActual.value = usuario
            _error.value = null
            onExito(usuario.rol)
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun cerrarSesion(onCierre: () -> Unit) {
        viewModelScope.launch {
            prefs.cerrarSesion()
            _usuarioActual.value = null
            onCierre()
        }
    }
}