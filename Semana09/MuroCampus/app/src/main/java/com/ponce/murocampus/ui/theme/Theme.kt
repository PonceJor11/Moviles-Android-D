package com.ponce.murocampus.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val EsquemaCalido = lightColorScheme(
    primary = Cognac,
    onPrimary = Color.White,
    primaryContainer = CognacClaro,
    onPrimaryContainer = CognacOscuro,
    secondary = Ocre,
    onSecondary = Color.White,
    secondaryContainer = OcreClaro,
    onSecondaryContainer = OcreOscuro,
    tertiary = Oliva,
    onTertiary = Color.White,
    tertiaryContainer = OlivaClaro,
    onTertiaryContainer = OlivaOscuro,
    background = Crema,
    onBackground = TextoMarron,
    surface = CremaSuperficie,
    onSurface = TextoMarron,
    surfaceVariant = CremaVariante,
    onSurfaceVariant = TextoSecundario,
    outline = Borde,
    outlineVariant = Borde,
    error = ErrorRojo,
    onError = Color.White,
    errorContainer = ErrorContenedor,
    onErrorContainer = Color(0xFF410002)
)

private val Formas = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp)
)

@Composable
fun MuroCampusTheme(content: @Composable () -> Unit) {
    // Sin dynamicColor: los colores no cambian segun el fondo de pantalla del celular
    MaterialTheme(
        colorScheme = EsquemaCalido,
        typography = Tipografia,
        shapes = Formas,
        content = content
    )
}
