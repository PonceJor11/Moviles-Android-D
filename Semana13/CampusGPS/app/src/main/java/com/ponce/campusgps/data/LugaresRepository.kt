package com.ponce.campusgps.data

import com.ponce.campusgps.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}
