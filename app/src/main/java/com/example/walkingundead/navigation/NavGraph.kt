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
import com.example.walkingundead.screens.Map
import com.example.walkingundead.screens.Medicine

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

        composable(route = Screens.Map.route) {
            Map()
        }

        composable(route = Screens.Medicine.route) {
            Medicine()
        }

        composable(route = Screens.Food.route) {
            Food()
        }
    }

}

@Composable
fun CustomScaffold(
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    val navController = remember { RepositoryProvider.navController }
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    val selectedItem = remember { mutableStateOf(currentRoute ?: Screens.Map.route) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Black,
                contentColor = Color.White,
                tonalElevation = 4.dp
            ) {
                Spacer(modifier = Modifier.weight(1f))

                val icons = listOf(
                    Screens.Authentication to Icons.Default.Home,
                    Screens.Map to Icons.Default.LocationOn,
                    Screens.Medicine to Icons.Default.ShoppingCart,
                    Screens.Food to Icons.Default.ThumbUp
                )

                icons.forEach { (screen, icon) ->
                    IconButton(onClick = {
                        selectedItem.value = screen.route
                        navController.navigate(screen.route) {
                            popUpTo(screen.route) { inclusive = false }
                        }
                    }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = screen.route,
                            modifier = Modifier.size(26.dp),
                            tint = if (selectedItem.value == screen.route) Color.White else Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}
