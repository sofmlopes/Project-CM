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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.walkingundead.R
import com.example.walkingundead.models.Profile
import com.example.walkingundead.provider.RepositoryProvider
import com.example.walkingundead.utilities.ProfileItem

@Composable
fun Profiles() {
    val database = RepositoryProvider.databaseRepository


    var profiles by remember { mutableStateOf<List<Profile>>(emptyList()) }

    LaunchedEffect(Unit) {
        database.getAllProfiles { fetchedProfiles ->
            profiles = fetchedProfiles
        }
    }

    var searchQuery by remember { mutableStateOf("") }

    // Filter medicines based on the search query
    val filteredProfiles = profiles.filter {
        it.email?.contains(searchQuery, ignoreCase = true) ?: false
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
                    text = "PROFILES",
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
                placeholder = { Text("Search Profiles", color = Color.DarkGray) },
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black)
            )

            Spacer(Modifier.height(5.dp))
            
            // Profile List
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(all = 10.dp)
            ) {
                if (filteredProfiles.isEmpty()) {
                    Text("No medicines available", color = Color.DarkGray)
                } else {
                    filteredProfiles.forEach { profile ->
                        ProfileItem(
                            profile
                        )
                    }
                }
            }
        }
    }
}

