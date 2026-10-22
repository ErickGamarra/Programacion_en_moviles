package com.gamarra.huellitasvet

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.gamarra.huellitasvet.data.local.VetDatabase
import com.gamarra.huellitasvet.data.local.entity.Vacuna
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

// Simula una base de la versión 1 con datos y comprueba que la migración 1 -> 2 agrega la tabla vacunas sin perderlos
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], application = Application::class)
class MigracionVacunasTest {
    private val esquemaV1 = listOf(
        "CREATE TABLE IF NOT EXISTS `servicios` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT NOT NULL, `descripcion` TEXT NOT NULL, `duracionMin` INTEGER NOT NULL, `precio` REAL NOT NULL, `activo` INTEGER NOT NULL, `icono` TEXT NOT NULL)",
        "CREATE TABLE IF NOT EXISTS `usuarios` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT NOT NULL, `correo` TEXT NOT NULL, `clave` TEXT NOT NULL, `rol` TEXT NOT NULL)",
        "CREATE TABLE IF NOT EXISTS `mascotas` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `duenoId` INTEGER NOT NULL, `nombre` TEXT NOT NULL, `especie` TEXT NOT NULL, `raza` TEXT NOT NULL, `anioNacimiento` INTEGER NOT NULL, `peso` REAL NOT NULL, `sexo` TEXT NOT NULL, FOREIGN KEY(`duenoId`) REFERENCES `usuarios`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )",
        "CREATE TABLE IF NOT EXISTS `citas` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `mascotaId` INTEGER NOT NULL, `servicioId` INTEGER NOT NULL, `fecha` TEXT NOT NULL, `hora` TEXT NOT NULL, `nota` TEXT NOT NULL, `estado` TEXT NOT NULL, `precio` REAL NOT NULL, `diagnostico` TEXT, `tratamiento` TEXT, `pesoRegistrado` REAL, `fechaCreacion` INTEGER NOT NULL, FOREIGN KEY(`mascotaId`) REFERENCES `mascotas`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`servicioId`) REFERENCES `servicios`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )",
        "CREATE INDEX IF NOT EXISTS `index_mascotas_duenoId` ON `mascotas` (`duenoId`)",
        "CREATE INDEX IF NOT EXISTS `index_citas_mascotaId` ON `citas` (`mascotaId`)",
        "CREATE INDEX IF NOT EXISTS `index_citas_servicioId` ON `citas` (`servicioId`)",
        "CREATE INDEX IF NOT EXISTS `index_citas_fecha_hora` ON `citas` (`fecha`, `hora`)"
    )

    @Test
    fun migraDeVersion1A2SinPerderDatos() = runBlocking {
        val contexto = ApplicationProvider.getApplicationContext<Context>()
        val nombre = "migracion.db"
        contexto.deleteDatabase(nombre)

        SQLiteDatabase.openOrCreateDatabase(contexto.getDatabasePath(nombre), null).use { v1 ->
            esquemaV1.forEach(v1::execSQL)
            v1.execSQL("INSERT INTO usuarios (nombre, correo, clave, rol) VALUES ('Ana García', 'ana@correo.test', '1234', 'CLIENTE')")
            v1.execSQL("INSERT INTO mascotas (duenoId, nombre, especie, raza, anioNacimiento, peso, sexo) VALUES (1, 'Luna', 'GATO', 'Siamés', 2023, 4.3, 'HEMBRA')")
            v1.version = 1
        }

        val db = Room.databaseBuilder(contexto, VetDatabase::class.java, nombre)
            .addMigrations(VetDatabase.MIGRACION_1_2)
            .build()

        assertEquals("Luna", db.mascotaDao().observarUna(1).first()?.nombre)
        db.vacunaDao().insertar(Vacuna(mascotaId = 1, nombre = "Triple felina", fecha = "2026-10-11", proximaDosis = "2027-10-11"))
        assertEquals(listOf("Triple felina"), db.vacunaDao().observarPorMascota(1).first().map { it.nombre })
        db.close()
    }
}
