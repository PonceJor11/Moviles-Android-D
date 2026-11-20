package com.ponce.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ponce.campusgps.data.LugaresRepositoryLocal
import com.ponce.campusgps.data.UbicacionRepositoryGoogle
import com.ponce.campusgps.ui.PantallaMapa
import com.ponce.campusgps.ui.theme.CampusGPSTheme
import com.ponce.campusgps.viewmodel.CampusViewModelFactory
import com.ponce.campusgps.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Las implementaciones concretas solo se conocen aquí; el resto trabaja con interfaces.
        val fabrica = CampusViewModelFactory(
            ubicacionRepo = UbicacionRepositoryGoogle(applicationContext),
            lugaresRepo = LugaresRepositoryLocal()
        )

        setContent {
            CampusGPSTheme {
                val vm: MapaViewModel = viewModel(factory = fabrica)
                PantallaMapa(vm)
            }
        }
    }
}
