package com.example.walkingundead.navigation

sealed class Screens(val route: String) {
    data object Skills: Screens("skills_screen")
    data object Menu: Screens("menu_screen")
    data object Medicine: Screens("medicine_screen")
    data object Food: Screens("food_screen")
    data object Shelter: Screens("shelter_screen")
    data object Profiles: Screens("profiles_screen")
}