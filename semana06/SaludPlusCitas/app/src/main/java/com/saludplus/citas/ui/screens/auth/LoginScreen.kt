package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.CampoTextoSaludPlus

@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    onNavigateToRegistro: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        CampoTextoSaludPlus(value = correo, onValueChange = { correo = it }, label = "Correo electrónico")
        Spacer(modifier = Modifier.height(12.dp))
        CampoTextoSaludPlus(value = clave, onValueChange = { clave = it }, label = "Contraseña")

        if (error) {
            Text(
                "Credenciales incorrectas",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        BotonPrincipal(
            texto = "Ingresar",
            onClick = {
                val user = Repositorio.iniciarSesion(correo, clave)
                if (user != null) {
                    onLoginExitoso()
                } else {
                    error = true
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = onNavigateToRegistro,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}