package com.example.walkingundead.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.walkingundead.R
import com.example.walkingundead.models.Food
import com.example.walkingundead.models.MedicineEntry
import com.example.walkingundead.models.ReportZombie
import com.example.walkingundead.models.Shelter
import com.example.walkingundead.provider.RepositoryProvider
import com.example.walkingundead.utilities.CurrentLocationMarker
import com.example.walkingundead.utilities.FilterPopup
import com.example.walkingundead.utilities.FoodMarkers
import com.example.walkingundead.utilities.MedicineMarkers
import com.example.walkingundead.utilities.ReportZombieDialog
import com.example.walkingundead.utilities.SOSDialog
import com.example.walkingundead.utilities.ShelterMarkers
import com.example.walkingundead.utilities.SoundGrenadeDialog
import com.example.walkingundead.utilities.ZombieMarkers
import com.example.walkingundead.utilities.isZombieNear
import com.example.walkingundead.utilities.parseLocation
import com.example.walkingundead.utilities.sendNotificationZombiesInTheArea
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

private const val REQUEST_CALL_PERMISSION = 1

@Composable
fun Menu(currentLocation: LatLng?, selectedMedicineLocation: LatLng?) {

    val database = RepositoryProvider.databaseRepository

    var medicines by remember { mutableStateOf<List<MedicineEntry>>(emptyList()) }
    var foods by remember { mutableStateOf<List<Food>>(emptyList()) }
    var shelterList by remember { mutableStateOf<List<Shelter>>(emptyList()) }
    //State for Zombie Reports
    var zombieReports by remember { mutableStateOf<List<ReportZombie>>(emptyList()) }
    var isZombiesFiltered by remember { mutableStateOf(true) }
    var isMedicineFiltered by remember { mutableStateOf(true)}
    var isFoodFiltered by remember { mutableStateOf(true) }
    var isShelterFiltered by remember { mutableStateOf(true) }
    // State to control the visibility of the "Report Zombie" dialog
    var openReportDialog by remember { mutableStateOf(false) }
    // State to control the visibility of the "SOS" dialog
    var openSOSDialog by remember { mutableStateOf(false) }
    // State to control the visibility of the "Sound Grenade" dialog
    var openSoundGrenadeDialog by remember { mutableStateOf(false) }
    var isFilterPopupVisible by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState {
        val defaultLatLng = LatLng(38.736946, -9.142685) // Default location
        val location = selectedMedicineLocation ?: currentLocation ?: defaultLatLng
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(location, 10f)
    }

    LaunchedEffect(Unit) {
        database.getAllMedicines { fetchedMedicines -> medicines = fetchedMedicines }
        database.getAllFoods { fetchedFoods-> foods = fetchedFoods }
        database.getAllShelters { fetchedShelters -> shelterList = fetchedShelters }
        database.getAllZombies { fetchedReports -> zombieReports = fetchedReports }
    }

    LaunchedEffect(selectedMedicineLocation) {
        selectedMedicineLocation?.let {
            cameraPositionState.position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(it, 15f)
        }
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
                uiSettings = MapUiSettings(zoomControlsEnabled = true)
            ) {
                MedicineMarkers(medicines = medicines, isMedicineFiltered = isMedicineFiltered)
                FoodMarkers(foods = foods, isFoodFiltered = isFoodFiltered)
                ShelterMarkers(shelterList = shelterList, isShelterFiltered = isShelterFiltered)
                CurrentLocationMarker(currentLocation)
                ZombieMarkers(zombieReports =zombieReports, isZombiesFiltered =isZombiesFiltered)

                zombieReports.forEach { report ->
                    val zombieLocation = parseLocation(report.location)
                    if (zombieLocation != null && currentLocation != null) {
                        if (isZombieNear(currentLocation, zombieLocation, 3000f)) {
                            sendNotificationZombiesInTheArea("zombie_alert_channel", "Zombie Alert",
                                "Channel for zombie proximity alerts", LocalContext.current)
                        }
                    }
                }
            }
        }
        /**
         * Android Developers. (n.d.). Chip. Retrieved January 18, 2025, from
         * https://developer.android.com/develop/ui/compose/components/chip
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(top = 8.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // The main menu UI
            ElevatedButton(onClick = { openReportDialog = true }) {
                Text("Report Zombie")
            }
            // Show the dialog when openReportDialog is true
            if (openReportDialog) {
                if (currentLocation != null) {
                    ReportZombieDialog(
                        currentLocation = currentLocation,
                        onNo = { openReportDialog = false },
                        onYes = { location ->
                            // Create a location string
                            val locationString = "${location.latitude},${location.longitude}"

                            // Save to database using the addNewReportZombie function
                            database.addNewReportZombie(locationString)

                            // Close the dialog
                            openReportDialog = false
                        }
                    )
                }
            }
            // The main menu UI
            ElevatedButton(onClick = { openSOSDialog = true }) {
                Text("SOS")
            }
            // Show the dialog when openReportDialog is true
            // Stack Overflow. (n.d.). How can I call emergency number programmatically?
            // Retrieved January 17, 2025, from
            // https://stackoverflow.com/questions/20770024/how-can-i-call-emergency-number-programmatically
            if (openSOSDialog) {
                SOSDialog(
                    onNo = { openSOSDialog = false },
                    onYes = { context, phoneNumber ->

                        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            // Permission granted, make the call
                            val intent = Intent(Intent.ACTION_CALL).apply {
                                data = Uri.parse("tel:$phoneNumber")
                            }
                            context.startActivity(intent)
                        }
                        else {
                            // Permission not granted, request permission
                            ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_CALL_PERMISSION)
                        }
                        // Close the dialog
                        openSOSDialog = false
                    }
                )
            }
            // The main menu UI
            IconButton(onClick = { isFilterPopupVisible = true }) {
                Icon(imageVector = Icons.Default.FilterList, contentDescription = "Filter")
            }
            ElevatedButton(onClick = { openSoundGrenadeDialog = true }) {
                Text("Sound Grenade")
            }

            // Show the dialog when openReportDialog is true
            /**
             * Stack Overflow. (n.d.). How to play a loud alert sound (beep) in Android?
             * Retrieved January 16, 2025, from
             * https://stackoverflow.com/questions/33159723/how-to-play-a-loud-alert-sound-beep-in-android
             */
            if (openSoundGrenadeDialog) {
                SoundGrenadeDialog(
                    onNo = { openSoundGrenadeDialog = false },
                    onYes = { context ->

                        // Get AudioManager instance
                        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

                        // Set the alarm stream volume to maximum
                        audioManager.setStreamVolume(
                            AudioManager.STREAM_ALARM,
                            audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM),
                            AudioManager.FLAG_PLAY_SOUND
                        )

                        // Create a new MediaPlayer instance for each playback
                        val mediaPlayer = MediaPlayer.create(context, R.raw.loud_emergency_alarm)

                        // Start playing the sound
                        mediaPlayer.start()

                        // Release MediaPlayer resources once the playback is complete
                        mediaPlayer.setOnCompletionListener {
                            mediaPlayer.release()  // Release the media player after it's done
                        }

                        // Close the dialog
                        openSoundGrenadeDialog = false
                    }
                )
            }
            if (isFilterPopupVisible) {
                FilterPopup(
                    isZombiesFiltered = isZombiesFiltered,
                    isMedicineFiltered = isMedicineFiltered,
                    isFoodFiltered = isFoodFiltered,
                    isShelterFiltered = isShelterFiltered,
                    onZombiesFilterToggle = { isZombiesFiltered = !isZombiesFiltered },
                    onMedicineFilterToggle = { isMedicineFiltered = !isMedicineFiltered },
                    onFoodFilterToggle = { isFoodFiltered = !isFoodFiltered },
                    onShelterFilterToggle = { isShelterFiltered = !isShelterFiltered },
                    onClose = { isFilterPopupVisible = false }
                )
            }
        }
    }
}





