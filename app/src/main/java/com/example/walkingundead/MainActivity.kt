package com.example.walkingundead

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.walkingundead.navigation.NavGraph
import com.example.walkingundead.navigation.Screens
import com.example.walkingundead.provider.RepositoryProvider
import com.example.walkingundead.screens.MapScreen
import com.example.walkingundead.ui.theme.WalkingUnDeadTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            WalkingUnDeadTheme {
                val navController = rememberNavController()
                RepositoryProvider.setNavController(navController)

                NavGraph(navController = navController)
            }
        }
    }
}


/*

@Composable
fun OurBottomAppBar() {
    val navigationController = rememberNavController()
    val context = LocalContext.current.applicationContext
    val selected = remember {
        mutableStateOf(Icons.Default.Home)
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Green
            ) {
                IconButton(onClick = {
                    selected.value = Icons.Default.Home
                    navigationController.navigate(Screens.Authentication.route) {
                        popUpToId.dec()
                    }
                }) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.Home) Color.White else Color.DarkGray
                    )
                }

                IconButton(onClick = {
                    selected.value = Icons.Default.LocationOn
                    navigationController.navigate(Screens.Map.route) {
                        popUpToId.dec()
                    }
                }) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.LocationOn) Color.White else Color.DarkGray
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                )
                {
                    FloatingActionButton(onClick = { Toast.makeText(context, "Open Bottom Sheet", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.Green)
                    }

                }
            }
        }
    ) { paddingValues ->
        NavHost(navController = navigationController,
            startDestination = Screens.Map.route,
            modifier = Modifier.padding(paddingValues))
        {
            composable(Screens.Map.route) { MapScreen() }
        }
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    WalkingUnDeadTheme {
        OurBottomAppBar()
    }
}
*/

