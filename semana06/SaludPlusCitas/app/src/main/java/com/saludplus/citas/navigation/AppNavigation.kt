package com.saludplus.citas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.saludplus.citas.ui.screens.home.HomeScreen
import com.saludplus.citas.ui.screens.notificaciones.NotificacionesScreen
import com.saludplus.citas.ui.screens.perfil.PerfilScreen
import com.saludplus.citas.ui.screens.resultados.ResultadosScreen

@Composable
    NavHost(
        navController = navController,
        startDestination = Rutas.SPLASH
    ) {
        composable(Rutas.SPLASH) {
            SplashScreen(
                onNavigateToLogin = { navController.navigate(Rutas.LOGIN) },
                onNavigateToRegistro = { navController.navigate(Rutas.REGISTRO) }
            )
        }

        // 2. Registro
        composable(Rutas.REGISTRO) {
            RegistroScreen(
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.navigate(Rutas.LOGIN) }
            )
        }

        composable(Rutas.LOGIN) {
            LoginScreen(
                onLoginExitoso = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToRegistro = { navController.navigate(Rutas.REGISTRO) }
            )
        }

        composable(Rutas.HOME) {
            HomeScreen(
                onNavigateToEspecialidades = { navController.navigate(Rutas.ESPECIALIDADES) },
            )
        }

        // 4. Especialidades
        composable(Rutas.ESPECIALIDADES) {
            EspecialidadesScreen(
            )
        }

            MedicosScreen(
            )
        }

            FechaHoraScreen(
            )
        }

        // 7. Confirmar Cita
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
            val hora = backStackEntry.arguments?.getString("hora") ?: ""

            ConfirmarCitaScreen(
                fecha = fecha,
                hora = hora,
                        popUpTo(Rutas.HOME) { inclusive = false }
                    }
            )
        }

            val citaId = backStackEntry.arguments?.getString("citaId") ?: ""
            CitaExitosaScreen(
                citaId = citaId,
                onIrAMisCitas = {
                    navController.navigate(Rutas.MIS_CITAS) {
                        popUpTo(Rutas.HOME) { inclusive = false }
                    }
                }
            )
        }

        composable(Rutas.MIS_CITAS) {
            MisCitasScreen(
            )
        }

            val citaId = backStackEntry.arguments?.getString("citaId") ?: ""
            DetalleCitaScreen(
                citaId = citaId,
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.RESULTADOS) {
        }

                    }
        }
    }
}