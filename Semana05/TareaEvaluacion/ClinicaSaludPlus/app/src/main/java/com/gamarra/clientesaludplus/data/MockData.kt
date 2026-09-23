package com.gamarra.clientesaludplus.data

import com.gamarra.clientesaludplus.model.Appointment
import com.gamarra.clientesaludplus.model.AppointmentStatus
import com.gamarra.clientesaludplus.model.Doctor
import com.gamarra.clientesaludplus.model.Specialty

val mockSpecialties = listOf(
    Specialty("1", "Cardiología"),
    Specialty("2", "Pediatría"),
    Specialty("3", "Dermatología")
)

val mockDoctors = listOf(
    Doctor(
        id = "doc_1",
        name = "Dra. Ana Torres",
        specialty = "Cardióloga",
        rating = 4.9,
        reviewsCount = 128,
        experienceYears = 12,
        bio = "Especialista en arritmias e hipertensión, formación en la Clínica Mayo."
    ),
    Doctor(
        id = "doc_2",
        name = "Dr. Luis Vega",
        specialty = "Pediatra",
        rating = 4.7,
        reviewsCount = 95,
        experienceYears = 8,
        bio = "Atención integral pediátrica, control de crecimiento y desarrollo infantil."
    ),
    Doctor(
        id = "doc_3",
        name = "Dra. Rosa Díaz",
        specialty = "Dermatóloga",
        rating = 4.8,
        reviewsCount = 110,
        experienceYears = 10,
        bio = "Dermatología clínica y estética, tratamiento avanzado del acné."
    )
)

val mockInitialAppointments = listOf(
    Appointment(
        id = "app_1",
        doctorName = "Dra. Ana Torres",
        specialty = "Cardióloga",
        date = "Viernes 27",
        time = "10:30 am",
        status = AppointmentStatus.CONFIRMADA
    ),
    Appointment(
        id = "app_2",
        doctorName = "Dr. Luis Vega",
        specialty = "Pediatra",
        date = "Miércoles 15",
        time = "3:00 pm",
        status = AppointmentStatus.COMPLETADA
    )
)