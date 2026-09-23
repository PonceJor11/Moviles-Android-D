package com.ejemplo.clinicasalud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
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

    // Para saber qué pantalla está activa y marcarla en el menú
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Estado local para la lista de citas (Sin ViewModel)
    val citasState = remember { mutableStateListOf(*DatosFuente.listaCitasIniciales.toTypedArray()) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                drawerShape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp),
                modifier = Modifier.width(300.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Cabecera: Avatar JP + Nombre
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEDE7F6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "JP",
                            color = Color(0xFF5E248F),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Juan Pérez",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Paciente",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // 1. Opción Inicio
                val esInicioSelected = currentRoute == "inicio"
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Inicio",
                            fontWeight = if (esInicioSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    selected = esInicioSelected,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("inicio") {
                            popUpTo("inicio") { inclusive = true }
                        }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color(0xFFEDE7F6),
                        selectedTextColor = Color(0xFF5E248F),
                        unselectedContainerColor = Color.Transparent,
                        unselectedTextColor = Color.Black
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                // 2. Opción Mis citas
                val esCitasSelected = currentRoute == "citas"
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Mis citas",
                            fontWeight = if (esCitasSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    selected = esCitasSelected,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("citas")
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color(0xFFEDE7F6),
                        selectedTextColor = Color(0xFF5E248F),
                        unselectedContainerColor = Color.Transparent,
                        unselectedTextColor = Color.Black
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                // 3. Opción Historial médico
                NavigationDrawerItem(
                    label = { Text("Historial médico", fontWeight = FontWeight.Normal) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        unselectedTextColor = Color.Black
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                // 4. Opción Perfil
                NavigationDrawerItem(
                    label = { Text("Perfil", fontWeight = FontWeight.Normal) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        unselectedTextColor = Color.Black
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp)
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
                    onOpenDrawer = { scope.launch { drawerState.open() } },
                    onCancelarCita = { citaACancelar ->
                        val index = citasState.indexOfFirst { it.id == citaACancelar.id }
                        if (index != -1) {
                            citasState[index] = citaACancelar.copy(estado = "Cancelada")
                        }
                    }
                )
            }
        }
    }
}