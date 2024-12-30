package com.example.walkingundead

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.walkingundead.navigation.NavGraph
import com.example.walkingundead.navigation.Screens
import com.example.walkingundead.provider.RepositoryProvider
import com.example.walkingundead.screens.Authentication
import com.example.walkingundead.ui.theme.WalkingUnDeadTheme
import androidx.compose.material3.Icon
import com.google.firebase.auth.ktx.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            WalkingUnDeadTheme {
                val authRepository = remember { RepositoryProvider.authRepository }
                val navController = rememberNavController()
                RepositoryProvider.setNavController(navController)

                if (authRepository.isAuthenticated()) {
                    CustomScaffold(
                        content = {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                ) {
                                    Icon(
                                        Icons.Default.AccountCircle,
                                        contentDescription = "Profile Icon",
                                        modifier = Modifier.size(36.dp).clickable {
                                            // Navigate to the Authentication screen when clicked
                                             navController.navigate(Screens.Authentication.route) {
                                                 // Prevent the profile screen from being added multiple times in the backstack
                                                    popUpTo(Screens.Menu.route) { inclusive = false }
                                                }
                                            }
                                    )
                                }
                                NavGraph(navController = navController)
                            }
                        }
                    )
                } else {
                    Authentication()
                }
            }
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
    val selectedItem = remember { mutableStateOf(currentRoute ?: Screens.Menu.route) }

    Scaffold(
        modifier = modifier.padding(top = 20.dp),
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Black,
                contentColor = Color.White,
                tonalElevation = 4.dp
            ) {
                Spacer(modifier = Modifier.weight(1f))

                val icons = listOf(
                    Screens.Menu to "Menu" to Icons.Default.Menu,
                    Screens.Medicine to "Medicine" to Icons.Default.MedicalServices,
                    Screens.Food to "Food" to Icons.Default.FoodBank,
                    Screens.Shelter to "Shelter" to Icons.Default.Home,
                    Screens.Profiles to "Profiles" to Icons.Default.Groups
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    icons.forEach { (pair, icon) ->
                        val (screen, label) = pair
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedItem.value = screen.route
                                    navController.navigate(screen.route) {
                                        popUpTo(screen.route) { inclusive = false }
                                    }
                                }
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                modifier = Modifier.size(26.dp),
                                tint = if (selectedItem.value == screen.route) Color.White else Color.Gray
                            )
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (selectedItem.value == screen.route) Color.White else Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}
