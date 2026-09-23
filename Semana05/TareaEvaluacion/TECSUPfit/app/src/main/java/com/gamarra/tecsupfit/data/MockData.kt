package com.gamarra.tecsupfit.data

import com.gamarra.tecsupfit.model.FilterPeriod
import com.gamarra.tecsupfit.model.GymClass
import com.gamarra.tecsupfit.model.GymReservation
import com.gamarra.tecsupfit.model.ReservationStatus

val mockGymClasses = listOf(
    GymClass(
        id = "1",
        name = "Yoga funcional",
        time = "7:00 am",
        room = "Sala 2",
        duration = "50 min",
        description = "Clase enfocada en flexibilidad, control postural y respiración consciente.",
        totalSlots = 15,
        availableSlots = 6,
        period = FilterPeriod.HOY
    ),
    GymClass(
        id = "2",
        name = "Cross Training",
        time = "6:00 pm",
        room = "Sala 1",
        duration = "45 min",
        description = "Entrenamiento funcional de alta intensidad. Cupos limitados.",
        totalSlots = 12,
        availableSlots = 8,
        period = FilterPeriod.HOY
    ),
    GymClass(
        id = "3",
        name = "Spinning",
        time = "7:30 pm",
        room = "Sala 3",
        duration = "45 min",
        description = "Sesión cardiovascular en bicicleta estática con intervalos de ritmo.",
        totalSlots = 20,
        availableSlots = 11,
        period = FilterPeriod.HOY
    ),
    GymClass(
        id = "4",
        name = "Pilates Mat",
        time = "8:00 am",
        room = "Sala 2",
        duration = "50 min",
        description = "Fortalecimiento de la zona media y estabilidad corporal.",
        totalSlots = 10,
        availableSlots = 4,
        period = FilterPeriod.ESTA_SEMANA
    ),
    GymClass(
        id = "5",
        name = "Box Acondicionamiento",
        time = "5:00 pm",
        room = "Sala 4",
        duration = "60 min",
        description = "Técnica de golpeo y resistencia cardiovascular con saco.",
        totalSlots = 14,
        availableSlots = 5,
        period = FilterPeriod.ESTA_SEMANA
    )
)

val mockInitialReservations = listOf(
    GymReservation(
        id = "101",
        className = "Cross Training",
        schedule = "Hoy, 6:00 pm",
        status = ReservationStatus.CONFIRMADA
    ),
    GymReservation(
        id = "102",
        className = "Yoga funcional",
        schedule = "Ayer, 7:00 am",
        status = ReservationStatus.COMPLETADA
    )
)