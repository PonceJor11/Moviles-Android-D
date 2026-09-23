package com.ejemplo.tecsupfit.data

import com.ejemplo.tecsupfit.model.ClaseGimnasio
import com.ejemplo.tecsupfit.model.Reserva

object DatosFuente {
    val listaClases = listOf(
        ClaseGimnasio(1, "Yoga funcional", "7:00 am", "Sala 2", "45 min", "Clase enfocada en flexibilidad y fuerza central.", 5, 10, "Hoy"),
        ClaseGimnasio(2, "Cross Training", "6:00 pm", "Sala 1", "45 min", "Entrenamiento funcional de alta intensidad. Cupos limitados.", 8, 12, "Hoy"),
        ClaseGimnasio(3, "Spinning", "7:30 pm", "Sala 3", "50 min", "Ciclismo de alta intensidad con ritmo cardiovascular.", 3, 15, "Hoy"),
        ClaseGimnasio(4, "Pilates", "8:00 am", "Sala 2", "45 min", "Control corporal y tonificacion muscular.", 6, 10, "Esta semana")
    )

    val listaReservasIniciales = listOf(
        Reserva(1, "Cross Training", "Hoy, 6:00 pm", "Confirmada"),
        Reserva(2, "Yoga funcional", "Ayer, 7:00 am", "Completada")
    )
}
