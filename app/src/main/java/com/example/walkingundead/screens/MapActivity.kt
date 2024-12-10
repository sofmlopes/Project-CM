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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.walkingundead.navigation.Screens
import com.example.walkingundead.provider.RepositoryProvider

@Composable
fun MapScreen(navController: NavController) { //o nome da função tem que ser assim pq há outra do kotlin com esse nome

    val authRepository = remember { RepositoryProvider.authRepository }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {

            Text(
                text = "Authenticated as ${authRepository.email}"
            )

            Text(
                text = "IMAGINE A MAP HERE"
            )

            Button(
                onClick = {
                    navController.navigate(route = Screens.Authentication.route)
                }
            ) {
                Text("back to authentication")
            }
        }
    }
}