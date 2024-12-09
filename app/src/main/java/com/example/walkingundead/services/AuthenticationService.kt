package com.example.walkingundead.services

import com.example.walkingundead.utilities.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthenticationService() {

    var email: String? = null
    var authtenticated = false

    suspend fun signIn(email: String, password: String): AuthResult {

        return try {

            val authResult = Firebase.auth.signInWithEmailAndPassword(email, password).await()
            this.email = email
            authtenticated = true
            AuthResult.SUCCESS

        } catch (e: Exception) {
            AuthResult.FAILED
        }
    }

    fun getAuthState(): Boolean {
        return authtenticated
    }

}