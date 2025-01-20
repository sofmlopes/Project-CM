package com.example.walkingundead.models

data class Profile (
    var id: Int? = 0,
    var name: String? = null,
    var email: String? = "",
    var skills: MutableList<Skill> = mutableListOf()
)