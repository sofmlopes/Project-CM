package com.example.walkingundead.utilities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.google.android.gms.maps.model.LatLng

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
fun SOSDialog(onNo: () -> Unit,  onYes: (Context, String) -> Unit) {

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


