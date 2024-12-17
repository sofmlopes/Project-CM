package com.example.walkingundead.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.walkingundead.navigation.Screens

@Composable
fun Medicine() {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
            .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 100.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(bottom = 20.dp),
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

            // Medicine List (List of medicines)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(all = 10.dp)
            ) {
                MedicineItem(name = "Dipirona", category = "Analgesic", count = 3)
                MedicineItem(name = "Paracetamol", category = "Analgesic", count = 2)
                MedicineItem(name = "Aspirina", category = "Analgesic", count = 2)
                MedicineItem(name = "Ibuprofeno", category = "Analgesic", count = 1)
                MedicineItem(name = "Dipirona", category = "Analgesic", count = 3)
                MedicineItem(name = "Paracetamol", category = "Analgesic", count = 2)
                MedicineItem(name = "Aspirina", category = "Analgesic", count = 2)
                MedicineItem(name = "Ibuprofeno", category = "Analgesic", count = 1)
                MedicineItem(name = "Dipirona", category = "Analgesic", count = 3)
                MedicineItem(name = "Paracetamol", category = "Analgesic", count = 2)
                MedicineItem(name = "Aspirina", category = "Analgesic", count = 2)
                MedicineItem(name = "Ibuprofeno", category = "Analgesic", count = 1)
                MedicineItem(name = "Dipirona", category = "Analgesic", count = 3)
                MedicineItem(name = "Paracetamol", category = "Analgesic", count = 2)
                MedicineItem(name = "Aspirina", category = "Analgesic", count = 2)
                MedicineItem(name = "Ibuprofeno", category = "Analgesic", count = 1)
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
                text = "Quantity: $count",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold,
                style = TextStyle(color = Color.Black)
            )

            Button(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                shape = RoundedCornerShape(6.dp),
                onClick = { }
            ) {
                Text("Edit Quantity")
            }
        }
    }
}
