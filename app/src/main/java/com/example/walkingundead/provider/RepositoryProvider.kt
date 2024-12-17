package com.example.walkingundead.provider

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.walkingundead.navigation.NavGraph
import com.example.walkingundead.services.AuthenticationService
import com.example.walkingundead.services.DatabaseService
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

object RepositoryProvider {
    private var _navController: NavHostController? = null

    val navController: NavHostController
        get() = _navController ?: throw IllegalStateException("NavController is not initialized")

    fun setNavController(navController: NavHostController) {
        _navController = navController
    }

    val authRepository: AuthenticationService by lazy {
        AuthenticationService()
    }

    private var databaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()

    val databaseRepository: DatabaseService by lazy {
        DatabaseService(databaseReference)
    }

}