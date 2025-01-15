package com.example.walkingundead.models

data class ReportZombie(
    val id: String = "", // Unique ID
    val location: String, // Location as "latitude,longitude"
    var emailRegisteredBy: String = ""
)
