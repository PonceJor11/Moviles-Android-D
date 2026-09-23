package com.ejemplo.clinicasalud.data

import com.ejemplo.clinicasalud.model.Cita
import com.ejemplo.clinicasalud.model.Medico

object DatosFuente {
    val listaMedicos = listOf(
        Medico(1, "Dra. Ana Torres", "Cardióloga", 4.9, 128, "12 años exp.", "Especialista en arritmias e hipertensión, formación en la Clínica Mayo."),
        Medico(2, "Dr. Luis Vega", "Pediatra", 4.7, 95, "8 años exp.", "Atención integral infantil y seguimiento del desarrollo."),
        Medico(3, "Dra. Rosa Díaz", "Dermatóloga", 4.8, 110, "10 años exp.", "Tratamientos avanzados para el cuidado de la piel.")
    )

    val listaCitasIniciales = mutableListOf(
        Cita(1, "Dra. Ana Torres", "Viernes 27", "10:30 am", "Confirmada"),
        Cita(2, "Dr. Luis Vega", "Miércoles 15", "3:00 pm", "Completada")
    )
}