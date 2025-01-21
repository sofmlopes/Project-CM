package com.example.walkingundead.models

data class Profile (
    var id: Int? = 0,
    var email: String? = "",
    var phoneNumber: String? = "",
    var skills: MutableList<Skill> = mutableListOf(),
    var contacts: MutableList<Contact> = mutableListOf(),
)