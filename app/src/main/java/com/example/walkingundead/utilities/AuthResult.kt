package com.example.walkingundead.utilities

/**
 * Result of authentication attempts
 */
enum class AuthResult {
    SUCCESS,
    FAILED,
    FAILED_WRONG_PASSWORD,
    FAILED_EMAIL_ALREADY_IN_USE,
}