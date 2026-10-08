package com.saludplus.citas.repository

import com.saludplus.citas.model.Cita
import com.saludplus.citas.model.Especialidad
import com.saludplus.citas.model.Medico
import com.saludplus.citas.model.Usuario

/**
 * Fuente de datos en memoria (sin base de datos). Es un object para que todas las pantallas
 * compartan las mismas colecciones; los datos se pierden al cerrar la app.
 *
 * Completa cada TODO usando operaciones de colecciones (filter, find, any, add, map, sortedBy...).
 * No cambies nombres ni parametros de las funciones.
 */
object Repositorio {

    // ---------- Colecciones ----------

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

    // ---------- Usuarios y sesion ----------

    /** Registra un usuario nuevo. Devuelve false si ya existe un usuario con ese correo. */
    fun registrarUsuario(nombre: String, correo: String, telefono: String, contrasena: String): Boolean {
        // TODO: usuarios.any { ... } para validar que el correo no exista; si no existe, usuarios.add(...)
        //       y dejar el nuevo usuario como usuarioActual. Devolver true/false segun el resultado.
        TODO("Implementar registrarUsuario")
    }

    /** Inicia sesion si el correo y la contrasena coinciden con un usuario registrado. */
    fun iniciarSesion(correo: String, contrasena: String): Boolean {
        // TODO: usuarios.find { ... }; si existe, asignarlo a usuarioActual y devolver true.
        TODO("Implementar iniciarSesion")
    }

    fun cerrarSesion() {
        // TODO: dejar usuarioActual en null.
    }

    // ---------- Especialidades ----------

    /** Busqueda en tiempo real: devuelve todas las especialidades si consulta esta vacia. */
    fun buscarEspecialidades(consulta: String): List<Especialidad> {
        // TODO: especialidades.filter { it.nombre.contains(consulta, ignoreCase = true) }
        TODO("Implementar buscarEspecialidades")
    }

    /** Especialidades para el LazyRow de Inicio. */
    fun especialidadesDestacadas(): List<Especialidad> {
        // TODO: especialidades.filter { it.destacada }.take(...)
        TODO("Implementar especialidadesDestacadas")
    }

    fun obtenerEspecialidad(id: Int): Especialidad? {
        // TODO: especialidades.find { it.id == id }
        TODO("Implementar obtenerEspecialidad")
    }

    // ---------- Medicos ----------

    fun obtenerMedico(id: Int): Medico? {
        // TODO: medicos.find { it.id == id }
        TODO("Implementar obtenerMedico")
    }

    /** Medicos de una especialidad, ordenados de mayor a menor calificacion. */
    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> {
        // TODO: medicos.filter { ... }.sortedByDescending { it.calificacion }
        TODO("Implementar medicosPorEspecialidad")
    }

    /** Igual que medicosPorEspecialidad pero filtrando ademas por el nombre del medico. */
    fun buscarMedicos(especialidadId: Int, consulta: String): List<Medico> {
        // TODO: filtrar por especialidad y por nombre (contains, ignoreCase) y ordenar por calificacion.
        TODO("Implementar buscarMedicos")
    }

    // ---------- Citas ----------

    /** Horarios libres de un medico en una fecha: horariosBase menos los ya reservados. */
    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        // TODO: citas.filter { medico y fecha }.map { it.hora } -> reservados;
        //       horariosBase.filter { it !in reservados }
        TODO("Implementar horariosDisponibles")
    }

    fun obtenerCita(id: Int): Cita? {
        // TODO: citas.find { it.id == id }
        TODO("Implementar obtenerCita")
    }

    /**
     * Agenda una cita para el usuarioActual. Devuelve false si no hay sesion o si el horario
     * ya esta reservado para ese medico y fecha.
     */
    fun agendarCita(medicoId: Int, fecha: String, hora: String): Boolean {
        // TODO: validar usuarioActual y que el horario este libre (citas.any { ... });
        //       crear la Cita (id nuevo, especialidadId tomado del medico) y citas.add(...)
        TODO("Implementar agendarCita")
    }

    /** Citas del usuarioActual ordenadas por fecha y hora. */
    fun citasDelUsuario(): List<Cita> {
        // TODO: citas.filter { it.usuarioId == usuarioActual?.id }.sortedWith(compareBy({ it.fecha }, { it.hora }))
        TODO("Implementar citasDelUsuario")
    }

    /** Reto extra (pantalla Detalle de cita). */
    fun cancelarCita(citaId: Int): Boolean {
        // TODO: citas.removeIf { it.id == citaId } (o removeAll { ... })
        TODO("Implementar cancelarCita")
    }
}
