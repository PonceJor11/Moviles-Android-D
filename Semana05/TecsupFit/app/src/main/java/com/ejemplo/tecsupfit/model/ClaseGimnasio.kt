package com.ejemplo.tecsupfit.model

data class ClaseGimnasio(
    val id: Int,
    val nombre: String,
    val horario: String,
    val sala: String,
    val duracion: String,
    val descripcion: String,
    val cuposDisponibles: Int,
    val cuposTotales: Int,
    val filtro: String
)
