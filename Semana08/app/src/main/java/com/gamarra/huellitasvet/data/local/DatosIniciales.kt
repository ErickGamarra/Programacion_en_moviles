package com.gamarra.huellitasvet.data.local

import com.gamarra.huellitasvet.data.local.entity.Cita
import com.gamarra.huellitasvet.data.local.entity.Mascota
import com.gamarra.huellitasvet.data.local.entity.Servicio
import com.gamarra.huellitasvet.data.local.entity.Usuario
import com.gamarra.huellitasvet.model.Especie
import com.gamarra.huellitasvet.model.EstadoCita
import com.gamarra.huellitasvet.model.Rol
import com.gamarra.huellitasvet.model.Sexo
import java.time.LocalDate

object DatosIniciales {
    suspend fun sembrar(db: VetDatabase) {
        if (db.usuarioDao().contar() > 0) return

        db.servicioDao().insertarTodos(
            listOf(
                Servicio(nombre = "Consulta general", descripcion = "Revision completa de la mascota", duracionMin = 30, precio = 60.0, icono = "heart"),
                Servicio(nombre = "Vacunación", descripcion = "Aplicacion de vacunas al dia", duracionMin = 30, precio = 45.0, icono = "syringe"),
                Servicio(nombre = "Baño y corte", descripcion = "Higiene general y corte de pelo", duracionMin = 60, precio = 50.0, icono = "scissors"),
                Servicio(nombre = "Desparasitación", descripcion = "Tratamiento antiparasitario interno y externo", duracionMin = 15, precio = 25.0, activo = false, icono = "heart")
            )
        )

        db.usuarioDao().insertarTodos(
            listOf(
                Usuario(nombre = "Ana García", correo = "ana@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(nombre = "Luis Ramos", correo = "luis@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(nombre = "Dra. Rojas", correo = "vet@huellitas.test", clave = "admin", rol = Rol.ADMIN.name)
            )
        )

        val anio = LocalDate.now().year
        db.mascotaDao().insertarTodos(
            listOf(
                Mascota(duenoId = 1, nombre = "Luna", especie = Especie.GATO.name, raza = "Siamés", anioNacimiento = anio - 3, peso = 4.3, sexo = Sexo.HEMBRA.name),
                Mascota(duenoId = 1, nombre = "Max", especie = Especie.PERRO.name, raza = "Labrador", anioNacimiento = anio - 5, peso = 24.5, sexo = Sexo.MACHO.name),
                Mascota(duenoId = 2, nombre = "Rocky", especie = Especie.PERRO.name, raza = "Bulldog", anioNacimiento = anio - 6, peso = 18.0, sexo = Sexo.MACHO.name),
                Mascota(duenoId = 2, nombre = "Michi", especie = Especie.GATO.name, raza = "Mestizo", anioNacimiento = anio - 2, peso = 3.8, sexo = Sexo.HEMBRA.name)
            )
        )

        fun dia(n: Long) = LocalDate.now().plusDays(n).toString()
        fun cita(
            mascotaId: Int, servicioId: Int, precio: Double, dias: Long, hora: String,
            estado: EstadoCita, diagnostico: String? = null, tratamiento: String? = null, peso: Double? = null
        ) = Cita(
            mascotaId = mascotaId, servicioId = servicioId, fecha = dia(dias), hora = hora,
            nota = "", estado = estado.name, precio = precio,
            diagnostico = diagnostico, tratamiento = tratamiento, pesoRegistrado = peso
        )

        db.citaDao().insertarTodos(
            listOf(
                cita(1, 2, 45.0, -10, "10:30", EstadoCita.ATENDIDA, "Vacuna triple felina. Sin reacciones.", "Refuerzo en 12 meses", 4.3),
                cita(1, 1, 60.0, -60, "09:00", EstadoCita.ATENDIDA, "Control de rutina, sin hallazgos.", "Ninguno", 4.2),
                cita(1, 3, 50.0, -120, "11:00", EstadoCita.ATENDIDA, "Higiene general, pelaje sano.", "Cepillado semanal", 4.0),
                cita(2, 1, 60.0, -20, "15:00", EstadoCita.ATENDIDA, "Buen estado general.", "Suplemento vitaminico", 24.0),
                cita(3, 3, 50.0, -5, "09:30", EstadoCita.ATENDIDA, "Piel sana, sin parasitos.", "Shampoo hipoalergenico", 18.0),
                cita(2, 3, 50.0, 0, "09:00", EstadoCita.ATENDIDA, "Baño completo, piel sana.", "Shampoo hipoalergenico", 24.5),
                cita(1, 2, 45.0, 0, "10:30", EstadoCita.CONFIRMADA),
                cita(3, 1, 60.0, 0, "11:00", EstadoCita.PENDIENTE),
                cita(4, 1, 60.0, 0, "12:30", EstadoCita.PENDIENTE),
                cita(2, 1, 60.0, 2, "16:00", EstadoCita.PENDIENTE),
                cita(4, 2, 45.0, 4, "10:00", EstadoCita.PENDIENTE)
            )
        )
    }
}
