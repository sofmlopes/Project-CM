package com.example.walkingundead.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.walkingundead.screens.Authentication
import com.example.walkingundead.screens.Food
import com.example.walkingundead.screens.Menu
import com.example.walkingundead.screens.Medicine
import com.example.walkingundead.screens.Profiles
import com.example.walkingundead.screens.Shelter
import com.example.walkingundead.screens.SkillsPickerScreen
import com.google.android.gms.maps.model.LatLng

@Composable
fun NavGraph(navController: NavHostController, currentLocation: LatLng?) {

    NavHost(
        navController = navController,
        startDestination = Screens.Menu.route
    )
    {
        composable(route = Screens.Skills.route) {
            SkillsPickerScreen()
        }

        // Define the Menu composable route with arguments in the navigation graph
        composable(
            route = "${Screens.Menu.route}?selectedLat={selectedLat}&selectedLng={selectedLng}",
            arguments = listOf(
                navArgument("selectedLat") { type = NavType.StringType; nullable = true },
                navArgument("selectedLng") { type = NavType.StringType; nullable = true }
            )
        ) { backStackEntry ->
            val selectedMedicineLat = backStackEntry.arguments?.getString("selectedLat")?.toDoubleOrNull()
            val selectedMedicineLng = backStackEntry.arguments?.getString("selectedLng")?.toDoubleOrNull()

            // Parse the selected location if valid, otherwise null
            val selectedMedicineLocation = if (selectedMedicineLat != null && selectedMedicineLng != null) {
                LatLng(selectedMedicineLat, selectedMedicineLng)
            } else null

            Menu(
                currentLocation = currentLocation,
                selectedMedicineLocation = selectedMedicineLocation
            )
        }

        // Define the Medicine composable route
        composable(route = Screens.Medicine.route) {
            Medicine(
                onMedicineSelected = { selectedLocation ->
                    // Navigate to the Menu screen and pass the location as arguments
                    if (selectedLocation != null) {
                        val latitude = Uri.encode(selectedLocation.latitude.toString())
                        val longitude = Uri.encode(selectedLocation.longitude.toString())
                        navController.navigate(
                            "${Screens.Menu.route}?selectedLat=$latitude&selectedLng=$longitude"
                        )
                    }
                }
            )
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

        composable(route = Screens.Authentication.route) {
            Authentication()
        }
    }
}
