package com.ponce.avisoscampus

import android.app.Application
import com.ponce.avisoscampus.notificacion.Notificador

/** Application: crea el canal de notificaciones una sola vez al iniciar. */
class AvisosApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Notificador.crearCanal(this)
    }
}
