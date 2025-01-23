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
import com.example.walkingundead.models.MedicineEntry
import com.example.walkingundead.provider.RepositoryProvider
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Displays a medicine entry with its name, type, location, and quantity.
 * Allows users to edit the quantity or delete the entry through a dialog.
 * Fetches the location address asynchronously based on latitude and longitude.
 */
@Composable
fun MedicineItem(medicine: MedicineEntry, onClick: () -> Unit) {

    val database = remember { RepositoryProvider.databaseRepository }
    val context = LocalContext.current

    var isDialogVisible by remember { mutableStateOf(false) }
    var quantity by remember { mutableIntStateOf(medicine.quantity) }
    var textValue by remember { mutableStateOf(quantity.toString()) }
    var tempValue by remember { mutableIntStateOf(quantity) } //used so changes are not immediately applied to quantity
    // State to store the fetched address
    var address by remember { mutableStateOf<String?>(null) }

    // Fetch address from location
    LaunchedEffect(medicine.location) {
        medicine.location?.let { locationString ->
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
                text = "Name: ${medicine.name ?: ""}",
                fontWeight = FontWeight.Bold,
                style = TextStyle(color = Color.Black)
            )
            Text(
                text = "Type: ${medicine.type ?: ""}",
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

/**
 * https://developer.android.com/develop/ui/compose/components/menu?hl=pt-br
 * Provides sorting options for the list of medicines (by name, type, location, or quantity).
 */
@Composable
fun DropdownMenuWithDetailsMedicine(
    onSortByName: () -> Unit,
    onSortByType: () -> Unit,
    onSortByQuantity: () -> Unit,
    onSortByLocation: () -> Unit
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

/**
 * Filters the medicine list based on the search query, including matching the name, type, location, or quantity.
 *
 */
@Composable
fun filterMedicinesOnSearch (medicines : List<MedicineEntry>, searchQuery : String, context: Context): List<MedicineEntry> {

    // Filter medicines based on the search query
    return medicines.filter { medicine ->
        val matchesName = medicine.name?.contains(searchQuery, ignoreCase = true) ?: false
        val matchesType = medicine.type?.contains(searchQuery, ignoreCase = true) ?: false
        // Try to parse the location into LatLng and fetch the address to match it
        val matchesLocation = medicine.location?.let {
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
            searchQuantity != null && medicine.quantity == searchQuantity  // Check if the quantity matches
        } catch (e: Exception) {
            false  // If there's an error converting to int, return false
        }

        matchesName || matchesType || matchesLocation || matchesQuantity
    }
}

/**
 * Sorts the filtered medicine list based on the selected criterion (name, type, quantity, or location).
 */
@Composable
fun sortMedicines(
    filteredMedicines: List<MedicineEntry>,
    sortBy: String
): List<MedicineEntry> {
    return when (sortBy) {
        "Name" -> filteredMedicines.sortedBy { it.name }
        "Type" -> filteredMedicines.sortedBy { it.type }
        "Quantity" -> filteredMedicines.sortedBy { it.quantity }
        "Location" -> filteredMedicines.sortedBy { distanceToCurrentLocation(it.location) }
        else -> filteredMedicines  // No sorting if the criterion is not recognized
    }
}

@Composable
fun HeaderMedicine (){
    Text(
        text = "MEDICINE",
        color = colorResource(id = R.color.purple_500),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 10.dp),
    )
}

/**
 * https://developer.android.com/develop/ui/compose/text/user-input?hl=pt-br
 * Provides a search bar for filtering medicine entries based on user input.
 */
@Composable
fun SearchBarMedicine(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    TextField(
        value = searchQuery,
        onValueChange = { onSearchQueryChange(it) }, // Update the search query
        placeholder = { Text("Search Medicines", color = Color.DarkGray) },
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(8.dp))
            .fillMaxWidth(),
        textStyle = TextStyle(color = Color.Black)
    )
}
