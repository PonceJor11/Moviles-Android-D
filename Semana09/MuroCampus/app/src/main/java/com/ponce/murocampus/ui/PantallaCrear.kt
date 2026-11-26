package com.ponce.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ponce.murocampus.viewmodel.PostsViewModel
import com.ponce.murocampus.viewmodel.UiState

@Composable
fun PantallaCrear(vm: PostsViewModel, onVolver: () -> Unit) {
    val estado by vm.publicacion.collectAsStateWithLifecycle()
    var titulo by rememberSaveable { mutableStateOf("") }
    var contenido by rememberSaveable { mutableStateOf("") }

    // Al publicar con exito se limpia el formulario; con error se conserva lo escrito
    LaunchedEffect(estado) {
        if (estado is UiState.Exito) {
            titulo = ""
            contenido = ""
        }
    }

    val enviando = estado is UiState.Cargando
    val puedePublicar = titulo.isNotBlank() && contenido.isNotBlank() && !enviando

    val coloresCampo = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        cursorColor = MaterialTheme.colorScheme.primary
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { BarraMuro(titulo = "Nueva publicación", onAtras = onVolver) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    titulo = it
                    if (estado is UiState.Exito) vm.limpiarPublicacion()
                },
                label = { Text("Título") },
                singleLine = true,
                enabled = !enviando,
                shape = MaterialTheme.shapes.medium,
                colors = coloresCampo,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = contenido,
                onValueChange = {
                    contenido = it
                    if (estado is UiState.Exito) vm.limpiarPublicacion()
                },
                label = { Text("Contenido") },
                minLines = 5,
                enabled = !enviando,
                shape = MaterialTheme.shapes.medium,
                colors = coloresCampo,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { vm.publicar(titulo.trim(), contenido.trim()) },
                enabled = puedePublicar,
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
            ) {
                if (enviando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Publicar")
                }
            }

            when (val e = estado) {
                is UiState.Exito -> Aviso(
                    texto = "Publicado con id ${e.datos.id}",
                    fondo = MaterialTheme.colorScheme.tertiaryContainer,
                    contenido = MaterialTheme.colorScheme.onTertiaryContainer
                )
                is UiState.Error -> Aviso(
                    texto = e.mensaje,
                    fondo = MaterialTheme.colorScheme.errorContainer,
                    contenido = MaterialTheme.colorScheme.onErrorContainer
                )
                else -> Unit
            }
        }
    }
}

@Composable
private fun Aviso(
    texto: String,
    fondo: androidx.compose.ui.graphics.Color,
    contenido: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = fondo, contentColor = contenido)
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}
