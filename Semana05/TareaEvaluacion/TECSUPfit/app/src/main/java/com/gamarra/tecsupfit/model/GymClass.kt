package com.gamarra.tecsupfit.model

data class GymClass(
    val id: String,
    val name: String,
    val time: String,
    val room: String,
    val duration: String,
    val description: String,
    val totalSlots: Int,
    val availableSlots: Int,
    val period: FilterPeriod
)

enum class FilterPeriod(val label: String) {
    HOY("Hoy"),
    ESTA_SEMANA("Esta semana")
}

data class GymReservation(
    val id: String,
    val className: String,
    val schedule: String,
    val status: ReservationStatus
)

enum class ReservationStatus {
    CONFIRMADA,
    COMPLETADA
}