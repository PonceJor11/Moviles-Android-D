package com.tecsup.centrotareas.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.centrotareas.model.CargaUi
import com.tecsup.centrotareas.model.PrimosUi
import com.tecsup.centrotareas.ui.theme.Estado
import com.tecsup.centrotareas.ui.theme.acentoDe
import com.tecsup.centrotareas.viewmodel.TareasViewModel
import java.util.Locale

private fun miles(n: Int): String = String.format(Locale.getDefault(), "%,d", n)

private class Seccion(val etiqueta: String, val icono: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    // Ambos estados sobreviven a la rotación. El contador se comparte entre secciones.
    var toques by rememberSaveable { mutableIntStateOf(0) }
    var actual by rememberSaveable { mutableIntStateOf(0) }

    val secciones = listOf(
        Seccion("Progreso", Icons.Filled.Build),
        Seccion("Primos", Icons.Filled.Search),
        Seccion("Cargas", Icons.Filled.Refresh)
    )
    val acento = acentoDe(actual)
    val barra by animateColorAsState(acento.barra, tween(300), label = "barra")
    val tinte by animateColorAsState(acento.tinte, tween(300), label = "tinte")

    // Cada sección repinta el tema con su propio color: botones, barras e interruptores lo heredan.
    val esquema = MaterialTheme.colorScheme.copy(
        primary = acento.principal,
        onPrimary = acento.sobrePrincipal,
        primaryContainer = acento.suave,
        onPrimaryContainer = acento.sobreSuave,
        secondaryContainer = acento.suave,
        onSecondaryContainer = acento.sobreSuave,
        background = tinte
    )

    MaterialTheme(
        colorScheme = esquema,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "CentroTareas",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "Que la pantalla nunca se congele",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.78f)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = barra,
                        titleContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    secciones.forEachIndexed { i, seccion ->
                        val a = acentoDe(i)
                        NavigationBarItem(
                            selected = actual == i,
                            onClick = { actual = i },
                            icon = { Icon(seccion.icono, contentDescription = null) },
                            label = { Text(seccion.etiqueta) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = a.sobreSuave,
                                selectedTextColor = a.principal,
                                indicatorColor = a.suave
                            )
                        )
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PruebaDeVida(toques = toques, onToque = { toques++ })
                when (actual) {
                    0 -> TarjetaProgreso(vm)
                    1 -> TarjetaPrimos(vm)
                    else -> TarjetaCargas(vm)
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun PruebaDeVida(toques: Int, onToque: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Prueba de vida", style = MaterialTheme.typography.titleSmall)
                Text(
                    text = "Si deja de sumar, la pantalla está congelada.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            FilledTonalButton(onClick = onToque) {
                Text("Tócame: $toques")
            }
        }
    }
}

// ---------------------------------------------------------------- Tarjeta 1

@Composable
private fun TarjetaProgreso(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()
    val animado by animateFloatAsState(
        targetValue = progreso,
        animationSpec = tween(durationMillis = 150),
        label = "progreso"
    )

    Tarjeta(
        titulo = "Tarea con progreso",
        subtitulo = "Trabaja en segundo plano y se puede cancelar",
        icono = Icons.Filled.Build
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = vm::tareaLarga,
                enabled = !corriendo,
                modifier = Modifier.weight(1f)
            ) {
                Text("Iniciar")
            }
            OutlinedButton(
                onClick = vm::cancelar,
                enabled = corriendo,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.size(6.dp))
                Text("Cancelar")
            }
        }

        LinearProgressIndicator(
            progress = { animado },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(CircleShape),
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            EtiquetaEstado(mensaje)
            Text(
                text = "${(progreso * 100).toInt()} %",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// ---------------------------------------------------------------- Tarjeta 2

@Composable
private fun TarjetaPrimos(vm: TareasViewModel) {
    val estado by vm.primos.collectAsState()
    var texto by rememberSaveable { mutableStateOf("500000") }
    var ultimoN by rememberSaveable { mutableIntStateOf(0) }

    val numero = texto.toIntOrNull()
    val valido = numero != null && numero >= 2
    val calculando = estado is PrimosUi.Calculando

    Tarjeta(
        titulo = "Contar números primos",
        subtitulo = "Cálculo pesado en Dispatchers.Default",
        icono = Icons.Filled.Search
    ) {
        OutlinedTextField(
            value = texto,
            onValueChange = { nuevo ->
                // Ignora lo que no sea número.
                if (nuevo.length <= 9 && nuevo.all { it.isDigit() }) texto = nuevo
            },
            label = { Text("Contar primos hasta") },
            singleLine = true,
            enabled = !calculando,
            isError = texto.isNotEmpty() && !valido,
            supportingText = {
                if (texto.isNotEmpty() && !valido) Text("Escribe un número mayor o igual a 2")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {
                    if (numero != null && valido) {
                        ultimoN = numero
                        vm.contar(numero)
                    }
                },
                enabled = valido && !calculando,
                modifier = Modifier.weight(1f)
            ) {
                Text("Contar")
            }
            OutlinedButton(
                onClick = vm::cancelarPrimos,
                enabled = calculando,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.size(6.dp))
                Text("Cancelar")
            }
        }

        when (val e = estado) {
            PrimosUi.Inicial -> Text(
                text = "Pulsa Contar y sigue tocando el botón de arriba: la pantalla no se bloquea.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            PrimosUi.Calculando -> Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 3.dp)
                Text(
                    text = "Calculando…",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            is PrimosUi.Listo -> Recuadro(
                fondo = Estado.exitoFondo,
                contenido = Estado.exitoTexto,
                icono = Icons.Filled.CheckCircle,
                titulo = "${miles(e.cantidad)} primos hasta ${miles(ultimoN)}"
            ) {
                Text("Tiempo: ${miles(e.milisegundos.toInt())} ms", style = MaterialTheme.typography.bodyMedium)
            }

            PrimosUi.Cancelado -> Recuadro(
                fondo = MaterialTheme.colorScheme.surfaceVariant,
                contenido = MaterialTheme.colorScheme.onSurfaceVariant,
                icono = Icons.Filled.Close,
                titulo = "Cálculo cancelado"
            ) {
                Text("Puedes iniciar otro cuando quieras.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

// ---------------------------------------------------------------- Tarjeta 3

@Composable
private fun TarjetaCargas(vm: TareasViewModel) {
    val estado by vm.carga.collectAsState()
    val simularError by vm.simularError.collectAsState()
    val cargando = estado is CargaUi.Cargando

    Tarjeta(
        titulo = "Dos cargas",
        subtitulo = "Perfil (2 s) y pedidos (3 s): una por una contra a la vez",
        icono = Icons.Filled.Refresh
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Simular error", style = MaterialTheme.typography.bodyMedium)
            Switch(checked = simularError, onCheckedChange = vm::cambiarSimularError)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = vm::cargarUnaPorUna,
                enabled = !cargando,
                modifier = Modifier.weight(1f)
            ) {
                Text("Una por una")
            }
            Button(
                onClick = vm::cargarALaVez,
                enabled = !cargando,
                modifier = Modifier.weight(1f)
            ) {
                Text("A la vez")
            }
        }

        when (val e = estado) {
            CargaUi.Inicial -> Text(
                text = "Compara los tiempos: una por una ronda los 5 s y a la vez, los 3 s.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            CargaUi.Cargando -> Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 3.dp)
                Text(
                    text = "Cargando datos…",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            is CargaUi.Exito -> Recuadro(
                fondo = Estado.exitoFondo,
                contenido = Estado.exitoTexto,
                icono = Icons.Filled.CheckCircle,
                titulo = "Carga completa"
            ) {
                Dato("Modo", e.modo)
                Dato("Perfil", e.perfil)
                Dato("Pedidos", e.pedidos.toString())
                Dato("Tiempo", "${miles(e.milisegundos.toInt())} ms")
            }

            is CargaUi.Error -> Recuadro(
                fondo = Estado.errorFondo,
                contenido = Estado.errorTexto,
                icono = Icons.Filled.Warning,
                titulo = "Algo salió mal"
            ) {
                Text(e.mensaje, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = vm::cargarALaVez,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text("Reintentar")
                }
            }
        }
    }
}

// ---------------------------------------------------------------- Piezas reutilizables

@Composable
private fun Tarjeta(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icono,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Column {
                    Text(
                        text = titulo,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = subtitulo,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            contenido()
        }
    }
}

@Composable
private fun EtiquetaEstado(mensaje: String) {
    val (fondo, texto) = when (mensaje) {
        "Terminó" -> Estado.exitoFondo to Estado.exitoTexto
        "Cancelado" -> Estado.errorFondo to Estado.errorTexto
        "Trabajando..." -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }
    Surface(color = fondo, contentColor = texto, shape = CircleShape) {
        Text(
            text = mensaje,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun Recuadro(
    fondo: Color,
    contenido: Color,
    icono: ImageVector,
    titulo: String,
    cuerpo: @Composable ColumnScope.() -> Unit
) {
    Surface(
        color = fondo,
        contentColor = contenido,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(imageVector = icono, contentDescription = null, tint = contenido)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                cuerpo()
            }
        }
    }
}

@Composable
private fun Dato(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(etiqueta, style = MaterialTheme.typography.bodyMedium)
        Text(valor, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

