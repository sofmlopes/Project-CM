package com.example.walkingundead.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.walkingundead.R
import com.example.walkingundead.models.MedicineEntry
import com.example.walkingundead.models.ReportZombie
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
fun Menu(currentLocation: LatLng?) {

    val database = RepositoryProvider.databaseRepository

    var medicines by remember { mutableStateOf<List<MedicineEntry>>(emptyList()) }
    var shelterList by remember { mutableStateOf<List<Shelter>>(emptyList()) }
    //State for Zombie Reports
    var zombieReports by remember { mutableStateOf<List<ReportZombie>>(emptyList()) }

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
    LaunchedEffect(Unit) {
        database.getAllZombies { fetchedReports ->
            zombieReports = fetchedReports
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(LatLng(38.736946, -9.142685), 10f)
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp), // Reserve space for the BottomBar
        verticalArrangement = Arrangement.SpaceBetween, // Adjust space
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            GoogleMap(
                cameraPositionState = cameraPositionState,
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

                // Add current location marker
                currentLocation?.let {
                    Marker(
                        state = rememberMarkerState(position = it),
                        title = "You are here",
                        snippet = "Current location",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                    )
                }

                //Show zombie markers on the Map
                zombieReports.forEach { report ->
                    val location = parseLocation(report.location)
                    location?.let {
                        val markerState = rememberMarkerState(position = it)
                        // Convert the image resource to Bitmap
                        val bitmap = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.zombie_marker)
                        // Scale the bitmap to a fixed size
                        val scaledBitmap = scaleBitmap(bitmap, 80, 80)  // Adjust the size here
                        Marker(
                            state = markerState,
                            title = "Report Zombie",
                            icon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(top = 8.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // State to control the visibility of the "Report Zombie" dialog
            val openReportDialog = remember { mutableStateOf(false) }

            // The main menu UI
            ElevatedButton(onClick = { openReportDialog.value = true }) {
                Text("Report Zombie")
            }

            // Show the dialog when openReportDialog is true
            if (openReportDialog.value) {
                if (currentLocation != null) {
                    ReportZombieDialog(
                        currentLocation = currentLocation,
                        onNo = { openReportDialog.value = false },
                        onYes = { location ->
                            // Create a location string
                            val locationString = "${location.latitude},${location.longitude}"

                            // Save to database using the addNewReportZombie function
                            database.addNewReportZombie(locationString)

                            // Close the dialog
                            openReportDialog.value = false
                        }
                    )
                }
            }

            ElevatedButton(onClick = {}) {
                Text("SOS")
            }

            ElevatedButton(onClick = {}) {
                Text("Sound Grenade")
            }
        }
    }
}

@Composable
fun ReportZombieDialog(currentLocation: LatLng, onNo: () -> Unit,  onYes: (LatLng) -> Unit) {

    Dialog(onDismissRequest = onNo) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.zombie_marker),
                    contentDescription = "Zombie Image in Report Zombie dialog",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(160.dp)
                )
                Text(
                    text = "Are you sure you want to \n" +
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
                        onClick = { onYes(currentLocation) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Yes")
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

// Function to scale the bitmap to a fixed size
fun scaleBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
    return Bitmap.createScaledBitmap(bitmap, width, height, false)
}
