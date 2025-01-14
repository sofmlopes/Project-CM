package com.example.walkingundead.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.walkingundead.models.MedicineEntry
import com.example.walkingundead.models.Shelter
import com.example.walkingundead.provider.RepositoryProvider
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun Menu() {

    val database = RepositoryProvider.databaseRepository

    var medicines by remember { mutableStateOf<List<MedicineEntry>>(emptyList()) }
    var shelterList by remember { mutableStateOf<List<Shelter>>(emptyList()) }

    LaunchedEffect(Unit) {
        database.getAllMedicines { fetchedMedicines ->
            medicines = fetchedMedicines
        }
    }
    LaunchedEffect(Unit) {
        database.getAllShelters { fetchedShelters ->
            shelterList = fetchedShelters
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(LatLng(38.736946, -9.142685), 10f)
    }

    Box(
        modifier = androidx.compose.ui.Modifier.fillMaxSize()
    ) {
        Column(
            modifier = androidx.compose.ui.Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GoogleMap(
                cameraPositionState = cameraPositionState,
                modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true
                )
            ) {
                // Add medicine markers
                medicines.forEach { medicine ->
                    val location = parseLocation(medicine.location)
                    location?.let {
                        val markerState = rememberMarkerState(position = it)
                        Marker(
                            state = markerState,
                            title = "Medicine: ${medicine.name}",
                            snippet = "Quantity: ${medicine.quantity}",
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                        )
                    }
                }

                // Add shelter markers
                shelterList.forEach { shelter ->
                    val location = parseLocation(shelter.location)
                    location?.let {
                        val markerState = rememberMarkerState(position = it)
                        Marker(
                            state = markerState,
                            title = "Shelter: ${shelter.name}",
                            snippet = "Capacity: ${shelter.numberOfBeds}",
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                        )
                    }
                }
            }
        }
    }
}

// Helper function to parse location strings into LatLng objects
fun parseLocation(location: String?): LatLng? {
    return try {
        val parts = location!!.split(",")
        val lat = parts[0].toDouble()
        val lng = parts[1].toDouble()
        LatLng(lat, lng)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
