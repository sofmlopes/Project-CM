package com.example.walkingundead.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.walkingundead.navigation.Screens
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun Authentication(navController: NavController) {
    val scope =
        rememberCoroutineScope()  // This gives us a CoroutineScope tied to the Composable lifecycle

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var authenticationResult by remember { mutableStateOf("Not authenticated") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            TextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                placeholder = { Text("Enter your email") }
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password") },
                placeholder = { Text("Enter your password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.Lock else Icons.Filled.Search
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = description)
                    }
                }
            )

            Spacer(Modifier.height(25.dp))

            Button(
                onClick = {
                    // Launch the coroutine when the button is clicked
                    scope.launch {
                        try {
                            // Make sure to replace 'email' and 'password' with actual values
                            // val email = "miguelrmsilva@gmail.com"
                            // val password = "123123123"
                            val authResult =
                                Firebase.auth.signInWithEmailAndPassword(email, password).await()

                            // You can now use the authResult or navigate
                            authenticationResult = "Authenticated successfully! 😊"
                            println("Authentication successful: ${authResult.user?.email}")
                        } catch (e: Exception) {
                            // Handle any authentication errors here
                            println("Authentication failed: ${e.message}")
                        }
                    }
                }
            ) {
                Text("Login")
            }

            Spacer(Modifier.height(50.dp))

            Text(
                text = authenticationResult
            )

            Spacer(Modifier.height(50.dp))

            Button(
                onClick = {
                    navController.navigate(route = Screens.Map.route)
                }
            ) {
                Text("GO TO MAP WOOO")
            }

        }
    }
}
