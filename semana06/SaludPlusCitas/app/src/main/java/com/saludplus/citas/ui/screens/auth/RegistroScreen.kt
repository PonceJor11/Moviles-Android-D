package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.model.Usuario
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.CampoTextoSaludPlus

@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit,
    onNavigateToTerminos: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        CampoTextoSaludPlus(value = nombre, onValueChange = { nombre = it }, label = "Nombre completo")
        Spacer(modifier = Modifier.height(12.dp))
        CampoTextoSaludPlus(value = correo, onValueChange = { correo = it }, label = "Correo electrónico")
        Spacer(modifier = Modifier.height(12.dp))
        CampoTextoSaludPlus(value = clave, onValueChange = { clave = it }, label = "Contraseña")

        mensajeError?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Al registrarte aceptas los Términos y Condiciones",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onNavigateToTerminos() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        BotonPrincipal(
            texto = "Registrarme",
            onClick = {
                if (nombre.isNotBlank() && correo.isNotBlank() && clave.isNotBlank()) {
                    val nuevoUsuario = Usuario(id = System.currentTimeMillis().toString(), nombre = nombre, correo = correo, clave = clave)
                    if (Repositorio.registrarUsuario(nuevoUsuario)) {
                        onRegistroExitoso()
                    } else {
                        mensajeError = "El correo ya está registrado"
                    }
                } else {
                    mensajeError = "Completa todos los campos"
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onNavigateToLogin, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}