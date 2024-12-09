package com.example.walkingundead.provider

import com.example.walkingundead.services.AuthenticationService

object RepositoryProvider {
    val authRepository: AuthenticationService by lazy {
        AuthenticationService()
    }
}