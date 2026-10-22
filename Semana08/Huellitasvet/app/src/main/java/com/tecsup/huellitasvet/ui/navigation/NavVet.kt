package com.tecsup.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.dueno.*
import com.tecsup.huellitasvet.ui.veterinario.*
import com.tecsup.huellitasvet.viewmodel.*

object Destinos {
    const val SPLASH = "splash"
    const val LOGIN = "login"

    // Flujo Dueño
    const val INICIO_DUENO = "inicio_dueno"
    const val MASCOTAS = "mascotas"
    const val PEDIR_CITA = "pedir_cita"
    const val MIS_CITAS = "mis_citas"
    const val PERFIL = "perfil"
    const val FORMULARIO_MASCOTA = "formulario_mascota"
    const val FICHA_MASCOTA = "ficha_mascota"

    // Flujo Veterinario
    const val PANEL_VET = "panel_vet"
    const val AGENDA = "agenda"
    const val PACIENTES = "pacientes"
    const val SERVICIOS = "servicios"
    const val AJUSTES = "ajustes"
    const val ATENDER_CITA = "atender_cita"
    const val FORMULARIO_SERVICIO = "formulario_servicio"
}

@Composable
fun NavVet(
    sesionViewModel: SesionViewModel,
    inicioViewModel: InicioViewModel,
    mascotaViewModel: MascotaViewModel,
    pedirCitaViewModel: PedirCitaViewModel,
    misCitasViewModel: MisCitasViewModel,
    panelViewModel: PanelViewModel,
    agendaViewModel: AgendaViewModel,
    atenderCitaViewModelFactory: (Int) -> AtenderCitaViewModel,
    pacientesViewModel: PacientesViewModel,
    serviciosViewModel: ServiciosViewModel,
    prefsStore: PreferenciasStore
) {
    val navController = rememberNavController()
    val perfilActivo by sesionViewModel.perfilActivo.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    val mostrarBottomBar = rutaActual in listOf(
        Destinos.INICIO_DUENO, Destinos.MASCOTAS, Destinos.MIS_CITAS, Destinos.PERFIL,
        Destinos.PANEL_VET, Destinos.AGENDA, Destinos.PACIENTES, Destinos.SERVICIOS, Destinos.AJUSTES
    )

    Scaffold(
        bottomBar = {
            if (mostrarBottomBar) {
                NavigationBar {
                    if (perfilActivo == Rol.CLIENTE.name) {
                        NavigationBarItem(
                            selected = rutaActual == Destinos.INICIO_DUENO,
                            onClick = { navController.navigate(Destinos.INICIO_DUENO) { popUpTo(navController.graph.findStartDestination().id) { saveState = true }; launchSingleTop = true; restoreState = true } },
                            icon = { Icon(Icons.Default.Home, contentDescription = null) },
                            label = { Text("Inicio") }
                        )
                        NavigationBarItem(
                            selected = rutaActual == Destinos.MASCOTAS,
                            onClick = { navController.navigate(Destinos.MASCOTAS) { popUpTo(navController.graph.findStartDestination().id) { saveState = true }; launchSingleTop = true; restoreState = true } },
                            icon = { Icon(Icons.Default.Pets, contentDescription = null) },
                            label = { Text("Mascotas") }
                        )
                        NavigationBarItem(
                            selected = rutaActual == Destinos.MIS_CITAS,
                            onClick = { navController.navigate(Destinos.MIS_CITAS) { popUpTo(navController.graph.findStartDestination().id) { saveState = true }; launchSingleTop = true; restoreState = true } },
                            icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                            label = { Text("Citas") }
                        )
                        NavigationBarItem(
                            selected = rutaActual == Destinos.PERFIL,
                            onClick = { navController.navigate(Destinos.PERFIL) { popUpTo(navController.graph.findStartDestination().id) { saveState = true }; launchSingleTop = true; restoreState = true } },
                            icon = { Icon(Icons.Default.Person, contentDescription = null) },
                            label = { Text("Perfil") }
                        )
                    } else {
                        NavigationBarItem(
                            selected = rutaActual == Destinos.PANEL_VET,
                            onClick = { navController.navigate(Destinos.PANEL_VET) { popUpTo(navController.graph.findStartDestination().id) { saveState = true }; launchSingleTop = true; restoreState = true } },
                            icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                            label = { Text("Panel") }
                        )
                        NavigationBarItem(
                            selected = rutaActual == Destinos.AGENDA,
                            onClick = { navController.navigate(Destinos.AGENDA) { popUpTo(navController.graph.findStartDestination().id) { saveState = true }; launchSingleTop = true; restoreState = true } },
                            icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                            label = { Text("Agenda") }
                        )
                        NavigationBarItem(
                            selected = rutaActual == Destinos.PACIENTES,
                            onClick = { navController.navigate(Destinos.PACIENTES) { popUpTo(navController.graph.findStartDestination().id) { saveState = true }; launchSingleTop = true; restoreState = true } },
                            icon = { Icon(Icons.Default.Pets, contentDescription = null) },
                            label = { Text("Pacientes") }
                        )
                        NavigationBarItem(
                            selected = rutaActual == Destinos.SERVICIOS,
                            onClick = { navController.navigate(Destinos.SERVICIOS) { popUpTo(navController.graph.findStartDestination().id) { saveState = true }; launchSingleTop = true; restoreState = true } },
                            icon = { Icon(Icons.Default.MedicalServices, contentDescription = null) },
                            label = { Text("Servicios") }
                        )
                        NavigationBarItem(
                            selected = rutaActual == Destinos.AJUSTES,
                            onClick = { navController.navigate(Destinos.AJUSTES) { popUpTo(navController.graph.findStartDestination().id) { saveState = true }; launchSingleTop = true; restoreState = true } },
                            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                            label = { Text("Ajustes") }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Destinos.SPLASH,
            modifier = Modifier.padding(padding)
        ) {
            composable(Destinos.SPLASH) {
                LaunchedEffect(perfilActivo) {
                    when (perfilActivo) {
                        Rol.CLIENTE.name -> navController.navigate(Destinos.INICIO_DUENO) { popUpTo(Destinos.SPLASH) { inclusive = true } }
                        Rol.ADMIN.name -> navController.navigate(Destinos.PANEL_VET) { popUpTo(Destinos.SPLASH) { inclusive = true } }
                        else -> navController.navigate(Destinos.LOGIN) { popUpTo(Destinos.SPLASH) { inclusive = true } }
                    }
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            composable(Destinos.LOGIN) {
                LoginScreen(
                    vm = sesionViewModel,
                    onLoginExito = { rol ->
                        val destino = if (rol == Rol.CLIENTE.name) Destinos.INICIO_DUENO else Destinos.PANEL_VET
                        navController.navigate(destino) { popUpTo(Destinos.LOGIN) { inclusive = true } }
                    }
                )
            }

            // ---------- DUEÑO ----------
            composable(Destinos.INICIO_DUENO) {
                InicioScreen(
                    nombreUsuario = "Carlos",
                    vm = inicioViewModel,
                    onVerMascotas = { navController.navigate(Destinos.MASCOTAS) },
                    onVerFichaMascota = { id -> navController.navigate("${Destinos.FICHA_MASCOTA}/$id") },
                    onAgregarMascota = { navController.navigate("${Destinos.FORMULARIO_MASCOTA}/0") },
                    onPedirCita = { navController.navigate(Destinos.PEDIR_CITA) }
                )
            }

            composable(Destinos.MASCOTAS) {
                MascotasScreen(
                    vm = inicioViewModel,
                    onVerFicha = { id -> navController.navigate("${Destinos.FICHA_MASCOTA}/$id") },
                    onAgregarMascota = { navController.navigate("${Destinos.FORMULARIO_MASCOTA}/0") }
                )
            }

            composable(
                route = "${Destinos.FORMULARIO_MASCOTA}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                FormularioMascotaScreen(
                    mascotaId = id,
                    vm = mascotaViewModel,
                    onVolver = { navController.popBackStack() }
                )
            }

            composable(
                route = "${Destinos.FICHA_MASCOTA}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                FichaMascotaScreen(
                    mascotaId = id,
                    vm = mascotaViewModel,
                    onVolver = { navController.popBackStack() },
                    onEditar = { mId -> navController.navigate("${Destinos.FORMULARIO_MASCOTA}/$mId") },
                    onPedirCita = { navController.navigate(Destinos.PEDIR_CITA) }
                )
            }

            composable(Destinos.PEDIR_CITA) {
                PedirCitaScreen(
                    vm = pedirCitaViewModel,
                    onVolver = { navController.popBackStack() },
                    onCitaConfirmada = { navController.navigate(Destinos.MIS_CITAS) { popUpTo(Destinos.INICIO_DUENO) } }
                )
            }

            composable(Destinos.MIS_CITAS) {
                MisCitasScreen(
                    vm = misCitasViewModel,
                    onPedirCita = { navController.navigate(Destinos.PEDIR_CITA) }
                )
            }

            composable(Destinos.PERFIL) {
                PerfilScreen(
                    sesionVm = sesionViewModel,
                    prefsStore = prefsStore,
                    onCambiarPerfil = { navController.navigate(Destinos.PANEL_VET) { popUpTo(0) { inclusive = true } } },
                    onCerrarSesion = { navController.navigate(Destinos.LOGIN) { popUpTo(0) { inclusive = true } } }
                )
            }

            // ---------- VETERINARIO ----------
            composable(Destinos.PANEL_VET) {
                PanelScreen(
                    vm = panelViewModel,
                    onVerAgenda = { navController.navigate(Destinos.AGENDA) }
                )
            }

            composable(Destinos.AGENDA) {
                AgendaScreen(
                    vm = agendaViewModel,
                    onAtenderCita = { id -> navController.navigate("${Destinos.ATENDER_CITA}/$id") }
                )
            }

            composable(
                route = "${Destinos.ATENDER_CITA}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val citaId = entry.arguments?.getInt("id") ?: 0
                val vm = atenderCitaViewModelFactory(citaId)
                AtenderCitaScreen(
                    vm = vm,
                    onVolver = { navController.popBackStack() }
                )
            }

            composable(Destinos.PACIENTES) {
                PacientesScreen(
                    vm = pacientesViewModel,
                    onVerFicha = { id -> navController.navigate("${Destinos.FICHA_MASCOTA}/$id") }
                )
            }

            composable(Destinos.SERVICIOS) {
                val serviciosList by serviciosViewModel.servicios.collectAsState()
                val mensajeError by serviciosViewModel.mensaje.collectAsState()

                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { navController.navigate("${Destinos.FORMULARIO_SERVICIO}/0") },
                            containerColor = Color(0xFF1D51D8)
                        ) {
                            Icon(Icons.Default.MedicalServices, contentDescription = null, tint = Color.White)
                        }
                    }
                ) { p ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(p)
                            .padding(16.dp)
                    ) {
                        Text(text = "Servicios", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                        Spacer(modifier = Modifier.height(12.dp))

                        if (mensajeError != null) {
                            Text(text = mensajeError!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        serviciosList.forEach { s ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = s.nombre, fontWeight = FontWeight.Bold)
                                        Text(text = "${s.duracionMin} min · S/ ${s.precio.toInt()}", fontSize = 12.sp, color = Color.Gray)
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Switch(
                                            checked = s.activo,
                                            onCheckedChange = { serviciosViewModel.alternar(s) }
                                        )
                                        IconButton(onClick = { navController.navigate("${Destinos.FORMULARIO_SERVICIO}/${s.id}") }) {
                                            Icon(Icons.Default.Person, contentDescription = null)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            composable(
                route = "${Destinos.FORMULARIO_SERVICIO}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                FormularioServicioScreen(
                    servicioId = id,
                    vm = serviciosViewModel,
                    onVolver = { navController.popBackStack() }
                )
            }

            composable(Destinos.AJUSTES) {
                AjustesScreen(
                    sesionVm = sesionViewModel,
                    prefsStore = prefsStore,
                    onCambiarPerfil = { navController.navigate(Destinos.INICIO_DUENO) { popUpTo(0) { inclusive = true } } },
                    onCerrarSesion = { navController.navigate(Destinos.LOGIN) { popUpTo(0) { inclusive = true } } }
                )
            }
        }
    }
}