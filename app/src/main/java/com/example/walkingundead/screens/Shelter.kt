package com.example.walkingundead.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.walkingundead.R
import com.example.walkingundead.models.MedicineEntry
import com.example.walkingundead.models.Shelter
import com.example.walkingundead.provider.RepositoryProvider

@Composable
fun Shelter() {
    val database = RepositoryProvider.databaseRepository

    var isDialogVisible by remember { mutableStateOf(false) }

    var shelterList by remember { mutableStateOf<List<Shelter>>(emptyList()) }

    LaunchedEffect(Unit) {
        database.getAllShelters { fetchedShelters ->
            shelterList = fetchedShelters
        }
    }

    //Variables to add new entry
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var numberOfBeds by remember { mutableIntStateOf(0) }
    var occupiedBeds by remember { mutableIntStateOf(0) }
    var textValueNrBeds by remember { mutableStateOf("") }
    var textValueOccupiedBeds by remember { mutableStateOf("") }

    /*
    val shelterList = listOf(
        Shelter(
            name = "Abandoned Warehouse",
            location = "2.45,7.3",
            numberOfBeds = 4,
            occupiedBeds = 1,
        ),
        Shelter(
            name = "Abandoned Warehouse",
            location = "2.45,7.3",
            numberOfBeds = 4,
            occupiedBeds = 1,
        ),
        Shelter(
            name = "Abandoned Warehouse",
            location = "2.45,7.3",
            numberOfBeds = 4,
            occupiedBeds = 1,
        )
    )
    */

    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
            .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Search Bar
            TextField(
                value = "",
                onValueChange = { /* Handle Search */ },
                placeholder = { Text("Search Shelter", color = Color.Gray) },
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black)
            )

            Spacer(Modifier.height(5.dp))

            //Sort and Filter
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                    shape = RoundedCornerShape(6.dp),
                    onClick = { }
                ) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Sort"
                    )
                    Text("Sort")
                }

                Button(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                    shape = RoundedCornerShape(6.dp),
                    onClick = { }
                ) {
                    Icon(
                        Icons.Default.Build,
                        contentDescription = "Filter"
                    )
                    Text("Filter")
                }
            }

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
            }


            // Medicine List (List of medicines)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(all = 10.dp)
            ) {
                if (shelterList.isEmpty()) {
                    Text("No medicines available", color = Color.Gray)
                } else {
                    shelterList.forEach { shelter ->
                        ShelterItem(
                            shelter
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
                                    // Save the medicine to the database
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
    }
}

@Composable
fun ShelterItem(shelter: Shelter) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp) //5dp = vertical space between cards
            .background(Color.White, RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        //Name and Category
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp, vertical = 20.dp)
        ) {
            Text(
                text = shelter.name?:"",
                fontWeight = FontWeight.Bold,
                style = TextStyle(color = Color.Black)
            )
            Text(
                text = "Number of free beds: ${shelter.numberOfBeds - shelter.occupiedBeds}",
                style = TextStyle(color = Color.DarkGray)
            )
            Text(
                text = "Location: ${shelter.location}",
                style = TextStyle(color = Color.DarkGray)
            )
        }

    }

}
