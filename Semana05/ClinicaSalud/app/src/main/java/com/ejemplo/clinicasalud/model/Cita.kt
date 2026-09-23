package com.ejemplo.clinicasalud.model

data class Cita(
    val id: Int,
    val medicoNombre: String,
    val fecha: String,
    val hora: String,
    val estado: String // "Confirmada" o "Completada"
)