package com.ejemplo.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PerfilScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Mi perfil", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Diego Ramos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(text = "Plan Premium")

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Card {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "14", style = MaterialTheme.typography.titleLarge)
                    Text(text = "Clases")
                }
            }
            Card {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "3", style = MaterialTheme.typography.titleLarge)
                    Text(text = "Rachas")
                }
            }
        }
    }
}