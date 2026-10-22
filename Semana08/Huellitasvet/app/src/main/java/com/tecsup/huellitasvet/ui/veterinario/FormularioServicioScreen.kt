package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel

private val ColorAzulVet = Color(0xFF1D51D8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioServicioScreen(
    servicioId: Int? = null,
    vm: ServiciosViewModel,
    onVolver: () -> Unit
) {
    val servicios by vm.servicios.collectAsState()
    val errores by vm.errores.collectAsState()

    val servicioState = remember(servicioId, servicios) {
        servicios.find { it.id == servicioId }
    }

    var nombre by remember { mutableStateOf("") }
    var duracionMin by remember { mutableIntStateOf(30) }
    var precioStr by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var icono by remember { mutableStateOf("heart") }
    var activo by remember { mutableStateOf(true) }

    LaunchedEffect(servicioState) {
        servicioState?.let { s ->
            nombre = s.nombre
            duracionMin = s.duracionMin
            precioStr = s.precio.toString()
            descripcion = s.descripcion
            icono = s.icono
            activo = s.activo
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (servicioId == null || servicioId == 0) "Nuevo servicio" else "Editar servicio", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del servicio") },
                isError = errores.nombre != null,
                supportingText = { errores.nombre?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Duración (Chips)
            Text(text = "Duración", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(15, 30, 45, 60).forEach { min ->
                    val selected = duracionMin == min
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .background(
                                color = if (selected) ColorAzulVet else Color.White,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .border(1.dp, if (selected) ColorAzulVet else Color.LightGray, RoundedCornerShape(20.dp))
                            .clickable { duracionMin = min },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "$min min", color = if (selected) Color.White else Color.Black, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Precio
            OutlinedTextField(
                value = precioStr,
                onValueChange = { precioStr = it },
                label = { Text("Precio (S/)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = errores.precio != null,
                supportingText = { errores.precio?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                minLines = 2,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ícono
            Text(text = "Ícono", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                listOf("heart" to Icons.Default.Favorite, "syringe" to Icons.Default.MedicalServices, "scissors" to Icons.Default.ContentCut).forEach { (clave, vector) ->
                    val selected = icono == clave
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(
                                color = if (selected) Color(0xFFE8F0FE) else Color.White,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, if (selected) ColorAzulVet else Color.LightGray, RoundedCornerShape(12.dp))
                            .clickable { icono = clave },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(vector, contentDescription = null, tint = if (selected) ColorAzulVet else Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Switch Servicio Activo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Servicio activo", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(text = "Los dueños podrán pedirlo", fontSize = 12.sp, color = Color.Gray)
                }
                Switch(
                    checked = activo,
                    onCheckedChange = { activo = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ColorAzulVet)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Botón Guardar
            Button(
                onClick = {
                    vm.guardar(
                        id = servicioId ?: 0,
                        nombre = nombre,
                        duracionMin = duracionMin,
                        precioStr = precioStr,
                        descripcion = descripcion,
                        icono = icono,
                        activo = activo,
                        onExito = onVolver
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = ColorAzulVet),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Guardar servicio", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onVolver,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Cancelar", fontSize = 16.sp, color = ColorAzulVet)
            }
        }
    }
}