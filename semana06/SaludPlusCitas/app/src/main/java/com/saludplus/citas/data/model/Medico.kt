package com.saludplus.citas.data.model

data class Medico(
    val id: String,
    val especialidadId: String,
    val nombre: String,
    val biografia: String,
    val calificacion: Double,
    val horarioAtencion: String
)