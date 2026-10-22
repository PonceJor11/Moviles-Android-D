package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

private val ColorDueno = Color(0xFF00966D) // Verde
private val ColorVet = Color(0xFF1D51D8)   // Azul

@Composable
fun LoginScreen(
    vm: SesionViewModel,
    onLoginExito: (String) -> Unit
) {
    var rolSeleccionado by remember { mutableStateOf(Rol.CLIENTE) }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var claveVisible by remember { mutableStateOf(false) }

    val errorState by vm.error.collectAsState()
    val colorTema by animateColorAsState(
        targetValue = if (rolSeleccionado == Rol.CLIENTE) ColorDueno else ColorVet,
        label = "ColorTemaAnimation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título principal
        Text(
            text = "Bienvenido",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Ingresa para continuar",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        // Selector de Rol (Dueño / Veterinario)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color(0xFFF2F2F2), shape = RoundedCornerShape(24.dp))
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Opción Dueño
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        color = if (rolSeleccionado == Rol.CLIENTE) ColorDueno else Color.Transparent,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable {
                        rolSeleccionado = Rol.CLIENTE
                        vm.limpiarError()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Dueño",
                    fontWeight = FontWeight.Bold,
                    color = if (rolSeleccionado == Rol.CLIENTE) Color.White else Color.Gray
                )
            }

            // Opción Veterinario
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        color = if (rolSeleccionado == Rol.ADMIN) ColorVet else Color.Transparent,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable {
                        rolSeleccionado = Rol.ADMIN
                        vm.limpiarError()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Veterinario",
                    fontWeight = FontWeight.Bold,
                    color = if (rolSeleccionado == Rol.ADMIN) Color.White else Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Campo Correo
        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
                vm.limpiarError()
            },
            label = { Text("Correo") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = colorTema) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Campo Contraseña
        OutlinedTextField(
            value = clave,
            onValueChange = {
                clave = it
                vm.limpiarError()
            },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = colorTema) },
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

        // Mensaje de Error (si existe)
        if (errorState != null) {
            Text(
                text = errorState!!,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }

        Text(
            text = "¿Olvidaste tu contraseña?",
            color = colorTema,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp, bottom = 24.dp)
                .clickable { /* Acción opcional */ }
        )

        // Botón Ingresar
        Button(
            onClick = {
                vm.login(correo, clave, rolSeleccionado, onLoginExito)
            },
            colors = ButtonDefaults.buttonColors(containerColor = colorTema),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Ingresar",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = "¿No tienes cuenta? ", color = Color.Gray, fontSize = 13.sp)
            Text(
                text = "Regístrate",
                color = colorTema,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.clickable { /* Registro */ }
            )
        }

        // Bloque informativo cuando está en modo Veterinario
        if (rolSeleccionado == Rol.ADMIN) {
            Spacer(modifier = Modifier.height(32.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF4FF)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Modo práctica",
                        fontWeight = FontWeight.Bold,
                        color = ColorVet,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Puedes cambiar de perfil desde Ajustes para probar ambos roles.",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}