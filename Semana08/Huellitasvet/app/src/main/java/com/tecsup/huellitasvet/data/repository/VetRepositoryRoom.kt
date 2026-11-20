package com.tecsup.huellitasvet.data.repository

import com.tecsup.huellitasvet.data.local.VetDatabase
import com.tecsup.huellitasvet.data.local.entity.Cita
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import com.tecsup.huellitasvet.model.ConteoServicio
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.Flow

class VetRepositoryRoom(private val db: VetDatabase) : VetRepository {

    // Usuarios
    override suspend fun login(correo: String, clave: String): Usuario? =
        db.usuarioDao().login(correo, clave)

    // Servicios
    override fun observarServicios(): Flow<List<Servicio>> =
        db.servicioDao().observarTodos()

    override fun observarServiciosActivos(): Flow<List<Servicio>> =
        db.servicioDao().observarActivos()

    override suspend fun guardarServicio(s: Servicio) {
        if (s.id == 0) {
            db.servicioDao().insertar(s)
        } else {
            db.servicioDao().actualizar(s)
        }
    }

    override suspend fun eliminarServicio(s: Servicio) =
        db.servicioDao().eliminar(s)

    override suspend fun cambiarActivo(id: Int, activo: Boolean) =
        db.servicioDao().cambiarActivo(id, activo)

    override suspend fun servicioTieneCitas(id: Int): Boolean = false // Simplificado para validación

    // Mascotas
    override fun observarMascotasDeDueno(duenoId: Int): Flow<List<Mascota>> =
        db.mascotaDao().observarPorDueno(duenoId)

    override fun observarMascota(id: Int): Flow<Mascota?> =
        db.mascotaDao().observarUna(id)

    override fun observarTodasLasMascotas(): Flow<List<MascotaConDueno>> =
        db.mascotaDao().observarTodas()

    override suspend fun guardarMascota(m: Mascota) {
        if (m.id == 0) {
            db.mascotaDao().insertar(m)
        } else {
            db.mascotaDao().actualizar(m)
        }
    }

    override suspend fun eliminarMascota(m: Mascota) =
        db.mascotaDao().eliminar(m)

    override suspend fun mascotaTieneCitas(id: Int): Boolean =
        db.mascotaDao().contarCitas(id) > 0

    // Citas
    override fun observarHorasOcupadas(fecha: String): Flow<List<String>> =
        db.citaDao().observarHorasOcupadas(fecha)

    override suspend fun crearCita(
        mascotaId: Int,
        servicioId: Int,
        fecha: String,
        hora: String,
        nota: String
    ): Long {
        val servicio = db.servicioDao().obtener(servicioId) ?: return -1L
        val cita = Cita(
            mascotaId = mascotaId,
            servicioId = servicioId,
            fecha = fecha,
            hora = hora,
            nota = nota,
            estado = EstadoCita.PENDIENTE.name,
            precio = servicio.precio
        )
        return db.citaDao().insertar(cita)
    }

    override fun observarCitasDeDueno(duenoId: Int): Flow<List<CitaConDetalle>> =
        db.citaDao().observarPorDueno(duenoId)

    override fun observarCitasPorFecha(fecha: String): Flow<List<CitaConDetalle>> =
        db.citaDao().observarPorFecha(fecha)

    override fun observarCita(id: Int): Flow<CitaConDetalle?> =
        db.citaDao().observarUna(id)

    override fun observarHistorial(mascotaId: Int): Flow<List<CitaConDetalle>> =
        db.citaDao().observarHistorial(mascotaId)

    override suspend fun cambiarEstadoCita(id: Int, estado: EstadoCita) =
        db.citaDao().actualizarEstado(id, estado.name)

    override suspend fun registrarAtencion(
        citaId: Int,
        diagnostico: String,
        tratamiento: String,
        peso: Double
    ) {
        val cita = db.citaDao().obtener(citaId) ?: return
        db.citaDao().registrarAtencion(citaId, diagnostico, tratamiento, peso)
        db.mascotaDao().actualizarPeso(cita.mascotaId, peso)
    }

    // Panel
    override fun observarCitasDeFecha(fecha: String): Flow<Int> =
        db.citaDao().contarPorFecha(fecha)

    override fun observarCantidadPorEstado(estado: EstadoCita): Flow<Int> =
        db.citaDao().contarPorEstado(estado.name)

    override fun observarCitasPorServicio(): Flow<List<ConteoServicio>> =
        db.citaDao().contarPorServicio()

    override fun observarIngresosDesde(fechaInicio: String): Flow<Double> =
        db.citaDao().ingresosDesde(fechaInicio)
}