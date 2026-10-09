package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import com.saludplus.citas.data.repository.Repositorio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    onNavigateToRegistro: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var claveVisible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf(false) }

    val azulOscuro = Color(0xFF0D1B2A)
    val azulPrincipal = Color(0xFF0066FF)
    val grisFondoCampo = Color(0xFFF8FAFC)
    val grisBorde = Color(0xFFE2E8F0)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¡Bienvenido!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = azulOscuro
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Ingresa tus credenciales para continuar",
                fontSize = 14.sp,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Campo Correo Electrónico
            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    error = false
                },
                placeholder = { Text("juan@gmail.com", color = Color(0xFF94A3B8)) },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = azulPrincipal)
                },
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

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Contraseña
            OutlinedTextField(
                value = clave,
                onValueChange = {
                    clave = it
                    error = false
                },
                placeholder = { Text("Contraseña", color = Color(0xFF94A3B8)) },
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

            if (error) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Correo o contraseña incorrectos",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Start).padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Ingresar
            Button(
                onClick = {
                    val user = Repositorio.iniciarSesion(correo.trim(), clave)
                    if (user != null) {
                        onLoginExitoso()
                    } else {
                        error = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = azulPrincipal),
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Ingresar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿No tienes cuenta? ",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B)
                )
                TextButton(
                    onClick = onNavigateToRegistro,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Regístrate aquí",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = azulPrincipal
                    )
                }
            }
        }
    }
}