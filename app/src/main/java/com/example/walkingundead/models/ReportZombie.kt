package com.example.walkingundead.models

data class ReportZombie(
    var id: String? = null, // Unique ID
    val location: String? = null, // Location as "latitude,longitude"
    val emailRegisteredBy: String? = null
) {
    // No-argument constructor required by Firebase
    constructor() : this(null, null, null)
}
