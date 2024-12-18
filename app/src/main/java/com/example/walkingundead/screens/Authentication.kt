package com.example.walkingundead.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.walkingundead.R
import com.example.walkingundead.navigation.Screens
import com.example.walkingundead.provider.RepositoryProvider
import kotlinx.coroutines.launch

@Composable
fun Authentication() {
    val authRepository = remember { RepositoryProvider.authRepository }
    val navController = remember { RepositoryProvider.navController }

    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var authenticated by remember { mutableStateOf(authRepository.isAuthenticated()) }

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

            WalkingUndeadLogo()

            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier.padding(horizontal = 50.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.zombie),
                    contentDescription = "Cute zombie",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(10.dp))

            //If logged in just show email and option to logout
            if (authenticated) {
                Text("Authenticated as ${authRepository.getEmail()}")

                Spacer(Modifier.height(25.dp))

                Button(
                    shape = RoundedCornerShape(6.dp),
                    onClick = {
                        // Launch the coroutine when the button is clicked
                        scope.launch {
                            try {
                                authRepository.logOut()
                                authenticated = false

                            } catch (e: Exception) {

                            }
                        }
                    }
                ) {
                    Text("Log Out")
                }

                Button(
                    shape = RoundedCornerShape(6.dp),
                    onClick = {
                        navController.navigate(Screens.Menu.route)
                    }
                ) {
                    Text("Go to Map")
                }

            } else {
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

                Row {
                    Button(
                        shape = RoundedCornerShape(6.dp),
                        onClick = {
                            // Launch the coroutine when the button is clicked
                            scope.launch {
                                try {
                                    authRepository.signIn(email, password)

                                    if (authRepository.isAuthenticated()) {
                                        authenticated = true
                                        navController.navigate(Screens.Menu.route)
                                    } else {
                                        authenticated = false
                                    }

                                } catch (e: Exception) {

                                }
                            }
                        }
                    ) {
                        Text("Login")
                    }

                    Spacer(Modifier.width(20.dp))

                    Button(
                        shape = RoundedCornerShape(6.dp),
                        onClick = {
                            // Launch the coroutine when the button is clicked
                            scope.launch {
                                try {
                                    authRepository.register(email, password)

                                    if (authRepository.isAuthenticated()) {
                                        authenticated = true
                                    } else {
                                        authenticated = false
                                    }

                                } catch (e: Exception) {

                                }
                            }
                        }
                    ) {
                        Text("Register")
                    }
                }
            }

            Spacer(Modifier.height(50.dp))

            /*
            if (authenticated) {
                Text(
                    text = "Authenticated successfully! 😊"
                )
            } else {
                Text(
                    text = "Authentication failed 🥲"
                )
            }
            */
        }
    }
}

@Composable
fun WalkingUndeadLogo() {
    Row {
        Text(
            text = "Walking ",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
        )

        Text(
            text = "Un",
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
        )

        Text(
            text = "Dead",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
        )
    }
}
