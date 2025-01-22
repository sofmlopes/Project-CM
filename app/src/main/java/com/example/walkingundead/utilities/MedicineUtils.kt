package com.example.walkingundead.utilities

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.walkingundead.models.MedicineEntry
import com.example.walkingundead.provider.RepositoryProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
