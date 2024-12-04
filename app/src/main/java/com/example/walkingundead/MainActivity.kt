package com.example.walkingundead

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.walkingundead.navigation.NavGraph
import com.example.walkingundead.ui.theme.WalkingUnDeadTheme

class MainActivity : ComponentActivity() {
    /*
    // define the global variable
    private lateinit var question1: TextView
    // Add button Move to Activity
    private lateinit var next_Activity_button: Button
    */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            WalkingUnDeadTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
                /*
                // by ID we can use each component which id is assign in xml file
                // use findViewById() to get the Button
                next_Activity_button = findViewById(R.id.first_activity_button)
                question1 = findViewById(R.id.question1_id)

                // In question1 get the TextView use by findViewById()
                // In TextView set question Answer for message
                question1.text = "Q1 - How to pass the data between activities in Android? Ans - Intent".trimIndent()

                // Add_button add clicklistener
                next_Activity_button.setOnClickListener {
                    // Intents are objects of the android.content.Intent type. Your code can send them to the Android system defining
                    // the components you are targeting. Intent to start an activity called SecondActivity with the following code.
                    val intent = Intent(this, SecondActivity::class.java)
                    // start the activity connect to the specified class
                    startActivity(intent)
                }

                para composable não me parece dar jeito fazer assim
                */
            }
        }
    }
}

