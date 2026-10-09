package com.saludplus.citas.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.saludplus.citas.navigation.Rutas

@Composable
fun BottomNavBar(
    rutaActual: String?,
    onNavegar: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = rutaActual == Rutas.HOME,
            onClick = { onNavegar(Rutas.HOME) },
            icon = { Text("🏠") },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = rutaActual == Rutas.MIS_CITAS,
            onClick = { onNavegar(Rutas.MIS_CITAS) },
            icon = { Text("📅") },
            label = { Text("Citas") }
        )
        NavigationBarItem(
            selected = rutaActual == Rutas.RESULTADOS,
            onClick = { onNavegar(Rutas.RESULTADOS) },
            icon = { Text("📋") },
            label = { Text("Resultados") }
        )
        NavigationBarItem(
            selected = rutaActual == Rutas.PERFIL,
            onClick = { onNavegar(Rutas.PERFIL) },
            icon = { Text("👤") },
            label = { Text("Perfil") }
        )
    }
}