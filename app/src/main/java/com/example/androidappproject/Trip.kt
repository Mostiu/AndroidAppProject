package com.example.androidappproject

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Trip(
    var destination: String? = "Brak",
    var duration: Int? = 0,
    var price: Int? = 0,
    var rating: Int? = 0,
    var transportType: String? = "inny",
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
)
