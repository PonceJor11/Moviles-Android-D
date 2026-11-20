package com.ponce.campusgps.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.almacenPreferencias: DataStore<Preferences> by preferencesDataStore(
    name = "campusgps_preferencias"
)

class PreferenciasRepositoryDataStore(contexto: Context) : PreferenciasRepository {

    private val almacen = contexto.applicationContext.almacenPreferencias

    override val ultimoLugarId: Flow<Int?> = almacen.data
        .catch { error ->
            // Un archivo ilegible equivale a "sin preferencias guardadas".
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferencias -> preferencias[CLAVE_ULTIMO_LUGAR] }

    override suspend fun guardarUltimoLugar(id: Int) {
        almacen.edit { preferencias -> preferencias[CLAVE_ULTIMO_LUGAR] = id }
    }

    private companion object {
        val CLAVE_ULTIMO_LUGAR = intPreferencesKey("ultimo_lugar_id")
    }
}
