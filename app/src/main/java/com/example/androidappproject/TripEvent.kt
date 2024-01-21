package com.example.androidappproject

sealed interface TripEvent{
    object saveTrip: TripEvent
    data class SetDestination(val destination: String): TripEvent
    data class SetDuration(val duration: Int): TripEvent
    data class SetPrice(val price: Int): TripEvent
    data class SetRating(val rating: Int): TripEvent
    data class SetTransportType(val transportType: String): TripEvent

    object ShowDialog: TripEvent
    object HideDialog: TripEvent

    data class EditTrip(val trip:Trip): TripEvent

    object HideEditDialog: TripEvent

    object ShowEditDialog: TripEvent

    data class DeleteTrip(val trip: Trip): TripEvent
}