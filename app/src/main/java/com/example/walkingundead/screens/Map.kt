package com.example.walkingundead.screens

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
import androidx.compose.ui.unit.dp
import com.example.walkingundead.navigation.Screens
import com.example.walkingundead.provider.RepositoryProvider
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun Map() {

    val authRepository = remember { RepositoryProvider.authRepository }
    val navController = remember { RepositoryProvider.navController }

    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(LatLng(38.736946, -9.142685), 10f) // San Francisco as an example
    }

    // Define the marker position
    val position = LatLng(38.736946, -9.142685)

    // Create a MarkerState with the position
    val markerState = rememberMarkerState(position = position)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {

            Text(
                text = "Authenticated as ${authRepository.getEmail()}"
            )


            GoogleMap(
                cameraPositionState = cameraPositionState,
                modifier = Modifier.fillMaxSize()
            ) {
                Marker(
                    state = markerState,
                )
            }

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