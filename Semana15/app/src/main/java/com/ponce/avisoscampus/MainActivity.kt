package com.ponce.avisoscampus

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.ponce.avisoscampus.data.AvisosRepositoryMemoria
import com.ponce.avisoscampus.ui.PantallaAvisos
import com.ponce.avisoscampus.ui.theme.AvisosCampusTheme
import com.ponce.avisoscampus.viewmodel.AvisosViewModel
import com.ponce.avisoscampus.viewmodel.AvisosViewModelFactory

class MainActivity : ComponentActivity() {

    private val vm: AvisosViewModel by viewModels {
        AvisosViewModelFactory(AvisosRepositoryMemoria())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Barra de estado transparente con íconos claros sobre la barra marrón
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        setContent {
            AvisosCampusTheme {
                PantallaAvisos(vm)
            }
        }
    }
}
