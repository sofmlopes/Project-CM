package com.example.walkingundead.screens

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.walkingundead.models.MedicineEntry
import com.example.walkingundead.navigation.Screens
import com.example.walkingundead.provider.RepositoryProvider

@Composable
fun Medicine() {
    val database = remember { RepositoryProvider.databaseRepository }

    // State to hold the list of medicines
    var medicines by remember { mutableStateOf<List<MedicineEntry>>(emptyList()) }

    // Fetch medicines from the database
    LaunchedEffect(Unit) {
        database.getAllMedicines { fetchedMedicines ->
            medicines = fetchedMedicines
        }
    }

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
                placeholder = { Text("Search Medicines", color = Color.Gray) },
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
            )

            Spacer(Modifier.height(5.dp))

            //Sort and Filter
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(
                    onClick = { }
                ) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Sort"
                    )
                    Text("Sort")
                }

                Button(
                    onClick = { }
                ) {
                    Icon(
                        Icons.Default.Build,
                        contentDescription = "Filter"
                    )
                    Text("Filter")
                }
            }

            Spacer(Modifier.height(5.dp))


            Button(
                onClick = {
                    val name = "vicodin"
                    val type = "opioid"
                    val location = "2,4"
                    val quantity = 5

                    database.addNewMedicineEntry(name, type, location, quantity)
                }
            ) {
                Text("Register new")
            }

            // Medicine List (List of medicines)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(all = 10.dp)
            ) {
                if (medicines.isEmpty()) {
                    Text("No medicines available", color = Color.Gray)
                } else {
                    medicines.forEach { medicine ->
                        MedicineItem(
                            name = medicine.name?:"",
                            category = medicine.type?:"",
                            count = medicine.quantity
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MedicineItem(name: String, category: String, count: Int) {
    var isDialogVisible by remember { mutableStateOf(false) }
    var quantity by remember { mutableIntStateOf(count) }
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
                text = name,
                fontWeight = FontWeight.Bold,
                style = TextStyle(color = Color.Black)
            )
            Text(
                text = category,
                style = TextStyle(color = Color.Gray)
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
                Text("Edit Quantity")
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
                                onClick = {
                                    isDialogVisible = false
                                    quantity = tempValue
                                }
                            ) {
                                Text("Apply")
                            }
                        } else {
                            Button(
                                onClick = {
                                    isDialogVisible = false
                                    //TODO NEEDS TO REMOVE IT FROM FIREBASE SOMEHOW
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
