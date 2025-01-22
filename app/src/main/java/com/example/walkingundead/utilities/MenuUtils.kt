package com.example.walkingundead.utilities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.walkingundead.R
import com.example.walkingundead.models.Contact
import com.example.walkingundead.models.Food
import com.example.walkingundead.models.MedicineEntry
import com.example.walkingundead.models.ReportZombie
import com.example.walkingundead.models.Shelter
import com.example.walkingundead.provider.RepositoryProvider
import com.example.walkingundead.provider.RepositoryProvider.authRepository
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState

/**
 * Android Developers. (n.d.). Dialogs in Compose. Google. Retrieved January 15, 2025,
 * from https://developer.android.com/develop/ui/compose/components/dialog?hl=pt-br
 */
@Composable
fun ReportZombieDialog(currentLocation: LatLng, onNo: () -> Unit, onYes: (LatLng) -> Unit) {

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
fun SOSDialog(onNo: () -> Unit, onYes: () -> Unit) {

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
                        onClick = { onYes() },
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

@Composable
fun ChooseNumber(onCancel: () -> Unit, onCall: (Context, String) -> Unit) {
    val database = RepositoryProvider.databaseRepository
    val authRepository = RepositoryProvider.authRepository

    var contactsList by remember { mutableStateOf<List<Contact>>(emptyList()) }

    LaunchedEffect(Unit) {
        database.getProfileContacts(authRepository.getEmail()) { fetchedContacts ->
            contactsList = fetchedContacts
        }
    }

    Dialog(onDismissRequest = onCancel) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                // Title
                Text(
                    text = "EMERGENCY CALL",
                    color = colorResource(id = R.color.purple_500),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                // Subtitle
                Text(
                    text = "Choose the phone number to call",
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                // Scrollable list of contacts
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 8.dp),
                ) {
                    contactsList.forEach { contact ->
                        SOSContactItem(
                            contact = contact,
                            onCall = { context, phoneNumber ->
                                onCall(context, phoneNumber)
                            }
                        )
                    }
                }

                // Cancel button at the bottom
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = onCancel,
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("CANCEL")
                    }
                }
            }
        }
    }
}


@Composable
fun SOSContactItem(contact: Contact, onCall: (Context, String) -> Unit) {
    val database = RepositoryProvider.databaseRepository
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(contact.name ?: "Unknown", fontWeight = FontWeight.Bold)
            Text(contact.number ?: "No number")
        }
        Button(onClick = {onCall(context, contact.number?: "")}) {
            Row {
                Text("Call")
                Icon(Icons.Default.Call, "Call")
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

/**
 * Android Developers. (n.d.). Build a notification. Google. Retrieved January 15, 2025,
 * from https://developer.android.com/develop/ui/views/notifications/build-notification?hl=pt-br
 */
fun sendNotificationZombiesInTheArea (channelId: String, channel_name: String, channel_description: String,
                                      context : Context
){
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

@Composable
fun ZombieMarkers(zombieReports: List<ReportZombie>, isZombiesFiltered: Boolean) {
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
}

@Composable
fun CurrentLocationMarker(currentLocation: LatLng?) {
    // Add current location marker
    currentLocation?.let {
        Marker(
            state = rememberMarkerState(position = it),
            title = "You are here",
            snippet = "Current location",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        )
    }
}

@Composable
fun MedicineMarkers(
    medicines: List<MedicineEntry>,
    isMedicineFiltered: Boolean
) {
    medicines.forEach { medicine ->
        if (isMedicineFiltered) {
            val location = parseLocation(medicine.location)
            location?.let {
                val markerState = rememberMarkerState(position = it)
                val bitmap = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.medicine_marker)
                val scaledBitmap = scaleBitmap(bitmap, 80, 80)
                Marker(
                    state = markerState,
                    title = "Medicine: ${medicine.name}",
                    snippet = "Quantity: ${medicine.quantity}",
                    icon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)
                )
            }
        }
    }
}

@Composable
fun FoodMarkers(
    foods: List<Food>,
    isFoodFiltered: Boolean
) {
    foods.forEach { food ->
        if (isFoodFiltered) {
            val location = parseLocation(food.location)
            location?.let {
                val markerState = rememberMarkerState(position = it)
                val bitmap = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.food_marker)
                val scaledBitmap = scaleBitmap(bitmap, 80, 80)
                Marker(
                    state = markerState,
                    title = "Food: ${food.name}",
                    snippet = "Quantity: ${food.quantity}",
                    icon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)
                )
            }
        }
    }
}

@Composable
fun ShelterMarkers(shelterList: List<Shelter>, isShelterFiltered: Boolean){
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
                    icon = BitmapDescriptorFactory.fromBitmap(scaledBitmap),
                )
            }
        }
    }
}

@Composable
fun FilterPopup(
    isZombiesFiltered: Boolean,
    isMedicineFiltered: Boolean,
    isFoodFiltered: Boolean,
    isShelterFiltered: Boolean,
    onZombiesFilterToggle: () -> Unit,
    onMedicineFilterToggle: () -> Unit,
    onFoodFilterToggle: () -> Unit,
    onShelterFilterToggle: () -> Unit,
    onClose: () -> Unit
) {
    Dialog(
        onDismissRequest = onClose
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
                    onClick = onZombiesFilterToggle,
                    label = { Text("Show Zombies") },
                    selected = isZombiesFiltered,
                    leadingIcon = if (isZombiesFiltered) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null
                )
                FilterChip(
                    onClick = onMedicineFilterToggle,
                    label = { Text("Show Medicine") },
                    selected = isMedicineFiltered,
                    leadingIcon = if (isMedicineFiltered) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null
                )
                FilterChip(
                    onClick = onFoodFilterToggle,
                    label = { Text("Show Food") },
                    selected = isFoodFiltered,
                    leadingIcon = if (isFoodFiltered) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null
                )
                FilterChip(
                    onClick = onShelterFilterToggle,
                    label = { Text("Show Shelters") },
                    selected = isShelterFiltered,
                    leadingIcon = if (isShelterFiltered) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onClose) {
                    Text("Close")
                }
            }
        }
    }
}


