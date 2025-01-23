package com.example.walkingundead.utilities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text

/**
 * Activity that displays a simple text when the screen is loaded.
 */
class AlertDetails : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Text("This is the Alert Details screen!")
        }
    }
}
