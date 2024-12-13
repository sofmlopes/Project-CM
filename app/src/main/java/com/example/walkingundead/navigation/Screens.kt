package com.example.walkingundead.navigation

sealed class Screens(val route: String) {
    data object Authentication: Screens("authentication_screen")
    data object Map: Screens("map_screen")
}