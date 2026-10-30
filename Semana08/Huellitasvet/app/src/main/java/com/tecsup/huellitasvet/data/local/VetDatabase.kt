package com.tecsup.huellitasvet.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tecsup.huellitasvet.data.local.dao.CitaDao
import com.tecsup.huellitasvet.data.local.dao.MascotaDao
import com.tecsup.huellitasvet.data.local.dao.ServicioDao
import com.tecsup.huellitasvet.data.local.dao.UsuarioDao
import com.tecsup.huellitasvet.data.local.entity.Cita
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.local.entity.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Servicio::class, Usuario::class, Mascota::class, Cita::class],
    version = 1,
    exportSchema = false
)
abstract class VetDatabase : RoomDatabase() {

    abstract fun servicioDao(): ServicioDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun mascotaDao(): MascotaDao
    abstract fun citaDao(): CitaDao

    companion object {
        @Volatile
        private var INSTANCE: VetDatabase? = null

        fun obtener(context: Context): VetDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VetDatabase::class.java,
                    "huellitasvet.db"
                )
                    .addCallback(VetDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class VetDatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    val database = obtener(context)
                    // Llamada al seeder de datos iniciales
                    DatosIniciales.sembrar(database)
                }
            }
        }
    }
}