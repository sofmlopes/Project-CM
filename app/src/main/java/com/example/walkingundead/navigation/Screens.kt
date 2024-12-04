package com.example.walkingundead.navigation

sealed class Screens(val route: String) {
    object Authentication: Screens("authentication_screen")
    object Map: Screens("map_screen")
}