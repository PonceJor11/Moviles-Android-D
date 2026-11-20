package com.ponce.campusgps.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ponce.campusgps.model.TipoError
import com.ponce.campusgps.model.UbicacionUi

/** Recuadro de error con la acción que corresponde a cada tipo de problema. */
@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val colores = MaterialTheme.colorScheme
    val coloresBoton = ButtonDefaults.buttonColors(
        containerColor = colores.error,
        contentColor = colores.onError
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = colores.errorContainer
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = error.mensaje,
                color = colores.onErrorContainer,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            when (error.tipo) {
                TipoError.SIN_PERMISO -> {
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { abrirAjustes(contexto) }, colors = coloresBoton) {
                        Text("Abrir ajustes")
                    }
                }
                TipoError.GPS_APAGADO, TipoError.OTRO -> {
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = onReintentar, colors = coloresBoton) {
                        Text("Reintentar")
                    }
                }
                TipoError.SIN_PLAY_SERVICES -> Unit // sin solución posible desde la app
            }
        }
    }
}

private fun abrirAjustes(contexto: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", contexto.packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    contexto.startActivity(intent)
}
