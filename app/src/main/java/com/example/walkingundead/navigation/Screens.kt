package com.example.walkingundead.navigation

sealed class Screens(val route: String) {
    data object Authentication: Screens("authentication_screen")
    data object Map: Screens("map_screen")
    data object Medicine: Screens("medicine_screen")
    data object Food: Screens("food_screen")
}