package com.gamarra.clientesaludplus.model

data class Specialty(
    val id: String,
    val name: String
)

data class Doctor(
    val id: String,
    val name: String,
    val specialty: String,
    val rating: Double,
    val reviewsCount: Int,
    val experienceYears: Int,
    val bio: String
)

enum class AppointmentStatus {
    CONFIRMADA,
    COMPLETADA
}

data class Appointment(
    val id: String,
    val doctorName: String,
    val specialty: String,
    val date: String,
    val time: String,
    val status: AppointmentStatus
)