package com.example.walkingundead.utilities

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.SortByAlpha
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.walkingundead.R
import com.example.walkingundead.models.Shelter
import com.example.walkingundead.provider.RepositoryProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ShelterItem(shelter: Shelter, onClick: () -> Unit) {

    val database = remember { RepositoryProvider.databaseRepository }
    val context = LocalContext.current
    var address by remember { mutableStateOf<String?>(null) }
    var isDialogVisible by remember { mutableStateOf(false) }
    var totalBeds by remember { mutableIntStateOf(shelter.numberOfBeds) }
    var freeBeds by remember { mutableIntStateOf(shelter.numberOfBeds - shelter.occupiedBeds) }
    var tempValue by remember { mutableIntStateOf(totalBeds) } //used so changes are not immediately applied to quantity
    var textValue by remember { mutableStateOf(freeBeds.toString()) }

    // Fetch address from location
    LaunchedEffect(shelter.location) {
        shelter.location?.let { locationString ->
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
            .background(Color.White, RoundedCornerShape(8.dp))
            .clickable { onClick() }, // Trigger callback on click
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
                text = "Location: $address",
                style = TextStyle(color = Color.DarkGray)
            )
        }
        //Edit Button
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                shape = RoundedCornerShape(6.dp),
                onClick = {
                    isDialogVisible = true
                    tempValue = freeBeds // Makes sure the pop up shows the current value
                    textValue = "$freeBeds" // Makes sure the pop up shows the current value
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
                            "Number of Free Beds:",
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
                                    if (number != null && number >= 0 && number <=totalBeds) {
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
                                if(tempValue < totalBeds ){
                                    tempValue++
                                    textValue = tempValue.toString()
                                }
                            }) {
                                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (tempValue >= 0) {
                            Button(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 10.dp),
                                shape = RoundedCornerShape(6.dp),
                                onClick = {
                                    isDialogVisible = false
                                    freeBeds = tempValue
                                    val updatedEntry = shelter.copy(occupiedBeds = totalBeds - freeBeds)
                                    database.editShelter(shelter.id!!, updatedEntry) //hopefully it crashes if it has no ID cuz it should always have an ID
                                }
                            ) {
                                Text("Apply")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownMenuWithDetailsShelter(onSortByName: () -> Unit, onSortByLocation: () -> Unit, onSortByNumberOfBeds: () -> Unit,
                                   onSortByNumberOfOccupiedBeds: () -> Unit) {

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
                text = { Text("Sort By Location") },
                leadingIcon = { Icon(Icons.Outlined.SortByAlpha, contentDescription = null) },
                onClick = {
                    onSortByLocation()
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Sort By Number Of Beds") },
                leadingIcon = { Icon(Icons.Outlined.SortByAlpha, contentDescription = null) },
                onClick = {
                    onSortByNumberOfBeds()
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Sort By Number Of Occupied Beds") },
                leadingIcon = { Icon(Icons.Outlined.SortByAlpha, contentDescription = null) },
                onClick = {
                    onSortByNumberOfOccupiedBeds()
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun SearchBarShelter(searchQuery: String, onSearchQueryChange: (String) -> Unit) {
    TextField(
        value = searchQuery,
        onValueChange = { onSearchQueryChange(it)}, // Update the search query
        placeholder = { Text("Search Shelter", color = Color.DarkGray) },
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(8.dp))
            .fillMaxWidth(),
        textStyle = TextStyle(color = Color.Black)
    )
}

@Composable
fun HeaderShelter() {
    Text(
        text = "SHELTER",
        color = colorResource(id = R.color.purple_500),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 10.dp),
    )
}

/**
 * Filter medicines based on the search query
 */
@Composable
fun filterSheltersOnSearch(shelterList: List<Shelter>, searchQuery: String, context: Context): List <Shelter>{

    return shelterList.filter {
        it.name?.contains(searchQuery, ignoreCase = true) ?: false || // Match name
                it.location?.contains(searchQuery, ignoreCase = true) ?: false // Match location
    }
}

/**
 * Function to sort shelters based on the selected criterion
 */
@Composable
fun sortShelters(
    filteredShelterList: List<Shelter>,
    sortBy: String
): List<Shelter> {
    return when (sortBy) {
        "Name" -> filteredShelterList.sortedBy { it.name }
        "Location" -> filteredShelterList.sortedBy { it.location }
        "Number Of Beds" -> filteredShelterList.sortedBy { it.numberOfBeds}
        "Number Of Occupied Beds" -> filteredShelterList.sortedBy { it.occupiedBeds}
        else -> filteredShelterList
    }
}

