package com.example.walkingundead.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.walkingundead.navigation.Screens

@Composable
fun Medicine() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
            .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 80.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Section (Search bar and Icons)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Search Bar
                TextField(
                    value = "",
                    onValueChange = { /* Handle Search */ },
                    placeholder = { Text("Search Medicines", color = Color.Gray) },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White, RoundedCornerShape(8.dp)),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
                )

                /*
                // New Button (camera icon)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* TO DO */ }) {
                        Icon(
                            Icons.Default.AccountBox,
                            contentDescription = "New",
                            tint = Color(0xFF6A4C9C) // Custom purple color for icon
                        )
                    }
                    Text(
                        text = "New",
                        modifier = Modifier.padding(start = 4.dp),
                        color = Color(0xFF6A4C9C),
                        fontWeight = FontWeight.Bold
                    )
                }
                */

            }


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

            /*
            // Sort and Filter Buttons (Icons)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { /* TODO */ }) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Sort",
                        tint = Color(0xFF6A4C9C) // Custom purple color for icon
                    )
                }
                IconButton(onClick = { /* TODO */ }) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Sort",
                        tint = Color(0xFF6A4C9C) // Custom purple color for icon
                    )
                }
                Text("Sort")
                IconButton(onClick = { /* TO DO */ }) {
                    Icon(
                        Icons.Default.Build,
                        contentDescription = "Filter",
                        tint = Color(0xFF6A4C9C) // Custom purple color for icon
                    )
                }
                Text("Filter")
            }
            */

            // Medicine List (List of medicines)
            Column(modifier = Modifier.fillMaxWidth()) {
                MedicineItem(name = "Dipirona", category = "Analgesic", count = 3)
                MedicineItem(name = "Paracetamol", category = "Analgesic", count = 2)
                MedicineItem(name = "Aspirina", category = "Analgesic", count = 2)
                MedicineItem(name = "Ibuprofeno", category = "Analgesic", count = 1)
            }
        }
    }
}

@Composable
fun MedicineItem(name: String, category: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .background(Color.White, RoundedCornerShape(12.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                style = androidx.compose.ui.text.TextStyle(color = Color.Black)
            )
            Text(
                text = category,
                style = androidx.compose.ui.text.TextStyle(color = Color.Gray)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Decrease Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = { /* TO DO */ }) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Decrease",
                        tint = Color(0xFF6A4C9C)
                    )
                }
                Text(
                    text = "Decrease",
                    modifier = Modifier.padding(start = 4.dp),
                    color = Color(0xFF6A4C9C),
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "$count",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold,
                style = androidx.compose.ui.text.TextStyle(color = Color(0xFF6A4C9C))
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = { /* TO DO */ }) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Increase",
                        tint = Color(0xFF6A4C9C)
                    )
                }
                Text(
                    text = "Increase",
                    modifier = Modifier.padding(start = 4.dp),
                    color = Color(0xFF6A4C9C),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
