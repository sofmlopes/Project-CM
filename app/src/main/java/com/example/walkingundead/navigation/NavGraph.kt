package com.example.walkingundead.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.walkingundead.provider.RepositoryProvider
import com.example.walkingundead.screens.Authentication
import com.example.walkingundead.screens.Food
import com.example.walkingundead.screens.Menu
import com.example.walkingundead.screens.Medicine
import com.example.walkingundead.screens.Profiles
import com.example.walkingundead.screens.Shelter
import com.example.walkingundead.screens.SkillsPickerScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screens.Authentication.route
    )
    {
        composable(route = Screens.Authentication.route) {
            Authentication()
        }

        composable(route = Screens.Skills.route) {
            SkillsPickerScreen()
        }

        composable(route = Screens.Menu.route) {
            Menu()
        }

        composable(route = Screens.Medicine.route) {
            Medicine()
        }

        composable(route = Screens.Food.route) {
            Food()
        }

        composable(route = Screens.Shelter.route) {
            Shelter()
        }

        composable(route = Screens.Profiles.route) {
            Profiles()
        }
    }
}
