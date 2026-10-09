package com.saludplus.citas.data.repository

import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Usuario

object Repositorio {

    // Colecciones en memoria
    val usuarios = mutableListOf<Usuario>()
    val especialidades = mutableListOf<Especialidad>(
        Especialidad("1", "Cardiología", "Salud del corazón y sistema circulatorio", "ic_cardio"),
        Especialidad("2", "Pediatría", "Atención médica integral para niños", "ic_pediatria"),
        Especialidad("3", "Dermatología", "Cuidado de la piel, cabello y uñas", "ic_dermato"),
        Especialidad("4", "Neurología", "Diagnóstico y tratamiento del sistema nervioso", "ic_neuro"),
        Especialidad("5", "Odontología", "Salud oral y tratamiento dental", "ic_odonto")
    )
    val medicos = mutableListOf<Medico>(
        Medico("m1", "1", "Dr. Carlos Mendoza", "Cardiólogo con 12 años de experiencia.", 4.8, "08:00 - 16:00"),
        Medico("m2", "1", "Dra. Ana Torres", "Especialista en cardiología intervencionista.", 4.9, "09:00 - 17:00"),
        Medico("m3", "2", "Dr. Luis Gómez", "Pediatra especializado en desarrollo infantil.", 4.7, "08:00 - 14:00"),
        Medico("m4", "3", "Dra. Elena Ramos", "Dermatóloga clínica y estética.", 4.9, "10:00 - 18:00"),
        Medico("m5", "4", "Dr. Roberto Silva", "Neurólogo especialista en migrañas.", 4.6, "08:00 - 15:00")
    )
    val citas = mutableListOf<Cita>()

    var usuarioActual: Usuario? = null
        private set

    // Horarios base de atención
    val horariosBase = listOf("08:00", "09:00", "10:00", "11:00", "14:00", "15:00", "16:00")

    // Operaciones de Usuario
    fun registrarUsuario(usuario: Usuario): Boolean {
        if (usuarios.any { it.correo.equals(usuario.correo, ignoreCase = true) }) {
            return false
        }
        usuarios.add(usuario)
        usuarioActual = usuario
        return true
    }

    fun iniciarSesion(correo: String, clave: String): Usuario? {
        val encontrado = usuarios.find {
            it.correo.equals(correo, ignoreCase = true) && it.clave == clave
        }
        if (encontrado != null) {
            usuarioActual = encontrado
        }
        return encontrado
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    // Operaciones de Especialidad y Médico
    fun especialidadesDestacadas(): List<Especialidad> {
        return especialidades.take(4)
    }

    fun buscarEspecialidades(query: String): List<Especialidad> {
        if (query.isBlank()) return especialidades
        return especialidades.filter {
            it.nombre.contains(query, ignoreCase = true) || it.descripcion.contains(query, ignoreCase = true)
        }
    }

    fun obtenerEspecialidad(id: String): Especialidad? {
        return especialidades.find { it.id == id }
    }

    fun obtenerMedico(id: String): Medico? {
        return medicos.find { it.id == id }
    }

    fun medicosPorEspecialidad(especialidadId: String): List<Medico> {
        return medicos.filter { it.especialidadId == especialidadId }
            .sortedByDescending { it.calificacion }
    }

    // Operaciones de Citas
    fun horariosDisponibles(medicoId: String, fecha: String): List<String> {
        val ocupados = citas.filter { it.medicoId == medicoId && it.fecha == fecha }
            .map { it.hora }
        return horariosBase.filter { it !in ocupados }
    }

    fun agendarCita(cita: Cita): Boolean {
        val yaExiste = citas.any {
            it.medicoId == cita.medicoId && it.fecha == cita.fecha && it.hora == cita.hora
        }
        if (yaExiste) return false
        citas.add(cita)
        return true
    }

    fun citasDelUsuario(usuarioId: String): List<Cita> {
        return citas.filter { it.usuarioId == usuarioId }
            .sortedWith(compareBy({ it.fecha }, { it.hora }))
    }

    fun obtenerCita(id: String): Cita? {
        return citas.find { it.id == id }
    }

    fun cancelarCita(citaId: String): Boolean {
        return citas.removeIf { it.id == citaId }
    }
}