package com.ponce.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.ponce.campusgps.model.Posicion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(private val contexto: Context) : UbicacionRepository {

    private val cliente = LocationServices.getFusedLocationProviderClient(contexto)

    @SuppressLint("MissingPermission") // el permiso se solicita en la capa de UI antes de llamar
    override suspend fun obtenerPosicion(): Result<Posicion> {
        if (!ubicacionActivada()) {
            return Result.failure(UbicacionNoDisponibleException("Activa el GPS e intenta de nuevo"))
        }
        val fuente = CancellationTokenSource()
        return try {
            val ubicacion = cliente
                .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, fuente.token)
                .await()
            if (ubicacion != null) {
                Result.success(Posicion(ubicacion.latitude, ubicacion.longitude))
            } else {
                Result.failure(UbicacionNoDisponibleException())
            }
        } catch (e: CancellationException) {
            fuente.cancel()
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun playServicesDisponible(): Boolean =
        GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(contexto) == ConnectionResult.SUCCESS

    private fun ubicacionActivada(): Boolean {
        val manager = contexto.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(manager)
    }
}
