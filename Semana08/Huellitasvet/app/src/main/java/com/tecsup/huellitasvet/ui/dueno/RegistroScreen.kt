package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.entity.Usuario

private val ColorVerdePrincipal = Color(0xFF00966D)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit,
    onVolverALogin: () -> Unit,
    onRegistrarUsuario: (Usuario, (Boolean) -> Unit) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var distrito by remember { mutableStateOf("Cayma") }
    var clave by remember { mutableStateOf("") }
    var claveVisible by remember { mutableStateOf(false) }

    var errorGeneral by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(false) }

    val distritosArequipa = listOf(
        "Cayma", "Cerro Colorado", "Yanahuara", "José Luis Bustamante y Rivero",
        "Arequipa Cercado", "Paucarpata", "Sachaca", "Socabaya"
    )
    var expandidoDistrito by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onVolverALogin) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorVerdePrincipal)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF6F8FB))
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Únete a Huellitasvet 🐾",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorVerdePrincipal
                    )
                    Text(
                        text = "Registra tus datos para gestionar las citas y salud de tus mascotas.",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 1. Campo Nombre completo
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre Completo") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ColorVerdePrincipal) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // 2. Campo Correo electrónico con teclado específico
                    OutlinedTextField(
                        value = correo,
                        onValueChange = {
                            correo = it.lowercase().trim()
                            errorGeneral = null
                        },
                        label = { Text("Correo Electrónico") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = ColorVerdePrincipal) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // 3. Campo Teléfono (solo dígitos numéricos, máx. 9 números)
                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() } && input.length <= 9) {
                                telefono = input
                                errorGeneral = null
                            }
                        },
                        label = { Text("Teléfono / Celular") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = ColorVerdePrincipal) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // 4. Selector de Distrito (ComboBox Desplegable)
                    ExposedDropdownMenuBox(
                        expanded = expandidoDistrito,
                        onExpandedChange = { expandidoDistrito = !expandidoDistrito },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = distrito,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Distrito") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = ColorVerdePrincipal) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoDistrito) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandidoDistrito,
                            onDismissRequest = { expandidoDistrito = false }
                        ) {
                            distritosArequipa.forEach { op ->
                                DropdownMenuItem(
                                    text = { Text(op) },
                                    onClick = {
                                        distrito = op
                                        expandidoDistrito = false
                                    }
                                )
                            }
                        }
                    }

                    // 5. Campo Contraseña
                    OutlinedTextField(
                        value = clave,
                        onValueChange = {
                            clave = it
                            errorGeneral = null
                        },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ColorVerdePrincipal) },
                        trailingIcon = {
                            IconButton(onClick = { claveVisible = !claveVisible }) {
                                Icon(
                                    imageVector = if (claveVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Mostrar clave"
                                )
                            }
                        },
                        singleLine = true,
                        visualTransformation = if (claveVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Mensaje de Error de Validación
                    if (errorGeneral != null) {
                        Text(
                            text = errorGeneral!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Botón Registrarse estilo verde principal
                    Button(
                        onClick = {
                            val regexCorreo = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]+$"
                            when {
                                nombre.isBlank() -> errorGeneral = "Ingresa tu nombre completo."
                                !correo.matches(regexCorreo.toRegex()) -> errorGeneral = "Ingresa un correo válido (ej: usuario@gmail.com)."
                                telefono.length < 9 -> errorGeneral = "El teléfono debe tener 9 dígitos."
                                clave.length < 4 -> errorGeneral = "La contraseña debe tener al menos 4 caracteres."
                                else -> {
                                    cargando = true
                                    errorGeneral = null
                                    val nuevoUsuario = Usuario(
                                        nombre = nombre,
                                        correo = correo,
                                        clave = clave,
                                        rol = "CLIENTE",
                                        telefono = telefono,
                                        distrito = distrito
                                    )
                                    onRegistrarUsuario(nuevoUsuario) { exito ->
                                        cargando = false
                                        if (exito) {
                                            onRegistroExitoso()
                                        } else {
                                            errorGeneral = "El correo ya está registrado."
                                        }
                                    }
                                }
                            }
                        },
                        enabled = !cargando,
                        colors = ButtonDefaults.buttonColors(containerColor = ColorVerdePrincipal),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Registrarme", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}