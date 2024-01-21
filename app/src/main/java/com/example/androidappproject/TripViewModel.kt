package com.example.androidappproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TripViewModel(private val dao: TripDao): ViewModel() {

    private val _state = MutableStateFlow(TripState())
    private val _trips = dao.getAllTrips().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _trips) { state, trips ->
        state.copy(trips = trips)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TripState())

    fun onEvent(event: TripEvent) {
        when(event) {
            is TripEvent.DeleteTrip -> {
                viewModelScope.launch {
                    dao.deleteTrip(event.trip)
                }
            }
            TripEvent.HideDialog -> {
                _state.update {
                    it.copy(isAddingNewTrip = false)
                }
            }
            is TripEvent.SetDestination -> {
                _state.update {
                    it.copy(destination = event.destination)
                }
            }
            is TripEvent.SetDuration -> {
                _state.update {
                    it.copy(duration = event.duration)
                }
            }
            is TripEvent.SetPrice -> {
                _state.update {
                    it.copy(price = event.price)
                }

            }
            is TripEvent.SetRating -> {
                _state.update {
                    it.copy(rating = event.rating)
                }

            }
            is TripEvent.SetTransportType -> {
                _state.update {
                    it.copy(transportType = event.transportType)
                }
            }
            TripEvent.ShowDialog -> {
                _state.update {
                    it.copy(isAddingNewTrip = true)
                }
            }
            TripEvent.ShowEditDialog -> {
                _state.update {
                    it.copy(isEditingTrip = true)
                }
            }
            is TripEvent.EditTrip -> {
                // Assuming you have a selectedTripId variable in TripState to store the ID of the trip being edited
                val selectedTrip = event.trip

                // Retrieve the existing trip from the database based on the ID
                val existingTrip = _trips.value.find { it == selectedTrip }

                // Check if the existing trip is not null
                if (existingTrip != null) {
                    // Update the information of the existing trip with new values
                    if(state.value.destination != "")
                        existingTrip.destination = state.value.destination
                    if(state.value.duration != 0)
                        existingTrip.duration = state.value.duration
                    if(state.value.price != 0)
                        existingTrip.price = state.value.price
                    if(state.value.rating != 0)
                        existingTrip.rating = state.value.rating
                    if(state.value.transportType != "inny")
                        existingTrip.transportType = state.value.transportType

                    // Save the updated trip back to the database
                    viewModelScope.launch {
                        dao.upsertTrip(existingTrip)
                    }
                }

                // Reset the editing state
                _state.update {
                    it.copy(
                        destination = "",
                        duration = 0,
                        price = 0,
                        rating = 0,
                        transportType = "inny",
                        isAddingNewTrip = false,
                        isEditingTrip = false,
                    )
                }
            }

            TripEvent.HideEditDialog -> {
                _state.update {
                    it.copy(isEditingTrip = false)
                }
            }
            TripEvent.saveTrip -> {
                val destination = state.value.destination
                val duration = state.value.duration
                val price = state.value.price
                val rating = state.value.rating
                val transportType = state.value.transportType

                val trip = Trip(destination, duration, price, rating, transportType)
                viewModelScope.launch {
                    dao.upsertTrip(trip)
                }
                _state.update {
                    it.copy(
                        destination = "",
                        duration = 0,
                        price = 0,
                        rating = 0,
                        transportType = "inny",
                        isAddingNewTrip = false,
                        isEditingTrip = false,
                    )
                }
            }
        }
    }


}