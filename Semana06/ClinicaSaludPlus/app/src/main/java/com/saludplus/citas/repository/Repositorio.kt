package com.saludplus.citas.repository

import com.saludplus.citas.model.Cita
import com.saludplus.citas.model.Especialidad
import com.saludplus.citas.model.Medico
import com.saludplus.citas.model.Usuario

object Repositorio {

    // Colecciones

    val usuarios = mutableListOf<Usuario>()
    var usuarioActual: Usuario? = null
        private set

    val especialidades = listOf(
        Especialidad(1, "Medicina General", "Consulta y control general de salud", destacada = true),
        Especialidad(2, "Pediatria", "Atencion medica para ninos y adolescentes", destacada = true),
        Especialidad(3, "Cardiologia", "Salud del corazon y sistema circulatorio", destacada = true),
        Especialidad(4, "Dermatologia", "Cuidado de la piel, cabello y unas"),
        Especialidad(5, "Traumatologia", "Huesos, articulaciones y lesiones"),
        Especialidad(6, "Oftalmologia", "Salud visual y enfermedades de los ojos"),
        Especialidad(7, "Ginecologia", "Salud de la mujer y control prenatal"),
        Especialidad(8, "Neurologia", "Sistema nervioso, cefaleas y mareos")
    )

    val medicos = listOf(
        Medico(1, "Dra. Ana Torres", 1, 4.8, 12),
        Medico(2, "Dr. Luis Paredes", 1, 4.5, 8),
        Medico(3, "Dra. Claudia Rojas", 2, 4.9, 15),
        Medico(4, "Dr. Marco Quispe", 2, 4.4, 6),
        Medico(5, "Dr. Luis Ramirez", 3, 4.7, 20),
        Medico(6, "Dra. Valeria Soto", 3, 4.6, 10),
        Medico(7, "Dra. Elena Campos", 4, 4.8, 9),
        Medico(8, "Dr. Jorge Medina", 5, 4.3, 14),
        Medico(9, "Dra. Patricia Vega", 6, 4.9, 18),
        Medico(10, "Dra. Rosa Delgado", 7, 4.7, 16),
        Medico(11, "Dr. Andres Salazar", 8, 4.5, 11),
        Medico(12, "Dra. Mariela Cruz", 8, 4.6, 7)
    )

    val citas = mutableListOf<Cita>()

    val horariosBase = listOf(
        "08:00", "09:00", "10:00", "11:00",
        "14:00", "15:00", "16:00", "17:00"
    )

    // Usuarios y sesion

    fun registrarUsuario(nombre: String, correo: String, telefono: String, contrasena: String): Boolean {
        val correoLimpio = correo.trim()
        val yaExiste = usuarios.any { it.correo.equals(correoLimpio, ignoreCase = true) }
        if (yaExiste) return false

        val nuevo = Usuario(
            id = (usuarios.maxOfOrNull { it.id } ?: 0) + 1,
            nombre = nombre.trim(),
            correo = correoLimpio,
            telefono = telefono.trim(),
            contrasena = contrasena
        )
        usuarios.add(nuevo)
        usuarioActual = nuevo
        return true
    }

    fun iniciarSesion(correo: String, contrasena: String): Boolean {
        val usuario = usuarios.find {
            it.correo.equals(correo.trim(), ignoreCase = true) && it.contrasena == contrasena
        } ?: return false

        usuarioActual = usuario
        return true
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    // Especialidades

    fun buscarEspecialidades(consulta: String): List<Especialidad> {
        return especialidades.filter { it.nombre.contains(consulta.trim(), ignoreCase = true) }
    }

    fun especialidadesDestacadas(): List<Especialidad> {
        return especialidades.filter { it.destacada }.take(4)
    }

    fun obtenerEspecialidad(id: Int): Especialidad? {
        return especialidades.find { it.id == id }
    }

    // Medicos

    fun obtenerMedico(id: Int): Medico? {
        return medicos.find { it.id == id }
    }

    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> {
        return medicos
            .filter { it.especialidadId == especialidadId }
            .sortedByDescending { it.calificacion }
    }

    fun buscarMedicos(especialidadId: Int, consulta: String): List<Medico> {
        return medicosPorEspecialidad(especialidadId)
            .filter { it.nombre.contains(consulta.trim(), ignoreCase = true) }
    }

    // Citas

    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        TODO("Implementar horariosDisponibles")
    }

    fun obtenerCita(id: Int): Cita? {
        TODO("Implementar obtenerCita")
    }

    fun agendarCita(medicoId: Int, fecha: String, hora: String): Boolean {
        TODO("Implementar agendarCita")
    }

    fun citasDelUsuario(): List<Cita> {
        TODO("Implementar citasDelUsuario")
    }

    fun cancelarCita(citaId: Int): Boolean {
        TODO("Implementar cancelarCita")
    }
}
