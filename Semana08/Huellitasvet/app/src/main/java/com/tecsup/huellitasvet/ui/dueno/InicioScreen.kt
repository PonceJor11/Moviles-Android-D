package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CircleNotifications
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.viewmodel.InicioViewModel

private val ColorVerdePrinicpal = Color(0xFF00966D)
private val ColorVerdeClaro = Color(0xFFE8F5E9)

@Composable
fun InicioScreen(
    nombreUsuario: String,
    vm: InicioViewModel,
    onVerMascotas: () -> Unit,
    onVerFichaMascota: (Int) -> Unit,
    onAgregarMascota: () -> Unit,
    onPedirCita: () -> Unit
) {
    val mascotas by vm.mascotas.collectAsState()
    val proximaCita by vm.proximaCita.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Cabecera Saludo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Hola, $nombreUsuario",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "¿Cómo están tus mascotas hoy?",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.CircleNotifications, contentDescription = "Notificaciones", tint = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Tarjeta Próxima Cita
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (proximaCita != null) ColorVerdePrinicpal else Color(0xFFE0E0E0)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (proximaCita != null) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Próxima cita", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${proximaCita!!.mascota.mascota.nombre} · ${proximaCita!!.servicio.nombre}",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Pets, contentDescription = null, tint = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${proximaCita!!.cita.fecha} · ${proximaCita!!.cita.hora}",
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White
                    ) {
                        Text(
                            text = proximaCita!!.cita.estado.lowercase().replaceFirstChar { it.uppercase() },
                            color = ColorVerdePrinicpal,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Aún no tienes citas agendadas", color = Color.DarkGray, fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sección Mascotas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Mis mascotas", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "Ver todas",
                color = ColorVerdePrinicpal,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onVerMascotas() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(mascotas, key = { it.id }) { m ->
                ItemMascotaCarrusel(mascota = m, onClick = { onVerFichaMascota(m.id) })
            }
            item {
                ItemAgregarMascotaCarrusel(onClick = onAgregarMascota)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sección Servicios Rápidos
        Text(text = "Servicios", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ItemServicioRapido("Consulta", Icons.Default.Favorite)
            ItemServicioRapido("Vacunas", Icons.Default.MedicalServices)
            ItemServicioRapido("Baño", Icons.Default.ContentCut)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Pedir una cita
        Button(
            onClick = onPedirCita,
            colors = ButtonDefaults.buttonColors(containerColor = ColorVerdePrinicpal),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(text = "Pedir una cita", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun ItemMascotaCarrusel(mascota: Mascota, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .width(100.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(ColorVerdeClaro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Pets, contentDescription = null, tint = ColorVerdePrinicpal)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = mascota.nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = "Ver ficha", fontSize = 11.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ItemAgregarMascotaCarrusel(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(110.dp)
            .border(1.dp, ColorVerdePrinicpal, RoundedCornerShape(16.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Add, contentDescription = null, tint = ColorVerdePrinicpal)
            Text(text = "Agregar", color = ColorVerdePrinicpal, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}

@Composable
fun ItemServicioRapido(nombre: String, icono: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ColorVerdeClaro),
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icono, contentDescription = null, tint = ColorVerdePrinicpal)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = nombre, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorVerdePrinicpal)
        }
    }
}