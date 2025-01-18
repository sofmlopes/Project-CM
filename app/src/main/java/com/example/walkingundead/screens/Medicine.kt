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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.walkingundead.R
import com.example.walkingundead.models.MedicineEntry
import com.example.walkingundead.provider.RepositoryProvider
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun Medicine() {
    val database = RepositoryProvider.databaseRepository

    var isDialogVisible by remember { mutableStateOf(false) }

    var medicines by remember { mutableStateOf<List<MedicineEntry>>(emptyList()) }

    LaunchedEffect(Unit) {
        database.getAllMedicines { fetchedMedicines ->
            medicines = fetchedMedicines
        }
    }

    //Variables to add new entry
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(0) }
    var textValue by remember { mutableStateOf("") }
    var isLocationDialogVisible by remember { mutableStateOf(false) }
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Filter medicines based on the search query
    val filteredMedicines = medicines.filter {
        it.name?.contains(searchQuery, ignoreCase = true) ?: false || // Match name
                it.type?.contains(searchQuery, ignoreCase = true) ?: false || // Match type
                it.location?.contains(searchQuery, ignoreCase = true) ?: false // Match location
    }

    val scrollState = rememberScrollState()

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
                //horizontalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "MEDICINE",
                    color = colorResource(id = R.color.purple_500),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 10.dp),
                )
            }
            // Search Bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it }, // Update the search query
                placeholder = { Text("Search Medicines", color = Color.DarkGray) },
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
                    Text("Register new")
                }
            }

            // Medicine List (List of medicines)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(all = 10.dp)
            ) {
                if (filteredMedicines.isEmpty()) {
                    Text("No medicines available", color = Color.DarkGray)
                } else {
                    filteredMedicines.forEach { medicine ->
                        MedicineItem(
                            medicine
                        )
                    }
                }
            }
        }

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
                        Text(stringResource(R.string.register_medicine), style = MaterialTheme.typography.titleLarge)

                        TextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        TextField(
                            value = type,
                            onValueChange = { type = it },
                            label = { Text("Type") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        TextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text("Location") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = { isLocationDialogVisible = true }
                        ) {
                            Text("Choose Location")
                        }

                        TextField(
                            value = textValue,
                            onValueChange = { input ->
                                val number = input.toIntOrNull()
                                if (number != null && number >= 0) {
                                    quantity = number
                                    textValue = "$number"
                                } else if (input.isEmpty()) {
                                    textValue = ""
                                }
                            },
                            label = { Text(stringResource(R.string.quantity)) },
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
                                    val quantityInt = quantity
                                    database.addNewMedicineEntry(name, type, location, quantityInt)
                                    isDialogVisible = false

                                    // Reset fields
                                    name = ""
                                    type = ""
                                    location = ""
                                    quantity = 0
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

@Composable
fun MedicineItem(medicine: MedicineEntry) {

    val database = remember { RepositoryProvider.databaseRepository }

    var isDialogVisible by remember { mutableStateOf(false) }
    var quantity by remember { mutableIntStateOf(medicine.quantity) }
    var textValue by remember { mutableStateOf(quantity.toString()) }
    var tempValue by remember { mutableIntStateOf(quantity) } //used so changes are not immediately applied to quantity

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
                text = medicine.name?:"",
                fontWeight = FontWeight.Bold,
                style = TextStyle(color = Color.Black)
            )
            Text(
                text = medicine.type?:"",
                style = TextStyle(color = Color.DarkGray)
            )
            Text(
                text = medicine.location?:"",
                style = TextStyle(color = Color.DarkGray)
            )
        }

        //Quantity and Edit Button
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quantity: $quantity",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold,
                style = TextStyle(color = Color.Black)
            )

            Button(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                shape = RoundedCornerShape(6.dp),
                onClick = {
                    isDialogVisible = true
                    tempValue = quantity // Makes sure the pop up shows the current value
                    textValue = "$quantity" // Makes sure the pop up shows the current value
                }
            ) {
                Text("Edit")
            }
        }

        // Dialog content
        if (isDialogVisible) {
            Dialog(
                onDismissRequest = {
                    isDialogVisible = false
                } // Closes the dialog when the background is tapped
            ) {
                // Pop-up content
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .wrapContentHeight()
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            "Quantity:",
                            fontSize = 20.sp
                        )

                        Spacer(Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(onClick = {
                                if (tempValue > 0) {
                                    tempValue--
                                    textValue = tempValue.toString()
                                }
                            }) {
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Decrease"
                                )
                            }

                            TextField(
                                value = textValue,
                                onValueChange = { input ->
                                    val number = input.toIntOrNull()
                                    if (number != null && number >= 0) {
                                        tempValue = number
                                        textValue = number.toString()
                                    } else if (input.isEmpty()) {
                                        textValue = ""
                                    }
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .width(100.dp),
                                singleLine = true
                            )

                            IconButton(onClick = {
                                tempValue++
                                textValue = tempValue.toString()
                            }) {
                                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (tempValue != 0) {
                            Button(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 10.dp),
                                shape = RoundedCornerShape(6.dp),
                                onClick = {
                                    isDialogVisible = false
                                    quantity = tempValue
                                    val updatedEntry = medicine.copy(quantity = quantity)
                                    database.editMedicineEntry(medicine.id!!, updatedEntry) //hopefully it crashes if it has no ID cuz it should always have an ID
                                }
                            ) {
                                Text("Apply")
                            }
                        } else {
                            Button(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 10.dp),
                                shape = RoundedCornerShape(6.dp),
                                onClick = {
                                    isDialogVisible = false
                                    medicine.id?.let { id ->
                                        database.deleteMedicineEntry(id)
                                    } ?: Log.e("DeleteError", "Cannot delete entry with no ID")
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red
                                )
                            ) {
                                Text("Delete entry")
                            }
                        }
                    }
                }
            }
        }
    }
}
