package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.viewmodel.SesionViewModel
import kotlinx.coroutines.launch

private val ColorAzulVet = Color(0xFF1D51D8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesScreen(
    sesionVm: SesionViewModel,
    prefsStore: PreferenciasStore,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val modoOscuro by sesionVm.modoOscuro.collectAsState()
    val notificaciones by prefsStore.notificaciones.collectAsState(initial = true)
    val aceptarCitas by prefsStore.aceptarCitas.collectAsState(initial = true)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Ajustes", fontWeight = FontWeight.Bold) }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Usuario Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color(0xFFE8F0FE), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = ColorAzulVet, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "Dra. Valeria Ríos", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = "valeria@huellitas.com", fontSize = 13.sp, color = Color.Gray)
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFE8F0FE)
                        ) {
                            Text(
                                text = "Perfil activo",
                                color = ColorAzulVet,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Cambiar de perfil
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = ColorAzulVet),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        coroutineScope.launch {
                            prefsStore.guardarSesion(Rol.CLIENTE.name, 1)
                            onCambiarPerfil()
                        }
                    }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Cambiar de perfil", color = Color.White, fontWeight = FontWeight.Bold)
                            Text(text = "Modo práctica: ir a vista Dueño", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        }
                    }
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Preferencias", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            // Switches
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Modo oscuro", fontWeight = FontWeight.Medium)
                    Text(text = "Cambia el tema de la app", fontSize = 12.sp, color = Color.Gray)
                }
                Switch(
                    checked = modoOscuro,
                    onCheckedChange = {
                        coroutineScope.launch { prefsStore.guardarModoOscuro(it) }
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Notificaciones", fontWeight = FontWeight.Medium)
                    Text(text = "Avisar citas nuevas", fontSize = 12.sp, color = Color.Gray)
                }
                Switch(
                    checked = notificaciones,
                    onCheckedChange = {
                        coroutineScope.launch { prefsStore.guardarNotificaciones(it) }
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Aceptar citas", fontWeight = FontWeight.Medium)
                    Text(text = "Abrir o cerrar la agenda", fontSize = 12.sp, color = Color.Gray)
                }
                Switch(
                    checked = aceptarCitas,
                    onCheckedChange = {
                        coroutineScope.launch { prefsStore.guardarAceptarCitas(it) }
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = { sesionVm.cerrarSesion(onCierre = onCerrarSesion) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Cerrar sesión", fontWeight = FontWeight.Bold)
            }
        }
    }
}