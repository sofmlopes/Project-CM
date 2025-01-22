package com.example.walkingundead.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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

        composable(Screens.Menu.route) { backStackEntry ->
            val selectedMedicineLat = backStackEntry.arguments?.getString("selectedLat")?.toDoubleOrNull()
            val selectedMedicineLng = backStackEntry.arguments?.getString("selectedLng")?.toDoubleOrNull()
            val selectedMedicineLocation = if (selectedMedicineLat != null && selectedMedicineLng != null) {
                LatLng(selectedMedicineLat, selectedMedicineLng)
            } else null

            Menu(
                currentLocation = currentLocation,
                selectedMedicineLocation = selectedMedicineLocation
            )
        }

        composable(route = Screens.Medicine.route) {
            Medicine(
                onMedicineSelected = { selectedLocation ->
                    // Navigate to the Menu screen and pass the location as arguments
                    if (selectedLocation != null) {
                        navController.navigate(
                            "${Screens.Menu.route}?selectedLat=${selectedLocation.latitude}&selectedLng=${selectedLocation.longitude}"
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
