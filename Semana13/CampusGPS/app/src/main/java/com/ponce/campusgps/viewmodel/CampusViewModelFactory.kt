package com.ponce.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ponce.campusgps.data.LugaresRepository
import com.ponce.campusgps.data.PreferenciasRepository
import com.ponce.campusgps.data.UbicacionRepository

class CampusViewModelFactory(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository,
    private val preferenciasRepo: PreferenciasRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(MapaViewModel::class.java)) {
            "ViewModel desconocido: ${modelClass.name}"
        }
        return MapaViewModel(ubicacionRepo, lugaresRepo, preferenciasRepo) as T
    }
}
