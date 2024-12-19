package com.example.walkingundead.models

data class Shelter(
    var id: String? = "",
    var name: String? = null,
    var location: String? = "",
    var numberOfBeds: Int = 0,
    var occupiedBeds: Int = 0,
    var emailRegisteredBy: String = "",
)