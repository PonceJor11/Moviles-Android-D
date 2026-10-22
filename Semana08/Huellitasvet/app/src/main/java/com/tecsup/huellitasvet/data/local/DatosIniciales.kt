package com.tecsup.huellitasvet.data.local

import com.tecsup.huellitasvet.data.local.entity.Cita
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.model.Sexo
import java.time.LocalDate

object DatosIniciales {

    suspend fun sembrar(db: VetDatabase) {
        if (db.usuarioDao().contar() > 0) return

        // 1. Insertar Servicios
        val servicios = listOf(
            Servicio(1, "Consulta General", "Examen físico completo y diagnóstico inicial", 30, 50.0, true, "heart"),
            Servicio(2, "Vacunación Sextuple", "Inmunización contra moquillo, parvovirus y más", 20, 65.0, true, "syringe"),
            Servicio(3, "Corte y Baño", "Higiene integral, lavado, secado y arreglo de pelaje", 60, 45.0, true, "scissors"),
            Servicio(4, "Desparasitación", "Tratamiento interno y externo de parásitos", 15, 30.0, true, "syringe")
        )
        db.servicioDao().insertarTodos(servicios)

        // 2. Insertar Usuarios
        val usuarios = listOf(
            Usuario(1, "Carlos Mendoza", "carlos@gmail.com", "123456", Rol.CLIENTE.name),
            Usuario(2, "Dra. Valeria Ríos", "valeria@huellitas.com", "admin123", Rol.ADMIN.name)
        )
        db.usuarioDao().insertarTodos(usuarios)

        // 3. Insertar Mascotas
        // 3. Insertar Mascotas
        val m1 = db.mascotaDao().insertar(
            Mascota(id = 1, duenoId = 1, nombre = "Bobby", especie = Especie.PERRO.name, raza = "Golden Retriever", anioNacimiento = 2020, peso = 28.5, sexo = Sexo.MACHO.name)
        ).toInt()

        val m2 = db.mascotaDao().insertar(
            Mascota(id = 2, duenoId = 1, nombre = "Michi", especie = Especie.GATO.name, raza = "Siamés", anioNacimiento = 2022, peso = 4.2, sexo = Sexo.HEMBRA.name)
        ).toInt()

        // 4. Calcular Fechas Dinámicas (Ayer, Hoy, Mañana, etc.)
        val hoy = LocalDate.now()
        val fAyer2 = hoy.minusDays(2).toString()
        val fAyer1 = hoy.minusDays(1).toString()
        val fHoy = hoy.toString()
        val fManana1 = hoy.plusDays(1).toString()
        val fManana2 = hoy.plusDays(2).toString()

        // 5. Insertar Citas (Pasadas con atención, hoy y futuras)
        val citas = listOf(
            // Pasadas ATENDIDAS (Mascota 1 - Bobby)
            Cita(1, m1, 1, fAyer2, "09:00", "Control de peso", EstadoCita.ATENDIDA.name, 50.0, "Mascota saludable", "Control nutricional y vitaminas", 28.5),
            Cita(2, m1, 2, fAyer1, "10:00", "Vacuna anual", EstadoCita.ATENDIDA.name, 65.0, "Vacunación exitosa", "Ninguno, descanso 24h", 28.7),

            // Pasadas ATENDIDAS (Mascota 2 - Michi)
            Cita(3, m2, 3, fAyer2, "11:00", "Baño medicado", EstadoCita.ATENDIDA.name, 45.0, "Piel limpia sin dermatitis", "Champú hipoalergénico", 4.1),
            Cita(4, m2, 4, fAyer1, "16:00", "Desparasitación semestral", EstadoCita.ATENDIDA.name, 30.0, "Parásitos leves", "1 pastilla Drontal", 4.2),

            // Citas para HOY
            Cita(5, m1, 1, fHoy, "09:00", "Revisión de oídos", EstadoCita.CONFIRMADA.name, 50.0),
            Cita(6, m2, 1, fHoy, "10:30", "Estrés y falta de apetito", EstadoCita.PENDIENTE.name, 50.0),
            Cita(7, m1, 3, fHoy, "15:00", "Corte veraniego", EstadoCita.CONFIRMADA.name, 45.0),

            // Citas FUTURAS
            Cita(8, m1, 2, fManana1, "10:00", "Refuerzo sextuple", EstadoCita.PENDIENTE.name, 65.0),
            Cita(9, m2, 1, fManana1, "11:30", "Chequeo mensual", EstadoCita.PENDIENTE.name, 50.0),
            Cita(10, m1, 4, fManana2, "16:00", "Desparasitación programada", EstadoCita.PENDIENTE.name, 30.0)
        )

        citas.forEach { db.citaDao().insertar(it) }
    }
}