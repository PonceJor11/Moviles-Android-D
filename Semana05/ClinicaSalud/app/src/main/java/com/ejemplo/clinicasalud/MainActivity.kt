package com.ejemplo.clinicasalud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ejemplo.clinicasalud.data.DatosFuente
import com.ejemplo.clinicasalud.model.Cita
import com.ejemplo.clinicasalud.screens.*
import com.ejemplo.clinicasalud.ui.theme.ClinicaSaludTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClinicaSaludTheme {
                AppSalud()
            }
        }
    }
}

@Composable
fun AppSalud() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Manejo de estado local para la lista de citas (Sin ViewModel)
    val citasState = remember { mutableStateListOf(*DatosFuente.listaCitasIniciales.toTypedArray()) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(modifier = Modifier.padding(16.dp)) {
                    Text(text = "JP  Juan Pérez", style = MaterialTheme.typography.titleLarge)
                }
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("inicio") {
                            popUpTo("inicio") { inclusive = true }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Mis citas") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("citas")
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Historial médico") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } }
                )
            }
        }
    ) {
        NavHost(navController = navController, startDestination = "inicio") {
            // 1. Pantalla de Inicio
            composable("inicio") {
                InicioScreen(
                    onOpenDrawer = { scope.launch { drawerState.open() } },
                    onMedicoSelect = { medicoId -> navController.navigate("perfil/$medicoId") }
                )
            }

            // 2. Pantalla Perfil del Médico
            composable(
                route = "perfil/{medicoId}",
                arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("medicoId") ?: 1
                val medico = DatosFuente.listaMedicos.find { it.id == id } ?: DatosFuente.listaMedicos.first()
                PerfilMedicoScreen(
                    medico = medico,
                    onBack = { navController.popBackStack() },
                    onAgendar = { navController.navigate("agendar/${medico.id}") }
                )
            }

            // 3. Pantalla Agendar Cita
            composable(
                route = "agendar/{medicoId}",
                arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("medicoId") ?: 1
                val medico = DatosFuente.listaMedicos.find { it.id == id } ?: DatosFuente.listaMedicos.first()
                AgendarCitaScreen(
                    medico = medico,
                    onBack = { navController.popBackStack() },
                    onConfirmar = { fecha, hora ->
                        // Agregar la nueva cita al estado compartido
                        citasState.add(
                            Cita(
                                id = citasState.size + 1,
                                medicoNombre = medico.nombre,
                                fecha = fecha,
                                hora = hora,
                                estado = "Confirmada"
                            )
                        )
                        navController.navigate("confirmacion/${medico.nombre}/$fecha/$hora")
                    }
                )
            }

            // 4. Pantalla de Confirmación
            composable("confirmacion/{nombre}/{fecha}/{hora}") { backStackEntry ->
                val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
                val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
                val hora = backStackEntry.arguments?.getString("hora") ?: ""
                ConfirmacionScreen(
                    nombreMedico = nombre,
                    fecha = fecha,
                    hora = hora,
                    onVerCitas = {
                        navController.navigate("citas") {
                            popUpTo("inicio") { inclusive = false }
                        }
                    }
                )
            }

            // 5. Pantalla Mis Citas
            composable("citas") {
                MisCitasScreen(
                    citas = citasState,
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }
        }
    }
}