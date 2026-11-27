package com.ponce.avisoscampus.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/** Escucha el anuncio del sistema. onReceive es corto: solo avisa con el estado. */
class ModoAvionReceiver(
    private val alCambiar: (Boolean) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val recibido = intent ?: return
        if (recibido.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            alCambiar(recibido.getBooleanExtra("state", false))
        }
    }
}
