package com.example.androidappproject

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Trip::class],
    version = 1

)

abstract class TripDatabase: RoomDatabase() {

    abstract val dao: TripDao
}