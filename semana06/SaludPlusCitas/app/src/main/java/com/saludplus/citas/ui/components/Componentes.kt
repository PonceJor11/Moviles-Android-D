package com.saludplus.citas.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarSaludPlus(
    titulo: String,
    mostrarBotonAtras: Boolean = true,
    onBackClick: () -> Unit = {}
) {
    val azulOscuro = Color(0xFF0D1B2A)

    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = azulOscuro
                )
            }
        },
        navigationIcon = {
            if (mostrarBotonAtras) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = azulOscuro
                    )
                }
            }
        },
        actions = {
            if (mostrarBotonAtras) {
                Spacer(modifier = Modifier.width(48.dp))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun BotonPrincipal(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val azulPrincipal = Color(0xFF0066FF)

    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = azulPrincipal),
        shape = RoundedCornerShape(25.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text(
            text = texto,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun CampoTextoSaludPlus(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    val azulOscuro = Color(0xFF0D1B2A)
    val azulPrincipal = Color(0xFF0066FF)
    val grisFondoCampo = Color(0xFFF8FAFC)
    val grisBorde = Color(0xFFE2E8F0)

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(label, color = Color(0xFF94A3B8)) },
            isError = isError,
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
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }
    }
}