package com.ponce.campusgps.util

import android.location.Location
import com.ponce.campusgps.model.Lugar
import com.ponce.campusgps.model.Posicion

/** Distancia en metros entre la posición del usuario y un lugar del campus. */
fun distanciaMetros(desde: Posicion, hasta: Lugar): Float {
    val resultado = FloatArray(1)
    Location.distanceBetween(
        desde.latitud, desde.longitud,
        hasta.latitud, hasta.longitud,
        resultado
    )
    return resultado[0]
}
