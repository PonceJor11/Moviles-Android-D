package com.tecsup.centrotareas

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.centrotareas.data.TareasRepositoryLocal
import com.tecsup.centrotareas.ui.PantallaTareas
import com.tecsup.centrotareas.ui.theme.CentroTareasTheme
import com.tecsup.centrotareas.viewmodel.TareasViewModel
import com.tecsup.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Barra de estado transparente con iconos claros: la barra superior es azul.
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        setContent {
            CentroTareasTheme {
                val vm: TareasViewModel = viewModel(
                    factory = TareasViewModelFactory(TareasRepositoryLocal())
                )
                PantallaTareas(vm)
            }
        }
    }
}
