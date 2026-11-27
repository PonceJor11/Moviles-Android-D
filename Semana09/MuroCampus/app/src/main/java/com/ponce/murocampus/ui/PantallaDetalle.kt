package com.ponce.murocampus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ponce.murocampus.model.Comentario
import com.ponce.murocampus.viewmodel.PostsViewModel
import com.ponce.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(vm: PostsViewModel, id: Int, onVolver: () -> Unit) {
    val estado by vm.detalle.collectAsStateWithLifecycle()

    LaunchedEffect(id) { vm.asegurarDetalle(id) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { BarraMuro(titulo = "Publicación", onAtras = onVolver) }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            when (val e = estado) {
                UiState.Cargando -> Cargando("Cargando publicación…")
                is UiState.Error -> ErrorConReintento(e.mensaje, onReintentar = vm::reintentarDetalle)
                is UiState.Exito -> {
                    val post = e.datos.post
                    val comentarios = e.datos.comentarios
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Column {
                                EtiquetaUsuario(post.userId)
                                Spacer(Modifier.height(12.dp))
                                Text(post.title, style = MaterialTheme.typography.headlineSmall)
                                Spacer(Modifier.height(12.dp))
                                Text(post.body, style = MaterialTheme.typography.bodyLarge)
                                Spacer(Modifier.height(20.dp))
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    text = "Comentarios (${comentarios.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        items(comentarios, key = { it.id }) { TarjetaComentario(it) }
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaComentario(c: Comentario) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                text = c.email,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = c.body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
