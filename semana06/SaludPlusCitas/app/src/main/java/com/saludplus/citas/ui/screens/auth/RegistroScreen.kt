package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.model.Usuario
import com.saludplus.citas.data.repository.Repositorio
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var claveVisible by remember { mutableStateOf(false) }

    // Mensajes de error específicos por campo
    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorUsuario by remember { mutableStateOf<String?>(null) }
    var errorCorreo by remember { mutableStateOf<String?>(null) }
    var errorTelefono by remember { mutableStateOf<String?>(null) }
    var errorClave by remember { mutableStateOf<String?>(null) }
    var errorGeneral by remember { mutableStateOf<String?>(null) }

    val azulOscuro = Color(0xFF0D1B2A)
    val azulPrincipal = Color(0xFF0066FF)
    val grisFondoCampo = Color(0xFFF8FAFC)
    val grisBorde = Color(0xFFE2E8F0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateToLogin) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = azulOscuro
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Encabezado
            Text(
                text = "Crear cuenta",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = azulOscuro
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Regístrate para agendar tus citas",
                fontSize = 14.sp,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // 1. Campo Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    errorNombre = null
                },
                placeholder = { Text("Juan Pérez", color = Color(0xFF94A3B8)) },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = azulPrincipal)
                },
                isError = errorNombre != null,
                singleLine = true,
                textStyle = TextStyle(color = azulOscuro, fontSize = 15.sp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = grisFondoCampo,
                    unfocusedContainerColor = grisFondoCampo,
                    focusedIndicatorColor = azulPrincipal,
                    unfocusedIndicatorColor = grisBorde,
                    focusedTextColor = azulOscuro,
                    unfocusedTextColor = azulOscuro
                ),
                modifier = Modifier.fillMaxWidth()
            )
            errorNombre?.let {
                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start).padding(start = 8.dp, top = 2.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Campo Nombre de Usuario
            OutlinedTextField(
                value = usuario,
                onValueChange = {
                    usuario = it
                    errorUsuario = null
                },
                placeholder = { Text("juanperez", color = Color(0xFF94A3B8)) },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = azulPrincipal)
                },
                isError = errorUsuario != null,
                singleLine = true,
                textStyle = TextStyle(color = azulOscuro, fontSize = 15.sp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = grisFondoCampo,
                    unfocusedContainerColor = grisFondoCampo,
                    focusedIndicatorColor = azulPrincipal,
                    unfocusedIndicatorColor = grisBorde,
                    focusedTextColor = azulOscuro,
                    unfocusedTextColor = azulOscuro
                ),
                modifier = Modifier.fillMaxWidth()
            )
            errorUsuario?.let {
                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start).padding(start = 8.dp, top = 2.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Campo Correo Electrónico
            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it.trim()
                    errorCorreo = null
                },
                placeholder = { Text("juan@gmail.com", color = Color(0xFF94A3B8)) },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = azulPrincipal)
                },
                isError = errorCorreo != null,
                singleLine = true,
                textStyle = TextStyle(color = azulOscuro, fontSize = 15.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = grisFondoCampo,
                    unfocusedContainerColor = grisFondoCampo,
                    focusedIndicatorColor = azulPrincipal,
                    unfocusedIndicatorColor = grisBorde,
                    focusedTextColor = azulOscuro,
                    unfocusedTextColor = azulOscuro
                ),
                modifier = Modifier.fillMaxWidth()
            )
            errorCorreo?.let {
                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start).padding(start = 8.dp, top = 2.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Campo Teléfono
            OutlinedTextField(
                value = telefono,
                onValueChange = { input ->
                    if (input.all { it.isDigit() } && input.length <= 9) {
                        telefono = input
                        errorTelefono = null
                    }
                },
                placeholder = { Text("987654321", color = Color(0xFF94A3B8)) },
                leadingIcon = {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = azulPrincipal)
                },
                isError = errorTelefono != null,
                singleLine = true,
                textStyle = TextStyle(color = azulOscuro, fontSize = 15.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = grisFondoCampo,
                    unfocusedContainerColor = grisFondoCampo,
                    focusedIndicatorColor = azulPrincipal,
                    unfocusedIndicatorColor = grisBorde,
                    focusedTextColor = azulOscuro,
                    unfocusedTextColor = azulOscuro
                ),
                modifier = Modifier.fillMaxWidth()
            )
            errorTelefono?.let {
                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start).padding(start = 8.dp, top = 2.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 5. Campo Contraseña
            OutlinedTextField(
                value = clave,
                onValueChange = {
                    clave = it
                    errorClave = null
                },
                placeholder = { Text("juanperez123", color = Color(0xFF94A3B8)) },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = azulPrincipal)
                },
                trailingIcon = {
                    IconButton(onClick = { claveVisible = !claveVisible }) {
                        Icon(
                            imageVector = if (claveVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                            contentDescription = "Mostrar contraseña",
                            tint = Color(0xFF64748B)
                        )
                    }
                },
                visualTransformation = if (claveVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = errorClave != null,
                singleLine = true,
                textStyle = TextStyle(color = azulOscuro, fontSize = 15.sp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = grisFondoCampo,
                    unfocusedContainerColor = grisFondoCampo,
                    focusedIndicatorColor = azulPrincipal,
                    unfocusedIndicatorColor = grisBorde,
                    focusedTextColor = azulOscuro,
                    unfocusedTextColor = azulOscuro
                ),
                modifier = Modifier.fillMaxWidth()
            )
            errorClave?.let {
                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start).padding(start = 8.dp, top = 2.dp))
            }

            errorGeneral?.let {
                Spacer(modifier = Modifier.height(10.dp))
                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón "Registrarse"
            Button(
                onClick = {
                    errorNombre = null
                    errorUsuario = null
                    errorCorreo = null
                    errorTelefono = null
                    errorClave = null
                    errorGeneral = null

                    var esValido = true

                    if (nombre.trim().length < 3) {
                        errorNombre = "Ingresa tu nombre completo (mínimo 3 caracteres)."
                        esValido = false
                    }

                    if (usuario.trim().length < 3) {
                        errorUsuario = "Ingresa un usuario válido."
                        esValido = false
                    }

                    if (!correo.contains("@") || !correo.endsWith(".com") || correo.length < 6) {
                        errorCorreo = "Ingresa un correo válido (ejemplo: juan@gmail.com)."
                        esValido = false
                    }

                    if (telefono.length != 9) {
                        errorTelefono = "El teléfono debe contener exactamente 9 dígitos."
                        esValido = false
                    }

                    if (clave.length < 6) {
                        errorClave = "La contraseña debe tener al menos 6 caracteres."
                        esValido = false
                    }

                    if (esValido) {
                        val nuevoUsuario = Usuario(
                            id = UUID.randomUUID().toString(),
                            nombre = nombre,
                            username = usuario,
                            correo = correo,
                            telefono = "+51 $telefono",
                            clave = clave
                        )
                        val ok = Repositorio.registrarUsuario(nuevoUsuario)
                        if (ok) {
                            Repositorio.iniciarSesion(correo, clave)
                            onNavigateToHome()
                        } else {
                            errorCorreo = "Este correo ya está registrado en la plataforma."
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = azulPrincipal),
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Registrarse",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Términos y Condiciones
            Text(
                text = "Al registrarte, aceptas nuestros",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
            )
            Text(
                text = "Términos y Condiciones",
                fontSize = 12.sp,
                color = azulPrincipal,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Ir a Iniciar Sesión
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Ya tienes cuenta? ",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B)
                )
                TextButton(
                    onClick = onNavigateToLogin,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Iniciar sesión",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = azulPrincipal
                    )
                }
            }
        }
    }
}