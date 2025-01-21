package com.example.walkingundead.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.walkingundead.R
import com.example.walkingundead.models.Contact
import com.example.walkingundead.models.MedicineEntry
import com.example.walkingundead.models.Skill
import com.example.walkingundead.navigation.Screens
import com.example.walkingundead.provider.RepositoryProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
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
    var selectedSkillsList by remember { mutableStateOf<List<Skill>>(emptyList()) }
    var isContactPopupVisible by remember { mutableStateOf(false) }
    val database = RepositoryProvider.databaseRepository

    LaunchedEffect(Unit) {
        database.getProfileSkills(authRepository.getEmail()) { fetchedSkills ->
            selectedSkillsList = fetchedSkills
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            //If logged in just show email and option to logout
            if (authenticated) {

                Image(
                    painter = painterResource(id = R.drawable.default_user),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(60.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(15.dp))

                val robotoFamily = FontFamily.Default

                Text(
                    "Authenticated as ${authRepository.getEmail()}",
                    style = TextStyle(
                        fontFamily = robotoFamily, // Example font family (you can use custom fonts)
                        fontWeight = FontWeight.Bold,  // Bold text
                        fontSize = 20.sp,              // Font size
                        letterSpacing = 1.5.sp,        // Letter spacing (spacing between characters)
                        color = Color.Black           // Text color
                    )
                )

                Spacer(Modifier.height(25.dp))

                // Display selected skills
                Text(
                    text = "Selected Skills: ${selectedSkillsList.joinToString(", ") { it.name ?: "Unnamed" }}",
                    style = TextStyle(fontSize = 16.sp)
                )

                Spacer(Modifier.height(15.dp))

                // Button to open the skill picker screen
                Button(
                    shape = RoundedCornerShape(6.dp),
                    onClick = {
                        // Logic to navigate to SkillsPickerScreen if necessary
                        // In case you're not navigating, you can make it a modal dialog or inline screen
                        navController.navigate(Screens.Skills.route)
                    }
                ) {
                    Text("Change Skills")
                }

                // Button to open the skill picker screen
                Button(
                    shape = RoundedCornerShape(6.dp),
                    onClick = {
                        isContactPopupVisible = true
                    }
                ) {
                    Text("Change Emergency Contacts")
                }

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

            } else {

                // Show login form when not authenticated
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
                                    database.addNewProfileEntry(
                                        name = "nsei",
                                        email = email,
                                        skills = mutableListOf()
                                    )

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
        }

        // Dialog to manage contacts
        if (isContactPopupVisible) {
            Dialog(onDismissRequest = { isContactPopupVisible = false }) {
                var newContactName by remember { mutableStateOf("") }
                var newContactNumber by remember { mutableStateOf("") }
                var contactsList by remember { mutableStateOf<List<Contact>>(emptyList()) }


                LaunchedEffect(Unit) {
                    database.getProfileContacts(authRepository.getEmail()) { fetchedContacts ->
                        contactsList = fetchedContacts
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            "Manage Emergency Contacts",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(10.dp))

                        // List of contacts
                        if (contactsList.isEmpty()) {
                            Text("No contacts available", color = Color.Gray)
                        } else {
                            contactsList.forEach { contact ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(contact.name ?: "Unknown", fontWeight = FontWeight.Bold)
                                        Text(contact.number ?: "No number")
                                    }
                                    IconButton(onClick = {
                                        // todo Remove contact
                                    }) {
                                        Icon(Icons.Default.Lock, contentDescription = "Remove contact")
                                    }
                                    IconButton(onClick = {
                                        // todo Edit contact
                                        newContactName = contact.name ?: ""
                                        newContactNumber = contact.number ?: ""
                                    }) {
                                        Icon(Icons.Default.Lock, contentDescription = "Edit contact")
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        // Input fields to add a new contact
                        TextField(
                            value = newContactName,
                            onValueChange = { newContactName = it },
                            label = { Text("Name") },
                            placeholder = { Text("Enter contact name") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        TextField(
                            value = newContactNumber,
                            onValueChange = { newContactNumber = it },
                            label = { Text("Number") },
                            placeholder = { Text("Enter contact number") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(10.dp))

                        Button(onClick = {
                            database.addContactToProfile(
                                authRepository.getEmail(),
                                Contact(name = newContactName, number = newContactNumber),
                            )
                        }) {
                            Text("Add Contact")
                        }
                    }
                }
            }
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
