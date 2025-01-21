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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.SortByAlpha
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.walkingundead.models.Food
import com.example.walkingundead.provider.RepositoryProvider
import com.example.walkingundead.utilities.getAddressFromCoordinates
import com.example.walkingundead.utilities.parseLocation
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun Food() {

    val database = RepositoryProvider.databaseRepository
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var isDialogVisible by remember { mutableStateOf(false) }
    var foods by remember { mutableStateOf<List<Food>>(emptyList()) }
    //Variables to add new entry
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(0) }
    var textValue by remember { mutableStateOf("") }
    var isLocationDialogVisible by remember { mutableStateOf(false) }
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    // Sort options
    // This serves as the default sorting option when the screen is first displayed.
    var sortBy by remember { mutableStateOf("Name") }
    var expirationDate by remember { mutableStateOf("") } // Holds the selected date as a string
    var showDatePicker by remember { mutableStateOf(false) } // Controls the visibility of the DatePickerModal

    LaunchedEffect(Unit) {
        database.getAllFoods { fetchedFoods ->
            foods = fetchedFoods
        }
    }

    // Filter foods based on the search query
    val filteredFoods = foods.filter { food ->
        val matchesName = food.name?.contains(searchQuery, ignoreCase = true) ?: false
        val matchesType = food.type?.contains(searchQuery, ignoreCase = true) ?: false
        // Try to parse the location into LatLng and fetch the address to match it
        val matchesLocation = food.location?.let {
            parseLocation(it)?.let { latLng ->
                // Fetch address asynchronously and compare it to the search query
                var address: String? = null
                try {
                    address = getAddressFromCoordinates(context, latLng.latitude, latLng.longitude)
                } catch (e: Exception) {
                    Log.e("AddressError", "Error fetching address", e)
                }
                address?.contains(searchQuery, ignoreCase = true) ?: false
            } ?: false
        } ?: false
        // Check if quantity matches the search query
        val matchesQuantity = try {
            val searchQuantity = searchQuery.toIntOrNull()  // Try to convert searchQuery to an integer
            searchQuantity != null && food.quantity == searchQuantity  // Check if the quantity matches
        } catch (e: Exception) {
            false  // If there's an error converting to int, return false
        }
        val matchesExpirationDate = food.expirationDate?.contains(searchQuery, ignoreCase = true) ?: false

        matchesName || matchesType || matchesLocation || matchesQuantity || matchesExpirationDate
    }
    // Sort foods after filtering
    val sortedFoods = when (sortBy) {
        "Name" -> filteredFoods.sortedBy { it.name }
        "Type" -> filteredFoods.sortedBy { it.type }
        "Quantity" -> filteredFoods.sortedBy { it.quantity }
        "Location" -> filteredFoods.sortedBy { it.location }
        "Expiration Date" -> filteredFoods.sortedBy { it.expirationDate }
        else -> filteredFoods
    }

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
                    text = "FOOD",
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
                placeholder = { Text("Search Food", color = Color.DarkGray) },
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black)
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
                    Text("Register new")
                }
                DropdownMenuWithDetails(
                    onSortByName = { sortBy = "Name" },
                    onSortByType = { sortBy = "Type" },
                    onSortByQuantity = { sortBy = "Quantity" },
                    onSortByLocation = { sortBy = "Location" },
                    onSortByExpirationDate = { sortBy = "Expiration Date" }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(all = 10.dp)
            ) {
                if (filteredFoods.isEmpty()) {
                    Text("No foods available", color = Color.DarkGray)
                } else {
                    // Display sorted and filtered foods
                    sortedFoods.forEach { food ->
                        FoodItem(food = food)
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
                        Text(stringResource(R.string.register_food), style = MaterialTheme.typography.titleLarge)

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
                        // TextField to display the selected expiration date
                        TextField(
                            value = expirationDate,
                            onValueChange = { /* No manual editing allowed */ },
                            label = { Text("Expiration Date") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true, // Prevent manual input
                            trailingIcon = {
                                IconButton(onClick = { showDatePicker = true }) {
                                    Icon(Icons.Default.CalendarToday, contentDescription = "Pick a Date")
                                }
                            }
                        )
                        // Show the DatePickerModal when triggered
                        if (showDatePicker) {
                            DatePickerModal(
                                onDateSelected = { selectedDateMillis ->
                                    if (selectedDateMillis != null) {
                                        // Format the selected date
                                        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                                        expirationDate = formatter.format(Date(selectedDateMillis))
                                    }
                                    showDatePicker = false // Close the modal after selection
                                },
                                onDismiss = { showDatePicker = false } // Handle dismiss
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = {
                                    // Save the food to the database
                                    database.addNewFoodEntry(name, type, location, quantity, expirationDate)
                                    isDialogVisible = false

                                    // Reset fields
                                    name = ""
                                    type = ""
                                    location = ""
                                    quantity = 0
                                    expirationDate = ""
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
fun FoodItem(food: Food) {

    val database = remember { RepositoryProvider.databaseRepository }
    val context = LocalContext.current

    var isDialogVisible by remember { mutableStateOf(false) }
    var quantity by remember { mutableIntStateOf(food.quantity) }
    var textValue by remember { mutableStateOf(quantity.toString()) }
    var tempValue by remember { mutableIntStateOf(quantity) } //used so changes are not immediately applied to quantity
    // State to store the fetched address
    var address by remember { mutableStateOf<String?>(null) }

    // Fetch address from location
    LaunchedEffect(food.location) {
        food.location?.let { locationString ->
            parseLocation(locationString)?.let { latLng ->
                withContext(Dispatchers.IO) {
                    // Fetch the address from the coordinates
                    address = getAddressFromCoordinates(context, latLng.latitude, latLng.longitude)
                }
            }
        }
    }

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
                text = "Name: ${food.name ?: ""}",
                fontWeight = FontWeight.Bold,
                style = TextStyle(color = Color.Black)
            )
            Text(
                text = "Type: ${food.type ?: ""}",
                style = TextStyle(color = Color.DarkGray)
            )
            Text(
                text = "Expiration Date: ${food.expirationDate ?: ""}",
                style = TextStyle(color = Color.DarkGray)
            )
            Text(
                text = "Location: ${address ?: ""}",
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
                                    val updatedEntry = food.copy(quantity = quantity)
                                    database.editFoodEntry(food.id!!, updatedEntry) //hopefully it crashes if it has no ID cuz it should always have an ID
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
                                    food.id?.let { id ->
                                        database.deleteFoodEntry(id)
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

/**
 * https://developer.android.com/develop/ui/compose/components/datepickers
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

/**
 * https://developer.android.com/develop/ui/compose/components/menu?hl=pt-br
 */
@Composable
fun DropdownMenuWithDetails(
    onSortByName: () -> Unit,
    onSortByType: () -> Unit,
    onSortByQuantity: () -> Unit,
    onSortByLocation: () -> Unit,
    onSortByExpirationDate: () -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Sort By Name") },
                leadingIcon = { Icon(Icons.Outlined.SortByAlpha, contentDescription = null) },
                onClick = {
                    onSortByName()
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Sort By Type") },
                leadingIcon = { Icon(Icons.Outlined.SortByAlpha, contentDescription = null) },
                onClick = {
                    onSortByType()
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Sort By Location") },
                leadingIcon = { Icon(Icons.Outlined.SortByAlpha, contentDescription = null) },
                onClick = {
                    onSortByLocation()
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Sort By ExpirationDate") },
                leadingIcon = { Icon(Icons.Outlined.SortByAlpha, contentDescription = null) },
                onClick = {
                    onSortByExpirationDate()
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Sort By Quantity") },
                leadingIcon = { Icon(Icons.Outlined.SortByAlpha, contentDescription = null) },
                onClick = {
                    onSortByQuantity()
                    expanded = false
                }
            )
        }
    }
}