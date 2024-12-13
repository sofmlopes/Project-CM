package com.example.walkingundead.provider

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.walkingundead.navigation.NavGraph
import com.example.walkingundead.services.AuthenticationService

object RepositoryProvider {
    private var _navController: NavHostController? = null

    val authRepository: AuthenticationService by lazy {
        AuthenticationService()
    }

    val navController: NavHostController
        get() = _navController ?: throw IllegalStateException("NavController is not initialized")

    fun setNavController(navController: NavHostController) {
        _navController = navController
    }
}