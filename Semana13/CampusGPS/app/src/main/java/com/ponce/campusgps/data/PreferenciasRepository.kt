package com.ponce.campusgps.data

import kotlinx.coroutines.flow.Flow

/** Preferencias del usuario que deben sobrevivir al cierre de la app. */
interface PreferenciasRepository {
    /** Id del último lugar seleccionado, o null si todavía no hay ninguno guardado. */
    val ultimoLugarId: Flow<Int?>

    suspend fun guardarUltimoLugar(id: Int)
}
