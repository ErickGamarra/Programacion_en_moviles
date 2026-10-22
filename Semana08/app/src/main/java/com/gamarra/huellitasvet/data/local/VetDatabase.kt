package com.gamarra.huellitasvet.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gamarra.huellitasvet.data.local.dao.CitaDao
import com.gamarra.huellitasvet.data.local.dao.MascotaDao
import com.gamarra.huellitasvet.data.local.dao.ServicioDao
import com.gamarra.huellitasvet.data.local.dao.UsuarioDao
import com.gamarra.huellitasvet.data.local.dao.VacunaDao
import com.gamarra.huellitasvet.data.local.entity.Cita
import com.gamarra.huellitasvet.data.local.entity.Mascota
import com.gamarra.huellitasvet.data.local.entity.Servicio
import com.gamarra.huellitasvet.data.local.entity.Usuario
import com.gamarra.huellitasvet.data.local.entity.Vacuna

@Database(
    entities = [Servicio::class, Usuario::class, Mascota::class, Cita::class, Vacuna::class],
    version = 2,
    exportSchema = false
)
abstract class VetDatabase : RoomDatabase() {
    abstract fun servicioDao(): ServicioDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun mascotaDao(): MascotaDao
    abstract fun citaDao(): CitaDao
    abstract fun vacunaDao(): VacunaDao

    companion object {
        @Volatile
        private var INSTANCE: VetDatabase? = null

        val MIGRACION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `vacunas` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`mascotaId` INTEGER NOT NULL, `nombre` TEXT NOT NULL, `fecha` TEXT NOT NULL, `proximaDosis` TEXT, " +
                        "FOREIGN KEY(`mascotaId`) REFERENCES `mascotas`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)"
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_vacunas_mascotaId` ON `vacunas` (`mascotaId`)")
            }
        }

        fun obtener(context: Context): VetDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                VetDatabase::class.java,
                "huellitasvet.db"
            ).addMigrations(MIGRACION_1_2).build().also { INSTANCE = it }
        }
    }
}
