package com.billioapp.domain.repository

import com.billioapp.domain.model.AuthResult
import com.billioapp.domain.model.User
import com.billioapp.domain.util.Result

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthResult>
    suspend fun register(email: String, password: String): Result<AuthResult>
    suspend fun logout(): Result<Unit>
    suspend fun getCurrentUser(): Result<User?>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    suspend fun sendEmailVerification(): Result<Unit>
    suspend fun isEmailVerified(): Result<Boolean>
    suspend fun reloadUser(): Result<Unit>
    suspend fun signInWithGoogle(idToken: String): Result<AuthResult>
    suspend fun signInWithApple(idToken: String, nonce: String): Result<AuthResult>
}
