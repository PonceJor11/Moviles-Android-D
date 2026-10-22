package com.tecsup.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.dueno.LoginScreen
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

object Destinos {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val INICIO_DUENO = "inicio_dueno"
    const val PANEL_VET = "panel_vet"
}

@Composable
fun NavVet(sesionViewModel: SesionViewModel) {
    val navController = rememberNavController()
    val perfilActivo by sesionViewModel.perfilActivo.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Destinos.SPLASH
    ) {
        // Pantalla Splash (autologin si ya hay perfil guardado)
        composable(Destinos.SPLASH) {
            LaunchedEffect(perfilActivo) {
                when (perfilActivo) {
                    Rol.CLIENTE.name -> navController.navigate(Destinos.INICIO_DUENO) {
                        popUpTo(Destinos.SPLASH) { inclusive = true }
                    }
                    Rol.ADMIN.name -> navController.navigate(Destinos.PANEL_VET) {
                        popUpTo(Destinos.SPLASH) { inclusive = true }
                    }
                    else -> navController.navigate(Destinos.LOGIN) {
                        popUpTo(Destinos.SPLASH) { inclusive = true }
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Pantalla de Login
        composable(Destinos.LOGIN) {
            LoginScreen(
                vm = sesionViewModel,
                onLoginExito = { rol ->
                    val destino = if (rol == Rol.CLIENTE.name) Destinos.INICIO_DUENO else Destinos.PANEL_VET
                    navController.navigate(destino) {
                        popUpTo(Destinos.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Destinos temporales (se irán desarrollando en los siguientes pasos)
        composable(Destinos.INICIO_DUENO) {
            // Pantalla Inicio Dueño
        }

        composable(Destinos.PANEL_VET) {
            // Pantalla Panel Veterinario
        }
    }
}