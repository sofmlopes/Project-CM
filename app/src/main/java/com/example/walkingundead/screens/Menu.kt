package com.example.walkingundead.screens

import android.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            //modifier = androidx.compose.ui.Modifier.fillMaxSize()
            modifier = Modifier.weight(1f), // Ensures the map takes up available space
            contentAlignment = Alignment.Center
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // State to control the visibility of the "Report Zombie" dialog
            val openReportDialog = remember { mutableStateOf(false) }

            // The main menu UI
            ElevatedButton(onClick = { openReportDialog.value = true }) {
                Text("Report Zombie")
            }

            // Show the dialog when openReportDialog is true
            if (openReportDialog.value) {
                ReportZombieDialog(
                    onNo = { openReportDialog.value = false },
                    onYes = {
                        // Add your "Report Zombie" action logic here
                        println("Zombies reported!")
                        openReportDialog.value = false
                    }
                )
            }

            ElevatedButton(onClick = { onClick() }) {
                Text("SOS")
            }
            ElevatedButton(onClick = { onClick() }) {
                Text("Sound Grenade")
            }
        }
    }
}

fun onClick() {
    TODO("Not yet implemented")
}

@Composable
fun ReportZombieDialog(onNo: () -> Unit, onYes: () -> Unit,) {

    Dialog(onDismissRequest = onNo) {

        Text(
            text = " Are you sure you want to \n" +
                    "report Zombies in your current \n" +
                    "location (3km range)?",
            modifier = Modifier.padding(16.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            TextButton(
                onClick = { onNo() },
                modifier = Modifier.padding(8.dp),
            ) {
                Text("No")
            }
            TextButton(
                onClick = { onYes() },
                modifier = Modifier.padding(8.dp),
            ) {
                Text("Yes")
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
