package com.ponce.murocampus

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ponce.murocampus.data.PostRepositoryRemote
import com.ponce.murocampus.data.remote.RetrofitClient
import com.ponce.murocampus.ui.PantallaCrear
import com.ponce.murocampus.ui.PantallaDetalle
import com.ponce.murocampus.ui.PantallaLista
import com.ponce.murocampus.ui.theme.MuroCampusTheme
import com.ponce.murocampus.viewmodel.PostsViewModel
import com.ponce.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Barra de estado transparente con iconos claros (sobre la barra cognac)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            MuroCampusTheme {
                val vm: PostsViewModel = viewModel(
                    factory = PostsViewModelFactory(PostRepositoryRemote(RetrofitClient.api))
                )
                MuroCampusApp(vm)
            }
        }
    }
}

@Composable
fun MuroCampusApp(vm: PostsViewModel) {
    // Navegacion simple: null = lista; id = detalle; creando = formulario
    var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
    var creando by rememberSaveable { mutableStateOf(false) }

    val id = idSeleccionado
    when {
        creando -> {
            BackHandler { creando = false }
            PantallaCrear(vm = vm, onVolver = { creando = false })
        }
        id != null -> {
            BackHandler { idSeleccionado = null }
            PantallaDetalle(vm = vm, id = id, onVolver = { idSeleccionado = null })
        }
        else -> PantallaLista(
            vm = vm,
            onAbrir = {
                vm.abrir(it)
                idSeleccionado = it
            },
            onCrear = {
                vm.limpiarPublicacion()
                creando = true
            }
        )
    }
}
