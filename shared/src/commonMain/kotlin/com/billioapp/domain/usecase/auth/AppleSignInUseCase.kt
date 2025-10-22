package com.billioapp.domain.usecase.auth

import com.billioapp.domain.repository.AuthRepository

class AppleSignInUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idToken: String, nonce: String) =
        repository.signInWithApple(idToken, nonce)
}
