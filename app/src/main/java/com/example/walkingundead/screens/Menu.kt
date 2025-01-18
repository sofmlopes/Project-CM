package com.example.walkingundead.screens

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.walkingundead.R
import com.example.walkingundead.models.Food
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

private const val REQUEST_CALL_PERMISSION = 1

@Composable
fun Menu(currentLocation: LatLng?) {

    val database = RepositoryProvider.databaseRepository
    var medicines by remember { mutableStateOf<List<MedicineEntry>>(emptyList()) }
    var foods by remember { mutableStateOf<List<Food>>(emptyList()) }
    var shelterList by remember { mutableStateOf<List<Shelter>>(emptyList()) }
    //State for Zombie Reports
    var zombieReports by remember { mutableStateOf<List<ReportZombie>>(emptyList()) }
    var isZombiesFiltered by remember { mutableStateOf(false) }
    var isMedicineFiltered by remember { mutableStateOf(false)}
    var isFoodFiltered by remember { mutableStateOf(false) }
    var isShelterFiltered by remember { mutableStateOf(false) }
    // State to control the visibility of the "Report Zombie" dialog
    var openReportDialog by remember { mutableStateOf(false) }
    // State to control the visibility of the "SOS" dialog
    var openSOSDialog by remember { mutableStateOf(false) }
    // State to control the visibility of the "Sound Grenade" dialog
    var openSoundGrenadeDialog by remember { mutableStateOf(false) }
    var isFilterPopupVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        database.getAllMedicines { fetchedMedicines -> medicines = fetchedMedicines }
        database.getAllFoods { fetchedFoods-> foods = fetchedFoods }
        database.getAllShelters { fetchedShelters -> shelterList = fetchedShelters }
        database.getAllZombies { fetchedReports -> zombieReports = fetchedReports }
    }

    val cameraPositionState = rememberCameraPositionState {
        val defaultLatLng = LatLng(38.736946, -9.142685) // Default location
        val location = currentLocation ?: defaultLatLng
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(location, 10f)
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
                    if(isMedicineFiltered){
                        val location = parseLocation(medicine.location)
                        location?.let {
                            val markerState = rememberMarkerState(position = it)
                            // Convert the image resource to Bitmap
                            val bitmap = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.medicine_marker)
                            // Scale the bitmap to a fixed size
                            val scaledBitmap = scaleBitmap(bitmap, 80, 80)  // Adjust the size here
                            Marker(
                                state = markerState,
                                title = "Medicine: ${medicine.name}",
                                snippet = "Quantity: ${medicine.quantity}",
                                icon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)
                            )
                        }
                    }
                }
                // Add food markers
                foods.forEach { food ->
                    if(isFoodFiltered){
                        val location = parseLocation(food.location)
                        location?.let {
                            val markerState = rememberMarkerState(position = it)
                            // Convert the image resource to Bitmap
                            val bitmap = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.food_marker)
                            // Scale the bitmap to a fixed size
                            val scaledBitmap = scaleBitmap(bitmap, 80, 80)  // Adjust the size here
                            Marker(
                                state = markerState,
                                title = "Medicine: ${food.name}",
                                snippet = "Quantity: ${food.quantity}",
                                icon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)
                            )
                        }
                    }
                }
                // Add shelter markers
                shelterList.forEach { shelter ->
                    if(isShelterFiltered){
                        val location = parseLocation(shelter.location)
                        location?.let {
                            val markerState = rememberMarkerState(position = it)
                            // Convert the image resource to Bitmap
                            val bitmap = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.shelter_marker)
                            // Scale the bitmap to a fixed size
                            val scaledBitmap = scaleBitmap(bitmap, 80, 80)  // Adjust the size here
                            Marker(
                                state = markerState,
                                title = "Shelter: ${shelter.name}",
                                snippet = "Capacity: ${shelter.numberOfBeds}",
                                icon = BitmapDescriptorFactory.fromBitmap(bitmap),
                            )
                        }
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
                    if(isZombiesFiltered){
                        val location = parseLocation(report.location)
                        location?.let {
                            val markerState = rememberMarkerState(position = it)
                            // Convert the image resource to Bitmap
                            val bitmap = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.zombie_marker)
                            // Scale the bitmap to a fixed size
                            val scaledBitmap = scaleBitmap(bitmap, 80, 80)  // Adjust the size here
                            Marker(
                                state = markerState,
                                title = "Watch out! A zombie is in your area.",
                                icon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)
                            )
                        }
                    }
                }
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

                        // Set the volume to max for the desired stream
                        audioManager.adjustStreamVolume(
                            AudioManager.STREAM_ALARM,
                            AudioManager.ADJUST_RAISE,
                            AudioManager.FLAG_PLAY_SOUND
                        )

                        val tone = ToneGenerator(AudioManager.STREAM_ALARM, 500) // 100 is max volume
                        tone.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 10000)

                        // Close the dialog
                        openSoundGrenadeDialog = false
                    }
                )
            }
            if (isFilterPopupVisible) {
                Dialog(
                    onDismissRequest = {
                        isFilterPopupVisible = false
                    }
                ) {
                    Card(
                        modifier = Modifier.padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Column(
                            Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Filters",
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            FilterChip(
                                onClick = { isZombiesFiltered = !isZombiesFiltered },
                                label = {
                                    Text("Filter Zombies")
                                },
                                selected = isZombiesFiltered,
                                leadingIcon = if (isZombiesFiltered) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                },
                            )
                            FilterChip(
                                onClick = { isMedicineFiltered = !isMedicineFiltered },
                                label = {
                                    Text("Filter Medicine")
                                },
                                selected = isMedicineFiltered,
                                leadingIcon = if (isMedicineFiltered) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                },
                            )
                            FilterChip(
                                onClick = { isFoodFiltered = !isFoodFiltered },
                                label = {
                                    Text("Filter Food")
                                },
                                selected = isFoodFiltered,
                                leadingIcon = if (isFoodFiltered) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                },
                            )
                            FilterChip(
                                onClick = { isShelterFiltered = !isShelterFiltered },
                                label = {
                                    Text("Filter Shelters")
                                },
                                selected = isShelterFiltered,
                                leadingIcon = if (isShelterFiltered) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                },
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(onClick = { isFilterPopupVisible = false }) {
                                Text("Close")
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Android Developers. (n.d.). Dialogs in Compose. Google. Retrieved January 15, 2025,
 * from https://developer.android.com/develop/ui/compose/components/dialog?hl=pt-br
 */
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
                Text(
                    text = "REPORT ZOMBIE",
                    color = colorResource(id = R.color.purple_500),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp),
                )
                Image(
                    painter = painterResource(id = R.drawable.zombie_marker),
                    contentDescription = "Zombie Image in Report Zombie dialog",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(100.dp)
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
                    //horizontalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { onYes(currentLocation) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("YES")
                    }
                    TextButton(
                        onClick = { onNo() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("NO")
                    }
                }
            }
        }
    }
}

