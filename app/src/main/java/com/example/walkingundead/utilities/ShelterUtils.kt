package com.example.walkingundead.utilities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.walkingundead.models.Shelter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ShelterItem(shelter: Shelter) {
    val context = LocalContext.current
    var address by remember { mutableStateOf<String?>(null) }

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
