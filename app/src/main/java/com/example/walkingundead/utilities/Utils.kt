package com.example.walkingundead.utilities

import android.content.Context
import android.graphics.Bitmap
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.walkingundead.provider.RepositoryProvider
import com.google.android.gms.maps.model.LatLng
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

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

/**
 * Function to scale the bitmap to a fixed size
 */
fun scaleBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
    return Bitmap.createScaledBitmap(bitmap, width, height, false)
}

/**
 * Helper function to parse location strings into LatLng objects
 */
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

fun getAddressFromCoordinates(
    context: Context,
    lat: Double,
    lng: Double
): String? {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lng, 1)
        if (!addresses.isNullOrEmpty()) {
            addresses[0].getAddressLine(0)
        } else {
            null
        }
    } catch (e: Exception) {
        Log.e("GeocoderError", "Error retrieving address", e)
        null
    }
}

@Composable
fun WalkingUndeadLogo() {
    Row {
        Text(
            text = "Walking ",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
        )

        Text(
            text = "Un",
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
        )

        Text(
            text = "Dead",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
        )
    }
}

/**
 * Calculates the distance using the Haversine formula
 */
fun distanceToCurrentLocation(location: String?): Int? {
    // Check if the location string is null or empty
    if (location.isNullOrEmpty()) return null

    // Parse the latitude and longitude from the location string
    val latLng = location.split(",").mapNotNull { it.toDoubleOrNull() }
    if (latLng.size != 2) return null // Ensure the location string contains both latitude and longitude

    val currentLocation = RepositoryProvider.locationService.currentLocation ?: return null

    val targetLocation = LatLng(latLng[0], latLng[1])

    // Calculate the distance in meters using the Haversine formula
    val earthRadius = 6371000.0 // Earth radius in meters

    val dLat = Math.toRadians(targetLocation.latitude - currentLocation.latitude)
    val dLng = Math.toRadians(targetLocation.longitude - currentLocation.longitude)

    val a = sin(dLat / 2).pow(2) +
            cos(Math.toRadians(currentLocation.latitude)) * cos(Math.toRadians(targetLocation.latitude)) *
            sin(dLng / 2).pow(2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    val distance = earthRadius * c

    return distance.toInt()
}