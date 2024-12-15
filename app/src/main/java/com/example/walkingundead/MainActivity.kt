package com.example.walkingundead

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.walkingundead.navigation.CustomScaffold
import com.example.walkingundead.navigation.NavGraph
import com.example.walkingundead.provider.RepositoryProvider
import com.example.walkingundead.ui.theme.WalkingUnDeadTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            WalkingUnDeadTheme {
                val navController = rememberNavController()
                RepositoryProvider.setNavController(navController)

                CustomScaffold(
                    content = {
                        NavGraph(navController = navController)
                    }
                )
            }
        }
    }
}
