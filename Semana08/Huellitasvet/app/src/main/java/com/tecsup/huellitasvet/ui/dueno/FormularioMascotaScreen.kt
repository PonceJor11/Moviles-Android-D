package com.tecsup.huellitasvet.ui.dueno

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Sexo
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel

private val ColorVerdePrincipal = Color(0xFF00966D)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioMascotaScreen(
    mascotaId: Int? = null,
    vm: MascotaViewModel,
    onVolver: () -> Unit
) {
    val mascotaState by if (mascotaId != null && mascotaId > 0) {
        vm.obtenerMascota(mascotaId).collectAsState()
    } else {
        remember { mutableStateOf(null) }
    }

    var nombre by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf(Especie.PERRO.name) }
    var raza by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf(Sexo.MACHO.name) }

    LaunchedEffect(mascotaState) {
        mascotaState?.let { m ->
            nombre = m.nombre
            especie = m.especie
            raza = m.raza
            anio = m.anioNacimiento.toString()
            peso = m.peso.toString()
            sexo = m.sexo
        }
    }

    val errores by vm.errores.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (mascotaId == null || mascotaId == 0) "Nueva mascota" else "Editar mascota",
                        fontWeight = FontWeight.Bold
                    )
                },
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar según especie
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(Color(0xFFE8F5E9), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Pets,
                        contentDescription = null,
                        tint = ColorVerdePrincipal,
                        modifier = Modifier.size(50.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(ColorVerdePrincipal, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Campo Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                isError = errores.nombre != null,
                supportingText = { errores.nombre?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Chips Especie
            Text(text = "Especie", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Perro" to Especie.PERRO.name, "Gato" to Especie.GATO.name, "Otro" to Especie.OTRO.name).forEach { (label, value) ->
                    val selected = especie == value
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .background(
                                color = if (selected) ColorVerdePrincipal else Color.White,
                                shape = RoundedCornerShape(22.dp)
                            )
                            .border(1.dp, if (selected) ColorVerdePrincipal else Color.LightGray, RoundedCornerShape(22.dp))
                            .clickable { especie = value },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = label, color = if (selected) Color.White else Color.Black, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Raza
            OutlinedTextField(
                value = raza,
                onValueChange = { raza = it },
                label = { Text("Raza") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Fila Año y Peso
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = anio,
                    onValueChange = { anio = it },
                    label = { Text("Año de nacimiento") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = errores.anio != null,
                    supportingText = { errores.anio?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 10.sp) } },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = peso,
                    onValueChange = { peso = it },
                    label = { Text("Peso (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = errores.peso != null,
                    supportingText = { errores.peso?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 10.sp) } },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chips Sexo
            Text(text = "Sexo", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf("Macho" to Sexo.MACHO.name, "Hembra" to Sexo.HEMBRA.name).forEach { (label, value) ->
                    val selected = sexo == value
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .background(
                                color = if (selected) Color(0xFFE8F5E9) else Color.White,
                                shape = RoundedCornerShape(22.dp)
                            )
                            .border(1.dp, if (selected) ColorVerdePrincipal else Color.LightGray, RoundedCornerShape(22.dp))
                            .clickable { sexo = value },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = label, color = if (selected) ColorVerdePrincipal else Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Botón Guardar
            Button(
                onClick = {
                    vm.guardar(
                        id = mascotaId ?: 0,
                        nombre = nombre,
                        especie = especie,
                        raza = raza,
                        anioStr = anio,
                        pesoStr = peso,
                        sexo = sexo,
                        onExito = onVolver
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = ColorVerdePrincipal),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Guardar mascota", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón Cancelar
            OutlinedButton(
                onClick = onVolver,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Cancelar", fontSize = 16.sp, color = ColorVerdePrincipal)
            }
        }
    }
}