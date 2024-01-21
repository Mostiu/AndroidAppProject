package com.example.androidappproject

data class TripState(
    val trips: List<Trip> = emptyList(),
    val destination: String = "",
    val duration: Int = 0,
    val price: Int = 0,
    val rating: Int = 0,
    val transportType: String = "inny",
    val isAddingNewTrip: Boolean = false,
    val isEditingTrip: Boolean = false,
)
