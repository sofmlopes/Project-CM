package com.example.walkingundead.services

import com.example.walkingundead.utilities.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthenticationService() {

    suspend fun signIn(email: String, password: String): AuthResult {

        return try {

            val authResult = Firebase.auth.signInWithEmailAndPassword(email, password).await()
            AuthResult.SUCCESS

        } catch (e: Exception) {
            AuthResult.FAILED
        }
        catch (e: FirebaseAuthInvalidCredentialsException) {
            // Handle wrong password or similar errors
            return AuthResult.FAILED_WRONG_PASSWORD
        } catch (e: FirebaseAuthUserCollisionException) {
            // Handle email already in use
            return AuthResult.FAILED_EMAIL_ALREADY_IN_USE
        } catch (e: Exception) {
            return AuthResult.FAILED
        }
    }

    suspend fun register(email: String, password: String): AuthResult {

        return try {

            val authResult = Firebase.auth.createUserWithEmailAndPassword(email, password).await()
            AuthResult.SUCCESS

        } catch (e: Exception) {
            AuthResult.FAILED
        }
    }

    suspend fun logOut() {
        Firebase.auth.signOut()
    }

    fun isAuthenticated(): Boolean {
        return Firebase.auth.currentUser != null
    }

    fun getEmail(): String {
        return Firebase.auth.currentUser?.email as String
    }
}