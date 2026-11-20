package com.ponce.campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Controles que flotan sobre el mapa. Solo dibujan y avisan al ViewModel mediante callbacks. */
@Composable
fun FiltrosMapa(
    categorias: List<String>,
    categoriaActiva: String?,
    onCategoria: (String?) -> Unit,
    radios: List<Int>,
    radioActivo: Int?,
    onRadio: (Int?) -> Unit,
    mostrarRadios: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        LazyRow(
            contentPadding = PaddingValues(start = 12.dp, end = 60.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                ChipFiltro(
                    texto = "Todas",
                    seleccionado = categoriaActiva == null,
                    onClick = { onCategoria(null) }
                )
            }
            items(categorias) { categoria ->
                ChipFiltro(
                    texto = categoria,
                    seleccionado = categoria == categoriaActiva,
                    onClick = { onCategoria(categoria) }
                )
            }
        }

        // El radio solo tiene sentido cuando ya se conoce la posición del usuario.
        if (mostrarRadios) {
            LazyRow(
                contentPadding = PaddingValues(start = 12.dp, end = 60.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(radios) { radio ->
                    ChipFiltro(
                        texto = "Radio $radio m",
                        seleccionado = radio == radioActivo,
                        onClick = { onRadio(radio) }
                    )
                }
            }
        }
    }
}

@Composable
internal fun ChipFiltro(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = { Text(texto) },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = colores.surface,
            labelColor = colores.onSurface,
            selectedContainerColor = colores.primaryContainer,
            selectedLabelColor = colores.onPrimaryContainer
        )
    )
}
