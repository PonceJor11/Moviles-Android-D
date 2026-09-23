package com.ejemplo.tecsupfit.data

import com.ejemplo.tecsupfit.model.ClaseGimnasio
import com.ejemplo.tecsupfit.model.Reserva

object DatosFuente {
    val listaClases = listOf(
        ClaseGimnasio(1, "Yoga funcional", "7:00 am", "Sala 2", "50 min", "Yoga y fuerza central.", 5, 15, "Hoy"),
        ClaseGimnasio(2, "Cross Training", "6:00 pm", "Sala 1", "45 min", "Entrenamiento de alta intensidad.", 8, 12, "Hoy"),
        ClaseGimnasio(3, "Spinning", "7:30 pm", "Sala 3", "45 min", "Cardio sobre bicicleta estática.", 10, 20, "Hoy"),
        ClaseGimnasio(4, "Pilates", "9:00 am", "Sala 2", "60 min", "Control de respiración y fuerza.", 6, 12, "Esta semana")
    )

    val listaReservasIniciales = listOf(
        Reserva(1, "Cross Training", "Hoy, 6:00 pm", "Confirmada"),
        Reserva(2, "Yoga funcional", "Ayer, 7:00 am", "Completada")
    )
}