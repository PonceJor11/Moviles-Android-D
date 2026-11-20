package com.ponce.campusgps.data

import com.ponce.campusgps.model.Posicion

interface UbicacionRepository {
    /** Devuelve la posición actual o un fallo (SecurityException, UbicacionNoDisponibleException, ...). */
    suspend fun obtenerPosicion(): Result<Posicion>

    /** Indica si el dispositivo cuenta con Google Play Services utilizable. */
    fun playServicesDisponible(): Boolean
}

/** Se lanza cuando el sistema no puede determinar la ubicación (por ejemplo, GPS apagado). */
class UbicacionNoDisponibleException(
    mensaje: String = "No se pudo determinar la ubicación"
) : Exception(mensaje)