/**
 * Android Developers. (n.d.). Dialogs in Compose. Google. Retrieved January 15, 2025,
 * from https://developer.android.com/develop/ui/compose/components/dialog?hl=pt-br
 */
@Composable
fun SOSDialog(onNo: () -> Unit,  onYes: (Context,String) -> Unit) {

    val context = LocalContext.current
    val emergencyNumber = "934051473"

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
                Text(
                    text = "EMERGENCY CALL",
                    color = colorResource(id = R.color.purple_500),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp),
                )
                Image(
                    painter = painterResource(id = R.drawable.sos_icon),
                    contentDescription = "SOS icon in SOS dialog",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(50.dp)
                )
                Text(
                    text = "Are you sure you want to share \n" +
                            "your location with your \n" +
                            "emergency contacts?",
                    modifier = Modifier.padding(16.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    //horizontalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { onYes(context,emergencyNumber) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("YES")
                    }
                    TextButton(
                        onClick = { onNo() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("NO")
                    }
                }
            }
        }
    }
}

/**
 * Android Developers. (n.d.). Dialogs in Compose. Google. Retrieved January 15, 2025,
 * from https://developer.android.com/develop/ui/compose/components/dialog?hl=pt-br
 */
@Composable
fun SoundGrenadeDialog(onNo: () -> Unit,  onYes: (Context) -> Unit) {

    val context = LocalContext.current

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
                Text(
                    text = "SOUND GRENADE",
                    color = colorResource(id = R.color.purple_500),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.sound_grenade_icon),
                    contentDescription = "Sound Grenade icon in Sound Grenade dialog",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(100.dp)
                )
                Text(
                    text = "Are you sure you want to emit \n" +
                            "a loud sound to distract the \n" +
                            "zombies?",
                    modifier = Modifier.padding(16.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    //horizontalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { onYes(context) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("YES")
                    }
                    TextButton(
                        onClick = { onNo() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("NO")
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

/**
 * Android Developers. (n.d.). Build a notification. Google. Retrieved January 15, 2025,
 * from https://developer.android.com/develop/ui/views/notifications/build-notification?hl=pt-br
 */
fun sendNotificationZombiesInTheArea (channelId: String, channel_name: String, channel_description: String,
                                    context : Context){
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is not in the Support Library.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_HIGH // High importance for heads-up notifications
        val channel = NotificationChannel(channelId, channel_name, importance).apply {
            description = channel_description
        }
        // Register the channel with the system.
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.createNotificationChannel(channel)
    }
    // Create an explicit intent for an Activity in your app.
    val intent = Intent(context, AlertDetails::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.zombie_marker)
        .setContentTitle("Zombie Alert!")
        .setContentText("A zombie has been reported near your location!")
        .setPriority(NotificationCompat.PRIORITY_HIGH) // High priority
        .setDefaults(NotificationCompat.DEFAULT_ALL) // Enable sound and vibration
        // Set the intent that fires when the user taps the notification.
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    // Check notification permission for Android 13+ (API 33+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, send the notification
            NotificationManagerCompat.from(context).notify(1, builder.build())
        } else {
            // Optionally, handle the case where permission is not granted
            Log.w("Notification", "Notification permission not granted.")
        }
    } else {
        // For devices below Android 13, no runtime permission is required
        NotificationManagerCompat.from(context).notify(1, builder.build())
    }
}

/**
 * Function to calculate if a zombie is within range of the user (3 km)
 */
fun isZombieNear(userLocation: LatLng, zombieLocation: LatLng, rangeInMeters: Float): Boolean {
    val location1 = Location("current")
    location1.latitude = userLocation.latitude
    location1.longitude = userLocation.longitude

    val location2 = Location("zombie")
    location2.latitude = zombieLocation.latitude
    location2.longitude = zombieLocation.longitude

    val distance = location1.distanceTo(location2)  // Distance in meters
    return distance <= rangeInMeters
}
