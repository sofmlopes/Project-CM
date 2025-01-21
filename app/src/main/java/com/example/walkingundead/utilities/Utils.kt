package com.example.walkingundead.utilities

import android.content.Context
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

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

// Function to scale the bitmap to a fixed size
fun scaleBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
    return Bitmap.createScaledBitmap(bitmap, width, height, false)
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