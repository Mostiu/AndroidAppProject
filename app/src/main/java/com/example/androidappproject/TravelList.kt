package com.example.androidappproject

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
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
                          Image(
                            painter = when(trip.transportType){
                                 "inny" -> painterResource(id = R.drawable.baseline_question_mark_24)
                                 "samolot" -> painterResource(id = R.drawable.baseline_airplanemode_active_24)
                                 "autobus" -> painterResource(id = R.drawable.baseline_directions_bus_24)
                                 "pociąg" -> painterResource(id = R.drawable.baseline_train_24)
                                 "statek" -> painterResource(id = R.drawable.baseline_water_24)
                                 else -> painterResource(id = R.drawable.baseline_question_mark_24)
                            },
                            contentDescription = "Transport type",
                            modifier = Modifier.padding(end = 16.dp)
                          )
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
    val choices = listOf("inny", "samolot", "autobus", "pociąg", "statek")
    val (selectedOption, setSelectedOption) = remember { mutableStateOf(choices[0]) }
    val ratings = listOf("1","2","3","4","5")
    val (selectedRating, setSelectedRating) = remember { mutableStateOf(ratings[0]) }
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
               mySpinner(label ="rating", choices = ratings, selectedOption = selectedRating, setSelected = setSelectedRating, onEvent = onEvent)
                Text(
                    text = "Transport type",
                )
                mySpinner(label = "transport type", choices =choices , selectedOption = selectedOption, setSelected = setSelectedOption, onEvent = onEvent)
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
    val choices = listOf("inny", "samolot", "autobus", "pociąg", "statek")
    val (selectedOption, setSelectedOption) = remember { mutableStateOf(choices[0]) }
    val ratings = listOf("1","2","3","4","5")
    val (selectedRating, setSelectedRating) = remember { mutableStateOf(ratings[0]) }
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
                    text = "Rating: $rating",
                )
                mySpinner(label ="rating", choices = ratings, selectedOption = selectedRating, setSelected = setSelectedRating, onEvent = onEvent)
                Text(
                    text = "Transport type: $transportType",
                )
                mySpinner(label = "transport type", choices =choices , selectedOption = selectedOption, setSelected = setSelectedOption, onEvent = onEvent)
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

@Composable
fun mySpinner(
    label: String,
    choices: List<String>,
    selectedOption: String,
    setSelected: (selected: String) -> Unit,
    onEvent: (TripEvent) -> Unit
) {
    var spinnerText by remember { mutableStateOf(selectedOption) }
    var my_expanded by remember { mutableStateOf(false) }

    Row {
        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .width(150.dp),
            text = label,
            fontWeight = FontWeight.Bold
        )
        Box(
            Modifier
                .width(150.dp)
                .border(width = 1.dp, color = Color.Black)
        ) {
            Row(Modifier
                .padding(start = 12.dp)
                .clickable {
                    my_expanded = !my_expanded
                }
                .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedOption,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
                DropdownMenu(expanded = my_expanded, onDismissRequest = { my_expanded = false }) {
                    choices.forEach { a_choice ->
                        DropdownMenuItem(
                            text = { Text(text = a_choice) },
                            onClick = {
                                my_expanded = false
                                spinnerText = a_choice
                                setSelected(spinnerText)
                                if(label == "rating")
                                    onEvent(TripEvent.SetRating(spinnerText.toInt()))
                                else if(label == "transport type")
                                    onEvent(TripEvent.SetTransportType(spinnerText))
                            })

                    }
                }
            }
        }
    }
}





