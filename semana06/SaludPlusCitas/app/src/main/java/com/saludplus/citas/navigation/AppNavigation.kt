package com.saludplus.citas.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saludplus.citas.ui.screens.agendamiento.CitaExitosaScreen
import com.saludplus.citas.ui.screens.agendamiento.ConfirmarCitaScreen
import com.saludplus.citas.ui.screens.agendamiento.EspecialidadesScreen
import com.saludplus.citas.ui.screens.agendamiento.FechaHoraScreen
import com.saludplus.citas.ui.screens.agendamiento.MedicosScreen
import com.saludplus.citas.ui.screens.auth.LoginScreen
import com.saludplus.citas.ui.screens.auth.RegistroScreen
import com.saludplus.citas.ui.screens.auth.SplashScreen
import com.saludplus.citas.ui.screens.citas.DetalleCitaScreen
import com.saludplus.citas.ui.screens.citas.MisCitasScreen
import com.saludplus.citas.ui.screens.home.HomeScreen
import com.saludplus.citas.ui.screens.notificaciones.NotificacionesScreen
import com.saludplus.citas.ui.screens.perfil.PerfilScreen
import com.saludplus.citas.ui.screens.resultados.ResultadosScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.SPLASH
    ) {
        // 1. Splash Screen
        composable(Rutas.SPLASH) {
            SplashScreen(
                onNavigateToLogin = { navController.navigate(Rutas.LOGIN) },
                onNavigateToRegistro = { navController.navigate(Rutas.REGISTRO) }
            )
        }

        // 2. Registro
        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onNavigateToHome = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.navigate(Rutas.LOGIN) }
            )
        }

        // Login
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

        // 3. Home
        composable(Rutas.HOME) {
            HomeScreen(
                onNavigateToEspecialidades = { navController.navigate(Rutas.ESPECIALIDADES) },
                onNavigateToMedicos = { especialidadId ->
                    navController.navigate("medicos/$especialidadId")
                },
                onNavigateToNotificaciones = { navController.navigate(Rutas.NOTIFICACIONES) },
                onNavigateToMisCitas = { navController.navigate(Rutas.MIS_CITAS) },
                onNavigateToPerfil = { navController.navigate(Rutas.PERFIL) },
                onNavigateToResultados = { navController.navigate(Rutas.RESULTADOS) }
            )
        }

        // 4. Especialidades
        composable(Rutas.ESPECIALIDADES) {
            EspecialidadesScreen(
                onSeleccionarEspecialidad = { especialidadId ->
                    navController.navigate("medicos/$especialidadId")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 5. Selección de Médico
        composable(
            route = "medicos/{especialidadId}",
            arguments = listOf(navArgument("especialidadId") { type = NavType.StringType })
        ) { backStackEntry ->
            val especialidadId = backStackEntry.arguments?.getString("especialidadId") ?: ""
            MedicosScreen(
                especialidadId = especialidadId,
                onSeleccionarMedico = { medicoId ->
                    navController.navigate("fecha_hora/$especialidadId/$medicoId")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 6. Selección de Fecha y Hora
        composable(
            route = "fecha_hora/{especialidadId}/{medicoId}",
            arguments = listOf(
                navArgument("especialidadId") { type = NavType.StringType },
                navArgument("medicoId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val especialidadId = backStackEntry.arguments?.getString("especialidadId") ?: ""
            val medicoId = backStackEntry.arguments?.getString("medicoId") ?: ""
            FechaHoraScreen(
                especialidadId = especialidadId,
                medicoId = medicoId,
                onNavigateToConfirmar = { espId, medId, fecha, hora ->
                    navController.navigate("confirmar_cita/$espId/$medId/$fecha/$hora")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 7. Confirmar Cita
        composable(
            route = "confirmar_cita/{especialidadId}/{medicoId}/{fecha}/{hora}",
            arguments = listOf(
                navArgument("especialidadId") { type = NavType.StringType },
                navArgument("medicoId") { type = NavType.StringType },
                navArgument("fecha") { type = NavType.StringType },
                navArgument("hora") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val especialidadId = backStackEntry.arguments?.getString("especialidadId") ?: ""
            val medicoId = backStackEntry.arguments?.getString("medicoId") ?: ""
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
            val hora = backStackEntry.arguments?.getString("hora") ?: ""

            ConfirmarCitaScreen(
                especialidadId = especialidadId,
                medicoId = medicoId,
                fecha = fecha,
                hora = hora,
                onNavigateToExitosa = { citaId ->
                    navController.navigate("cita_exitosa/$citaId") {
                        popUpTo(Rutas.HOME) { inclusive = false }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Cita Exitosa
        composable(
            route = "cita_exitosa/{citaId}",
            arguments = listOf(navArgument("citaId") { type = NavType.StringType })
        ) { backStackEntry ->
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

        // Mis Citas
        composable(Rutas.MIS_CITAS) {
            MisCitasScreen(
                onVerDetalle = { citaId ->
                    navController.navigate("detalle_cita/$citaId")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Detalle de Cita
        composable(
            route = "detalle_cita/{citaId}",
            arguments = listOf(navArgument("citaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getString("citaId") ?: ""
            DetalleCitaScreen(
                citaId = citaId,
                onVolver = { navController.popBackStack() }
            )
        }

        // Notificaciones
        composable(Rutas.NOTIFICACIONES) {
            NotificacionesScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Resultados / Historial
        composable(Rutas.RESULTADOS) {
            ResultadosScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Perfil
        composable(Rutas.PERFIL) {
            PerfilScreen(
                onCerrarSesion = {
                    navController.navigate(Rutas.SPLASH) {
                        popUpTo(Rutas.HOME) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}