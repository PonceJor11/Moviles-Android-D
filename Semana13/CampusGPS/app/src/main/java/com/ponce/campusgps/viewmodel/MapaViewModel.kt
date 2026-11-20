package com.ponce.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ponce.campusgps.data.LugaresRepository
import com.ponce.campusgps.data.PreferenciasRepository
import com.ponce.campusgps.data.UbicacionNoDisponibleException
import com.ponce.campusgps.data.UbicacionRepository
import com.ponce.campusgps.model.Lugar
import com.ponce.campusgps.model.LugarConDistancia
import com.ponce.campusgps.model.Posicion
import com.ponce.campusgps.model.TipoError
import com.ponce.campusgps.model.UbicacionUi
import com.ponce.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository,
    private val preferenciasRepo: PreferenciasRepository,
    // Inyectable para poder probar el ViewModel sin el framework de Android.
    private val calcularDistancia: (Posicion, Lugar) -> Float = ::distanciaMetros
) : ViewModel() {

    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) }
    )
    val lugares: StateFlow<List<LugarConDistancia>> = _lugares.asStateFlow()

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    /** Categorías disponibles, en el orden en que aparecen en los datos. */
    val categorias: List<String> = lugaresRepo.obtenerLugares().map { it.categoria }.distinct()

    private val _categoria = MutableStateFlow<String?>(null)
    val categoriaActiva: StateFlow<String?> = _categoria.asStateFlow()

    /** Lugares que pasan el filtro de categoría: es lo que se dibuja en el mapa y en la lista. */
    val lugaresVisibles: StateFlow<List<LugarConDistancia>> =
        combine(_lugares, _categoria) { lista, categoria -> aplicarFiltro(lista, categoria) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), _lugares.value)

    /** Radios de cercanía que ofrece la interfaz, en metros. */
    val radios: List<Int> = listOf(100, 200, 500)

    private val _radio = MutableStateFlow<Int?>(null)
    val radioActivo: StateFlow<Int?> = _radio.asStateFlow()

    /** Ids de los lugares visibles que están dentro del radio elegido (vacío si no hay radio). */
    val idsEnRadio: StateFlow<Set<Int>> = combine(lugaresVisibles, _radio) { lista, radio ->
        if (radio == null) {
            emptySet<Int>()
        } else {
            lista.filter { (it.metros ?: Float.MAX_VALUE) <= radio }.map { it.lugar.id }.toSet()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    /** Lugar más cercano entre los visibles; null mientras no se conozca la ubicación. */
    val masCercano: StateFlow<Lugar?> = lugaresVisibles
        .map { lista -> lista.firstOrNull { it.metros != null }?.lugar }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    init {
        // Recupera el último lugar visitado, siempre que siga existiendo en los datos.
        viewModelScope.launch {
            val id = preferenciasRepo.ultimoLugarId.first()
            val existe = lugaresRepo.obtenerLugares().any { it.id == id }
            if (id != null && existe && _seleccionado.value == null) {
                _seleccionado.value = id
            }
        }
    }

    fun buscar() {
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error(
                mensaje = "Este dispositivo no tiene Google Play Services",
                tipo = TipoError.SIN_PLAY_SERVICES
            )
            return
        }

        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess {
                    _estado.value = UbicacionUi.Lista(it)
                    actualizarDistancias(it)
                }
                .onFailure { _estado.value = errorDe(it) }
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            mensaje = "Sin permiso de ubicación no puedo mostrar dónde estás",
            tipo = TipoError.SIN_PERMISO
        )
    }

    fun seleccionar(id: Int?) {
        _seleccionado.value = id
        if (id != null) {
            viewModelScope.launch { preferenciasRepo.guardarUltimoLugar(id) }
        }
    }

    /** Activa el radio de [metros]; pulsarlo de nuevo (o pasar null) lo quita. */
    fun cambiarRadio(metros: Int?) {
        _radio.value = if (metros == _radio.value) null else metros
    }

    /** Activa el filtro de [categoria]; pulsarla de nuevo (o pasar null) lo quita. */
    fun filtrarPorCategoria(categoria: String?) {
        val nueva = if (categoria == _categoria.value) null else categoria
        _categoria.value = nueva

        // Si el lugar seleccionado queda oculto por el filtro, se deselecciona.
        val id = _seleccionado.value
        val sigueVisible = lugaresRepo.obtenerLugares()
            .any { it.id == id && (nueva == null || it.categoria == nueva) }
        if (id != null && !sigueVisible) _seleccionado.value = null
    }

    private fun aplicarFiltro(
        lista: List<LugarConDistancia>,
        categoria: String?
    ): List<LugarConDistancia> =
        if (categoria == null) lista else lista.filter { it.lugar.categoria == categoria }

    private fun actualizarDistancias(posicion: Posicion) {
        val ordenados = lugaresRepo.obtenerLugares()
            .map { LugarConDistancia(it, calcularDistancia(posicion, it)) }
            .sortedBy { it.metros }
        _lugares.value = ordenados
    }

    private fun errorDe(e: Throwable): UbicacionUi.Error = when (e) {
        is SecurityException -> UbicacionUi.Error(
            mensaje = "Falta el permiso de ubicación. Actívalo desde los ajustes de la app",
            tipo = TipoError.SIN_PERMISO
        )
        is UbicacionNoDisponibleException -> UbicacionUi.Error(
            mensaje = "Activa el GPS e intenta de nuevo",
            tipo = TipoError.GPS_APAGADO
        )
        else -> UbicacionUi.Error(
            mensaje = e.message ?: "No se pudo obtener la ubicación",
            tipo = TipoError.OTRO
        )
    }
}
