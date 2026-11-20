package com.ponce.campusgps.viewmodel

import com.ponce.campusgps.data.LugaresRepository
import com.ponce.campusgps.data.PreferenciasRepository
import com.ponce.campusgps.data.UbicacionNoDisponibleException
import com.ponce.campusgps.data.UbicacionRepository
import com.ponce.campusgps.model.Lugar
import com.ponce.campusgps.model.Posicion
import com.ponce.campusgps.model.TipoError
import com.ponce.campusgps.model.UbicacionUi
import kotlin.math.abs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MapaViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    // ---- Datos de prueba: los lugares están en una línea, a 100, 200, 300 y 500 "metros" ----

    private val lugares = listOf(
        Lugar(1, "Biblioteca", "Estudio", 1.0, 0.0),
        Lugar(2, "Cafetería", "Comida", 3.0, 0.0),
        Lugar(3, "Laboratorio", "Estudio", 2.0, 0.0),
        Lugar(4, "Auditorio", "Eventos", 5.0, 0.0)
    )

    private class UbicacionFalsa(
        var resultado: Result<Posicion> = Result.success(Posicion(0.0, 0.0)),
        var playServices: Boolean = true
    ) : UbicacionRepository {
        override suspend fun obtenerPosicion(): Result<Posicion> = resultado
        override fun playServicesDisponible(): Boolean = playServices
    }

    private class LugaresFalsos(private val datos: List<Lugar>) : LugaresRepository {
        override fun obtenerLugares(): List<Lugar> = datos
    }

    private class PreferenciasFalsas(inicial: Int? = null) : PreferenciasRepository {
        private val ultimo = MutableStateFlow(inicial)
        override val ultimoLugarId: Flow<Int?> = ultimo
        override suspend fun guardarUltimoLugar(id: Int) {
            ultimo.value = id
        }
        val guardado: Int? get() = ultimo.value
    }

    private fun distanciaLineal(desde: Posicion, hasta: Lugar): Float =
        (abs(hasta.latitud - desde.latitud) * 100).toFloat()

    private fun crearVm(
        ubicacion: UbicacionFalsa = UbicacionFalsa(),
        preferencias: PreferenciasFalsas = PreferenciasFalsas()
    ) = MapaViewModel(
        ubicacionRepo = ubicacion,
        lugaresRepo = LugaresFalsos(lugares),
        preferenciasRepo = preferencias,
        calcularDistancia = this::distanciaLineal
    )

    /** Los StateFlow derivados solo se calculan mientras alguien los observa, como hace la UI. */
    private fun TestScope.observar(vm: MapaViewModel) {
        backgroundScope.launch(dispatcher) { vm.lugaresVisibles.collect { } }
        backgroundScope.launch(dispatcher) { vm.masCercano.collect { } }
        backgroundScope.launch(dispatcher) { vm.idsEnRadio.collect { } }
    }

    // ---- Distancias y lugar más cercano ----

    @Test
    fun `buscar ordena los lugares por distancia y expone el mas cercano`() = runTest(dispatcher) {
        val vm = crearVm()
        observar(vm)

        vm.buscar()

        assertTrue(vm.estado.value is UbicacionUi.Lista)
        assertEquals(listOf(1, 3, 2, 4), vm.lugares.value.map { it.lugar.id })
        assertEquals(1, vm.masCercano.value?.id)
    }

    @Test
    fun `antes de buscar no hay lugar mas cercano`() = runTest(dispatcher) {
        val vm = crearVm()
        observar(vm)

        assertNull(vm.masCercano.value)
    }

    // ---- Filtro por categoría ----

    @Test
    fun `las categorias se listan sin repetir y en orden de aparicion`() {
        assertEquals(listOf("Estudio", "Comida", "Eventos"), crearVm().categorias)
    }

    @Test
    fun `el filtro deja solo los lugares de la categoria y recalcula el mas cercano`() = runTest(dispatcher) {
        val vm = crearVm()
        observar(vm)
        vm.buscar()

        vm.filtrarPorCategoria("Estudio")
        assertEquals(listOf(1, 3), vm.lugaresVisibles.value.map { it.lugar.id })
        assertEquals(1, vm.masCercano.value?.id)

        vm.filtrarPorCategoria("Comida")
        assertEquals(listOf(2), vm.lugaresVisibles.value.map { it.lugar.id })
        assertEquals(2, vm.masCercano.value?.id)
    }

    @Test
    fun `pulsar la misma categoria quita el filtro`() = runTest(dispatcher) {
        val vm = crearVm()
        observar(vm)

        vm.filtrarPorCategoria("Estudio")
        vm.filtrarPorCategoria("Estudio")

        assertNull(vm.categoriaActiva.value)
        assertEquals(4, vm.lugaresVisibles.value.size)
    }

    @Test
    fun `el filtro deselecciona un lugar que queda oculto`() = runTest(dispatcher) {
        val vm = crearVm()
        observar(vm)
        vm.seleccionar(2)

        vm.filtrarPorCategoria("Estudio")

        assertNull(vm.seleccionado.value)
    }

    // ---- Radio de cercanía ----

    @Test
    fun `el radio marca solo los lugares que estan dentro`() = runTest(dispatcher) {
        val vm = crearVm()
        observar(vm)
        vm.buscar()

        vm.cambiarRadio(200)
        assertEquals(setOf(1, 3), vm.idsEnRadio.value)

        vm.cambiarRadio(500)
        assertEquals(setOf(1, 2, 3, 4), vm.idsEnRadio.value)

        vm.cambiarRadio(500) // la misma opción lo desactiva
        assertTrue(vm.idsEnRadio.value.isEmpty())
    }

    // ---- Estados de error ----

    @Test
    fun `permiso denegado muestra el error de permiso`() {
        val vm = crearVm()

        vm.permisoDenegado()

        assertEquals(TipoError.SIN_PERMISO, (vm.estado.value as UbicacionUi.Error).tipo)
    }

    @Test
    fun `sin Play Services no intenta buscar y avisa`() {
        val vm = crearVm(ubicacion = UbicacionFalsa(playServices = false))

        vm.buscar()

        assertEquals(TipoError.SIN_PLAY_SERVICES, (vm.estado.value as UbicacionUi.Error).tipo)
    }

    @Test
    fun `ubicacion no disponible se traduce a GPS apagado`() {
        val vm = crearVm(
            ubicacion = UbicacionFalsa(resultado = Result.failure(UbicacionNoDisponibleException()))
        )

        vm.buscar()

        assertEquals(TipoError.GPS_APAGADO, (vm.estado.value as UbicacionUi.Error).tipo)
    }

    @Test
    fun `una SecurityException se traduce a falta de permiso`() {
        val vm = crearVm(ubicacion = UbicacionFalsa(resultado = Result.failure(SecurityException())))

        vm.buscar()

        assertEquals(TipoError.SIN_PERMISO, (vm.estado.value as UbicacionUi.Error).tipo)
    }

    // ---- Último lugar visitado ----

    @Test
    fun `seleccionar guarda el ultimo lugar visitado`() {
        val preferencias = PreferenciasFalsas()
        val vm = crearVm(preferencias = preferencias)

        vm.seleccionar(3)

        assertEquals(3, preferencias.guardado)
        assertEquals(3, vm.seleccionado.value)
    }

    @Test
    fun `al iniciar recupera el ultimo lugar guardado`() {
        val vm = crearVm(preferencias = PreferenciasFalsas(inicial = 2))

        assertEquals(2, vm.seleccionado.value)
    }

    @Test
    fun `ignora un lugar guardado que ya no existe`() {
        val vm = crearVm(preferencias = PreferenciasFalsas(inicial = 99))

        assertNull(vm.seleccionado.value)
    }
}
