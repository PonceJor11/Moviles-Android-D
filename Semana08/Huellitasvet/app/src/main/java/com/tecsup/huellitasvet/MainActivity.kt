package com.tecsup.huellitasvet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.tecsup.huellitasvet.data.local.VetDatabase
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.data.repository.VetRepositoryRoom
import com.tecsup.huellitasvet.ui.navigation.NavVet
import com.tecsup.huellitasvet.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Base de datos, Repositorio y DataStore
        val db = VetDatabase.obtener(applicationContext)

        val repository = VetRepositoryRoom(db)
        val prefsStore = PreferenciasStore(applicationContext)

        // 2. Instancias de ViewModels pasando duenoId = 1 para el flujo del cliente
        val idDuenoPrueba = 1

        val sesionViewModel = SesionViewModel(repository, prefsStore)
        val inicioViewModel = InicioViewModel(repository, idDuenoPrueba)
        val mascotaViewModel = MascotaViewModel(repository, idDuenoPrueba)
        val pedirCitaViewModel = PedirCitaViewModel(repository, idDuenoPrueba)
        val misCitasViewModel = MisCitasViewModel(repository, idDuenoPrueba)
        val panelViewModel = PanelViewModel(repository)
        val agendaViewModel = AgendaViewModel(repository)
        val pacientesViewModel = PacientesViewModel(repository)
        val serviciosViewModel = ServiciosViewModel(repository)

        setContent {
            val modoOscuro by sesionViewModel.modoOscuro.collectAsState(initial = false)

            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavVet(
                        sesionViewModel = sesionViewModel,
                        inicioViewModel = inicioViewModel,
                        mascotaViewModel = mascotaViewModel,
                        pedirCitaViewModel = pedirCitaViewModel,
                        misCitasViewModel = misCitasViewModel,
                        panelViewModel = panelViewModel,
                        agendaViewModel = agendaViewModel,
                        atenderCitaViewModelFactory = { citaId ->
                            AtenderCitaViewModel(repository, citaId)
                        },
                        pacientesViewModel = pacientesViewModel,
                        serviciosViewModel = serviciosViewModel,
                        prefsStore = prefsStore
                    )
                }
            }
        }
    }
}