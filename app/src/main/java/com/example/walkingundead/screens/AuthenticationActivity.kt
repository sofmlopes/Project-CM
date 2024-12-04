package com.example.walkingundead.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.walkingundead.navigation.Screens

@Composable
fun Authentication(navController: NavController) {
    Column(
        modifier = Modifier
            .padding(all = 20.dp)
            .background(color = Color.Cyan)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        Text(
            text = "Authentication WOOOO"
        )

        Button(
            onClick = {
                navController.navigate(route = Screens.Map.route)
            }
        ) {
            Text("GO TO MAP WOOO")
        }

    }
}