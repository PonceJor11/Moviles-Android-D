package com.ponce.avisoscampus.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val EsquemaMarron = lightColorScheme(
    primary = Marron,
    onPrimary = Color.White,
    primaryContainer = MarronContenedor,
    onPrimaryContainer = MarronTexto,
    secondary = MarronOscuro,
    onSecondary = Color.White,
    background = Crema,
    onBackground = MarronTexto,
    surface = CremaSuperficie,
    onSurface = MarronTexto,
    surfaceVariant = Arena,
    onSurfaceVariant = TextoSecundario,
    outline = Borde,
    outlineVariant = Borde,
    error = RojoError,
    errorContainer = RojoFondo,
    onErrorContainer = RojoError
)

private val Formas = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp)
)

/** Tema claro fijo (sin color dinámico) para mantener la identidad marrón. */
@Composable
fun AvisosCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaMarron,
        shapes = Formas,
        content = content
    )
}
