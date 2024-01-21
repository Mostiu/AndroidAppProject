package com.example.androidappproject

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TravelList(navController: NavController, state: TripState, onEvent: (TripEvent) -> Unit) {

    var selectedTripPosition by remember { mutableStateOf(-1) }


    Scaffold(
        floatingActionButton = {
        FloatingActionButton(onClick = {
            onEvent(TripEvent.ShowDialog)
        }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Trip")
        }
    },
        modifier = Modifier.padding(16.dp)
    ) { padding ->
        if(state.isAddingNewTrip){
            AddTripDialog(state = state, onEvent = onEvent)
        }
        if(state.isEditingTrip){
            EditTripDialog(state = state, onEvent = onEvent, id = selectedTripPosition)
        }
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
                item {
                    Text(
                        text = "Travel List",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                items(state.trips) { trip ->
                   Row(
                       modifier = Modifier
                           .fillMaxWidth()
                           .clickable {
                               selectedTripPosition = state.trips.indexOf(trip)
                               onEvent(TripEvent.ShowEditDialog)
                           }

                   ){
                       Column(
                           modifier = Modifier.weight(1f),
                       ) {
                            Text(
                                text = "${trip.destination}",
                                fontSize = 20.sp,
                            )
                           Text(
                               text = "Duration: ${trip.duration} days Cost: ${trip.price}",
                               fontSize = 12.sp,
                           )

                       }
                       IconButton(onClick = {
                           onEvent(TripEvent.DeleteTrip(trip))
                       }) {
                           Icon(
                               imageVector = Icons.Default.Delete,
                               contentDescription = "Delete Trip"
                           )
                       }
                   }
                    Divider()
                }
            }
        }
        
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTripDialog(
    state: TripState,
    onEvent: (TripEvent) -> Unit,
    modifier: Modifier = Modifier
){
    AlertDialog(
        modifier = modifier,
        title = { Text(text = "Add Trip") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ){
                Text(
                    text = "Destination",
                )
                TextField(
                    value = state.destination,
                    onValueChange = {
                        onEvent(TripEvent.SetDestination(it))
                    },
                    placeholder = { Text(text = "Destination") },
                )
                Text(
                    text = "Duration",
                )
                TextField(
                    value = state.duration.toString(),
                    onValueChange = {
                        val duration = it.toIntOrNull() ?: 0
                        onEvent(TripEvent.SetDuration(duration))
                    },
                    placeholder = { Text(text = "duration") },
                )
                Text(
                    text = "Price",
                )
                TextField(
                    value = state.price.toString(),
                    onValueChange = {
                        val price = it.toIntOrNull() ?: 0
                        onEvent(TripEvent.SetPrice(price))
                    },
                    placeholder = { Text(text = "price") },
                )
                Text(
                    text = "Rating",
                )
                TextField(
                    value = state.rating.toString(),
                    onValueChange = {
                        val rating = it.toIntOrNull() ?: 0
                        onEvent(TripEvent.SetRating(rating))
                    },
                    placeholder = { Text(text = "Rating") },
                )
                Text(
                    text = "Transport type",
                )
                TextField(
                    value = state.transportType,
                    onValueChange = {
                        onEvent(TripEvent.SetTransportType(it))
                    },
                    placeholder = { Text(text = "Transport type") },
                )
            }
        },
        onDismissRequest = {
            onEvent(TripEvent.HideDialog)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onEvent(TripEvent.saveTrip)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onEvent(TripEvent.HideDialog)
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun EditTripDialog(
    state: TripState,
    onEvent: (TripEvent) -> Unit,
    modifier: Modifier = Modifier,
    id: Int
){
    var destination by remember { mutableStateOf(state.trips[id].destination) }
    var duration by remember { mutableStateOf(state.trips[id].duration) }
    var price by remember { mutableStateOf(state.trips[id].price) }
    var rating by remember { mutableStateOf(state.trips[id].rating) }
    var transportType by remember { mutableStateOf(state.trips[id].transportType) }

    AlertDialog(
        modifier = modifier,
        title = { Text(text = "Show And Edit Trip") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ){
                Text(
                    text = "Destination",
                )
                TextField(
                    value = destination.toString(),
                    onValueChange = {
                        destination = it
                        onEvent(TripEvent.SetDestination(it))
                    },
                    placeholder = { Text(text = "Destination") },
                )
                Text(
                    text = "Duration",
                )
                TextField(
                    value = duration.toString(),
                    onValueChange = {
                        duration = it.toIntOrNull() ?: 0
                        onEvent(TripEvent.SetDuration(duration!!))
                    },
                    placeholder = { Text(text = "duration") },
                )
                Text(
                    text = "Price",
                )
                TextField(
                    value = price.toString(),
                    onValueChange = {
                        price = it.toIntOrNull() ?: 0
                        onEvent(TripEvent.SetPrice(price!!))
                    },
                    placeholder = { Text(text = "price") },
                )
                Text(
                    text = "Rating",
                )
                TextField(
                    value = rating.toString(),
                    onValueChange = {
                        rating = it.toIntOrNull() ?: 0
                        onEvent(TripEvent.SetRating(rating!!))
                    },
                    placeholder = { Text(text = "Rating") },
                )
                Text(
                    text = "Transport type",
                )
                TextField(
                    value = transportType.toString(),
                    onValueChange = {
                        transportType = it
                        onEvent(TripEvent.SetTransportType(it))
                    },
                    placeholder = { Text(text = "Transport type") },
                )
            }
        },
        onDismissRequest = {
            onEvent(TripEvent.HideEditDialog)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onEvent(TripEvent.EditTrip(state.trips[id]))
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onEvent(TripEvent.HideEditDialog)
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}
