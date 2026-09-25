package com.ejemplo.tecsupfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ejemplo.tecsupfit.data.DatosFuente
import com.ejemplo.tecsupfit.model.Reserva
import com.ejemplo.tecsupfit.screens.*
import com.ejemplo.tecsupfit.ui.theme.TecsupFitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TecsupFitTheme {
                AppTecsupFit()
            }
        }
    }
}

@Composable
fun AppTecsupFit() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Estado local para las reservas manejado con remember (Sin ViewModel/MVVM)
    val reservasState = remember { mutableStateListOf(*DatosFuente.listaReservasIniciales.toTypedArray()) }

    // Las 4 pestañas requeridas para el bottomBar
    val pestañasBottomBar = listOf("inicio", "reservas", "rutinas", "perfil")
    val mostrarBottomBar = currentRoute in pestañasBottomBar

    Scaffold(
        bottomBar = {
            if (mostrarBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == "inicio",
                        onClick = {
                            navController.navigate("inicio") {
                                popUpTo("inicio") { inclusive = true }
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                        label = { Text("Inicio") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "reservas",
                        onClick = { navController.navigate("reservas") },
                        icon = { Icon(Icons.Default.DateRange, contentDescription = "Reservas") },
                        label = { Text("Reservas") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "rutinas",
                        onClick = { navController.navigate("rutinas") },
                        icon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Rutinas") },
                        label = { Text("Rutinas") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "perfil",
                        onClick = { navController.navigate("perfil") },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                        label = { Text("Perfil") }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("inicio") {
                InicioScreen(
                    onClaseSelect = { claseId -> navController.navigate("detalle/$claseId") }
                )
            }

            composable(
                route = "detalle/{claseId}",
                arguments = listOf(navArgument("claseId") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("claseId") ?: 1
                val clase = DatosFuente.listaClases.find { it.id == id } ?: DatosFuente.listaClases.first()
                DetalleClaseScreen(
                    clase = clase,
                    onBack = { navController.popBackStack() },
                    onReservar = {
                        reservasState.add(
                            Reserva(
                                id = reservasState.size + 1,
                                nombreClase = clase.nombre,
                                horario = "Hoy, ${clase.horario}",
                                estado = "Confirmada"
                            )
                        )
                        navController.navigate("confirmacion/${clase.nombre}/${clase.horario}")
                    }
                )
            }

            composable("confirmacion/{nombre}/{horario}") { backStackEntry ->
                val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
                val horario = backStackEntry.arguments?.getString("horario") ?: ""
                ConfirmacionScreen(
                    nombreClase = nombre,
                    horario = horario,
                    onVerReservas = {
                        navController.navigate("reservas") {
                            popUpTo("inicio") { inclusive = false }
                        }
                    }
                )
            }

            composable("reservas") {
                ReservasScreen()
            }

            composable("rutinas") {
                RutinasScreen()
            }

            composable("perfil") {
                PerfilScreen()
            }
        }
    }
}