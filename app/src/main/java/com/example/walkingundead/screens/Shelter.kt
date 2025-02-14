package com.example.walkingundead.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.walkingundead.R
import com.example.walkingundead.models.Shelter
import com.example.walkingundead.provider.RepositoryProvider
import com.example.walkingundead.utilities.DropdownMenuWithDetailsShelter
import com.example.walkingundead.utilities.HeaderShelter
import com.example.walkingundead.utilities.SearchBarShelter
import com.example.walkingundead.utilities.ShelterItem
import com.example.walkingundead.utilities.filterSheltersOnSearch
import com.example.walkingundead.utilities.parseLocation
import com.example.walkingundead.utilities.sortShelters
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun Shelter(onShelterSelected: (LatLng?) -> Unit) {

    val database = RepositoryProvider.databaseRepository
    val scrollState = rememberScrollState()

    var isDialogVisible by remember { mutableStateOf(false) }
    var shelterList by remember { mutableStateOf<List<Shelter>>(emptyList()) }
    //Variables to add new entry
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var numberOfBeds by remember { mutableIntStateOf(0) }
    var occupiedBeds by remember { mutableIntStateOf(0) }
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    var isLocationDialogVisible by remember { mutableStateOf(false) }
    var textValueNrBeds by remember { mutableStateOf("") }
    var textValueOccupiedBeds by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var sortBy by remember { mutableStateOf("Name") }

    LaunchedEffect(Unit) {
        database.getAllShelters { fetchedShelters ->
            shelterList = fetchedShelters
        }
    }

    val filteredShelterList = filterSheltersOnSearch(shelterList, searchQuery)
    val sortedShelters = sortShelters(filteredShelterList,sortBy)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                HeaderShelter()
            }
            SearchBarShelter(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it } // Update the search query when it changes
            )

            Spacer(Modifier.height(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                    shape = RoundedCornerShape(6.dp),
                    onClick = {
                        isDialogVisible = true
                    }
                ) {
                    Text(stringResource(R.string.register_new))
                }
                DropdownMenuWithDetailsShelter(
                    onSortByName = { sortBy = "Name" },
                    onSortByLocation = { sortBy = "Location" },
                    onSortByNumberOfFreeBeds = { sortBy = "Number Of Free Beds" },
                    onSortByNumberOfOccupiedBeds =  { sortBy = "Number Of Occupied Beds" },
                )
            }

            // Shelters List (List of shelters)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(all = 10.dp)
            ) {
                if (filteredShelterList.isEmpty()) {
                    Text("No shelters available", color = Color.Gray)
                } else {
                    sortedShelters.forEach { shelter ->
                        ShelterItem(
                            shelter = shelter,
                            onClick = {
                                val tempLocation = parseLocation(shelter.location)
                                onShelterSelected(tempLocation) // Notify parent about selection
                            }
                        )
                    }
                }
            }
        }

        //Register new pop up
        if (isDialogVisible) {
            Dialog(
                onDismissRequest = {
                    isDialogVisible = false
                }
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(stringResource(R.string.register_shelter), style = MaterialTheme.typography.titleLarge)

                        TextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text(stringResource(R.string.name)) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        TextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text(stringResource(R.string.location)) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = { isLocationDialogVisible = true }
                        ) {
                            Text("Choose Location")
                        }

                        TextField(
                            value = textValueNrBeds,
                            onValueChange = { input ->
                                val number = input.toIntOrNull()
                                if (number != null && number >= 0) {
                                    numberOfBeds = number
                                    textValueNrBeds = "$number"
                                } else if (input.isEmpty()) {
                                    textValueNrBeds = ""
                                }
                            },
                            label = { Text(stringResource(R.string.number_of_beds)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                        )

                        TextField(
                            value = textValueOccupiedBeds,
                            onValueChange = { input ->
                                val number = input.toIntOrNull()
                                if (number != null && number >= 0) {
                                    occupiedBeds = number
                                    textValueOccupiedBeds = "$number"
                                } else if (input.isEmpty()) {
                                    textValueNrBeds = ""
                                }
                            },
                            label = { Text(stringResource(R.string.number_of_occupied_beds)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                        )


                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = {
                                    // Save the shelter to the database
                                    val nrOfBedsSave = numberOfBeds
                                    val occupiedBedsSave = occupiedBeds
                                    database.addNewShelter(name, location, nrOfBedsSave, occupiedBedsSave)
                                    isDialogVisible = false

                                    // Reset fields
                                    name = ""
                                    location = ""
                                    numberOfBeds = 0
                                    occupiedBeds = 0
                                }
                            ) {
                                Text("Save")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            OutlinedButton(
                                onClick = {
                                    isDialogVisible = false
                                }
                            ) {
                                Text("Cancel")
                            }
                        }
                    }
                }
            }
        }

        if (isLocationDialogVisible) {
            Dialog(
                onDismissRequest = {
                    isLocationDialogVisible = false
                } // Closes the map dialog
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            "Select Location on Map",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(8.dp),
                            textAlign = TextAlign.Center
                        )

                        // Constrain the height of the GoogleMap
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f) // Ensures it takes available vertical space proportionally
                        ) {
                            GoogleMap(
                                onMapClick = { latLng ->
                                    Log.d("MapClick", "Location: ${latLng.latitude}, ${latLng.longitude}")
                                    markerPosition = latLng // Update the marker position
                                    location = "${latLng.latitude}, ${latLng.longitude}" // Save location
                                }
                            ) {
                                // Add a marker if the user has tapped
                                markerPosition?.let { position ->
                                    Marker(
                                        state = MarkerState(position = position),
                                        title = "Selected Location",
                                        snippet = "${position.latitude}, ${position.longitude}"
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { isLocationDialogVisible = false }
                        ) {
                            Text("Confirm Location")
                        }
                    }
                }
            }
        }
    }
}

