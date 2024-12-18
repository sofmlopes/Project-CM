package com.example.walkingundead.models

data class MedicineEntry(
    var name: String? = null,
    var type: String? = null,
    var location: String? = null, //maybe shouldn't be a string but maybe its fine (x, y)
    var quantity: Int = 0
)
