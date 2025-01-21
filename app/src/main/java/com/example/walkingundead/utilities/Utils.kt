package com.example.walkingundead.utilities

import android.graphics.Bitmap
import android.location.Location
import com.google.android.gms.maps.model.LatLng

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