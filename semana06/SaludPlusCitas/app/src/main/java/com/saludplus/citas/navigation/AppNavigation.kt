package com.saludplus.citas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.screens.agendamiento.*
import com.saludplus.citas.ui.screens.auth.*
import com.saludplus.citas.ui.screens.citas.*
import com.saludplus.citas.ui.screens.home.HomeScreen
import com.saludplus.citas.ui.screens.notificaciones.NotificacionesScreen
import com.saludplus.citas.ui.screens.perfil.PerfilScreen
import com.saludplus.citas.ui.screens.resultados.ResultadosScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Rutas.SPLASH
    ) {
        // 1. Splash
        composable(Rutas.SPLASH) {
            SplashScreen(
                onNavigateToLogin = { navController.navigate(Rutas.LOGIN) },
                onNavigateToRegistro = { navController.navigate(Rutas.REGISTRO) }
            )
        }

        // 2. Registro
        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onRegistroExitoso = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToTerminos = { navController.navigate(Rutas.TERMINOS) },
                onNavigateToLogin = { navController.navigate(Rutas.LOGIN) }
            )
        }

        // 8. Iniciar Sesión
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

        // 3. Inicio
        composable(Rutas.HOME) {
            HomeScreen(
                onNavigateToEspecialidades = { navController.navigate(Rutas.ESPECIALIDADES) },
                onNavigateToMedicos = { espId -> navController.navigate("${Rutas.MEDICOS}/$espId") },
                onNavigateToNotificaciones = { navController.navigate(Rutas.NOTIFICACIONES) }
            )
        }

        // 4. Especialidades
        composable(Rutas.ESPECIALIDADES) {
            EspecialidadesScreen(
                onSeleccionarEspecialidad = { espId ->
                    navController.navigate("${Rutas.MEDICOS}/$espId")
                }
            )
        }

        // 5. Médicos
        composable("${Rutas.MEDICOS}/{especialidadId}") { backStackEntry ->
            val espId = backStackEntry.arguments?.getString("especialidadId") ?: ""
            MedicosScreen(
                especialidadId = espId,
                onSeleccionarMedico = { medId ->
                    navController.navigate("${Rutas.FECHA_HORA}/$medId")
                }
            )
        }

        // 6. Fecha y Hora
        composable("${Rutas.FECHA_HORA}/{medicoId}") { backStackEntry ->
            val medId = backStackEntry.arguments?.getString("medicoId") ?: ""
            FechaHoraScreen(
                medicoId = medId,
                onContinuar = { fecha, hora ->
                    navController.navigate("${Rutas.CONFIRMAR_CITA}/$medId/$fecha/$hora")
                }
            )
        }

        // 7. Confirmar Cita
        composable("${Rutas.CONFIRMAR_CITA}/{medicoId}/{fecha}/{hora}") { backStackEntry ->
            val medId = backStackEntry.arguments?.getString("medicoId") ?: ""
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
            val hora = backStackEntry.arguments?.getString("hora") ?: ""
            val medico = Repositorio.obtenerMedico(medId)

            ConfirmarCitaScreen(
                especialidadId = medico?.especialidadId ?: "",
                medicoId = medId,
                fecha = fecha,
                hora = hora,
                onConfirmado = { citaId ->
                    navController.navigate("${Rutas.CITA_EXITOSA}/$citaId") {
                        popUpTo(Rutas.HOME) { inclusive = false }
                    }
                }
            )
        }

        // 9. Cita Agendada
        composable("${Rutas.CITA_EXITOSA}/{citaId}") { backStackEntry ->
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

        // 10. Mis Citas
        composable(Rutas.MIS_CITAS) {
            MisCitasScreen(
                onVerDetalle = { citaId -> navController.navigate("${Rutas.DETALLE_CITA}/$citaId") }
            )
        }

        // 11. Perfil
        composable(Rutas.PERFIL) {
            PerfilScreen(
                onCerrarSesion = {
                    navController.navigate(Rutas.SPLASH) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // 12. Detalle de Cita (Reto)
        composable("${Rutas.DETALLE_CITA}/{citaId}") { backStackEntry ->
            val citaId = backStackEntry.arguments?.getString("citaId") ?: ""
            DetalleCitaScreen(
                citaId = citaId,
                onVolver = { navController.popBackStack() }
            )
        }

        // 13. Resultados (Reto)
        composable(Rutas.RESULTADOS) {
            ResultadosScreen()
        }

        // 14. Notificaciones (Reto)
        composable(Rutas.NOTIFICACIONES) {
            NotificacionesScreen()
        }

        // 15. Términos (Reto)
        composable(Rutas.TERMINOS) {
            TerminosScreen(onVolver = { navController.popBackStack() })
        }
    }
}