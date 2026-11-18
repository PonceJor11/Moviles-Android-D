package com.tecsup.centrotareas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val EsquemaClaro = lightColorScheme(
    primary = AzulMarca,
    onPrimary = Color.White,
    primaryContainer = AzulSuave,
    onPrimaryContainer = AzulTinta,
    secondaryContainer = Color(0xFFE6EDFA),
    onSecondaryContainer = AzulTinta,
    background = FondoClaro,
    onBackground = Color(0xFF151B2B),
    surface = Color.White,
    onSurface = Color(0xFF151B2B),
    surfaceVariant = Color(0xFFE6EBF4),
    onSurfaceVariant = Color(0xFF4A5368),
    outline = Color(0xFF8089A0),
    outlineVariant = Color(0xFFD5DBE8),
    error = Color(0xFFB3261E),
    errorContainer = Color(0xFFFCE9E7),
    onErrorContainer = Color(0xFF9C2018)
)

private val EsquemaOscuro = darkColorScheme(
    primary = Color(0xFFA9C7FF),
    onPrimary = Color(0xFF00305F),
    primaryContainer = Color(0xFF1E4488),
    onPrimaryContainer = Color(0xFFD9E5FF),
    secondaryContainer = Color(0xFF263250),
    onSecondaryContainer = Color(0xFFD9E5FF),
    background = FondoOscuro,
    onBackground = Color(0xFFE3E7F2),
    surface = Color(0xFF171E2E),
    onSurface = Color(0xFFE3E7F2),
    surfaceVariant = Color(0xFF29324A),
    onSurfaceVariant = Color(0xFFB9C1D6),
    outline = Color(0xFF8790A8),
    outlineVariant = Color(0xFF333D57),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF3A1815),
    onErrorContainer = Color(0xFFFFB4AB)
)

// Jerarquía de formas: tarjetas más redondeadas que botones y recuadros.
private val Formas = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(20.dp)
)

@Composable
fun CentroTareasTheme(
    oscuro: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (oscuro) EsquemaOscuro else EsquemaClaro,
        typography = AppTypography,
        shapes = Formas,
        content = content
    )
}
