package com.example.walkingundead.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.walkingundead.screens.Authentication
import com.example.walkingundead.screens.MapScreen

@Composable
fun NavGraph (navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screens.Authentication.route
    )
    {
        composable(route = Screens.Authentication.route){
            Authentication(navController = navController)
        }
        composable(route = Screens.Map.route){
            MapScreen(navController = navController)
        }
    }

}
