package com.tecsup.centrotareas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta base: azul de marca, superficies frías muy claras y estados con significado propio.
val AzulMarca = Color(0xFF1B5FC1)
val AzulProfundo = Color(0xFF14346B)
val AzulSuave = Color(0xFFDCE8FF)
val AzulTinta = Color(0xFF0B1F44)
val FondoClaro = Color(0xFFF3F6FB)
val FondoOscuro = Color(0xFF0E1320)

/** Colores de estado (éxito y error) que cambian según el modo claro u oscuro. */
object Estado {
    val barra: Color
        @Composable get() = if (isSystemInDarkTheme()) AzulProfundo else AzulMarca

    val exitoFondo: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF133324) else Color(0xFFE4F4EA)
    val exitoTexto: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF8FE0B0) else Color(0xFF14663A)

    val errorFondo: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF3A1815) else Color(0xFFFCE9E7)
    val errorTexto: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFFFFB4AB) else Color(0xFF9C2018)
}

/** Color propio de cada sección: Progreso (azul), Primos (violeta) y Cargas (ámbar). */
class Acento(
    val principal: Color,
    val sobrePrincipal: Color,
    val suave: Color,
    val sobreSuave: Color,
    val tinte: Color,
    val barra: Color
)

@Composable
fun acentoDe(indice: Int): Acento {
    val oscuro = isSystemInDarkTheme()
    return when (indice) {
        0 -> if (oscuro) Acento(
            Color(0xFFA9C7FF), Color(0xFF00305F), Color(0xFF1E4488), Color(0xFFD9E5FF),
            Color(0xFF0E1424), Color(0xFF14346B)
        ) else Acento(
            Color(0xFF1B5FC1), Color.White, Color(0xFFDCE8FF), Color(0xFF0B1F44),
            Color(0xFFEEF3FC), Color(0xFF1B5FC1)
        )
        1 -> if (oscuro) Acento(
            Color(0xFFCDBDFF), Color(0xFF2E1773), Color(0xFF4A30A0), Color(0xFFE9DEFF),
            Color(0xFF140F24), Color(0xFF2E1C66)
        ) else Acento(
            Color(0xFF6B3FD0), Color.White, Color(0xFFE9E1FC), Color(0xFF24104F),
            Color(0xFFF4F0FD), Color(0xFF6B3FD0)
        )
        else -> if (oscuro) Acento(
            Color(0xFFFFB874), Color(0xFF4A2800), Color(0xFF6B3A00), Color(0xFFFFDDBA),
            Color(0xFF1A130B), Color(0xFF5A2E05)
        ) else Acento(
            Color(0xFFB45309), Color.White, Color(0xFFFDE8CF), Color(0xFF4A2203),
            Color(0xFFFEF4E8), Color(0xFFB45309)
        )
    }
}
