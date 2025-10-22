package com.billioapp.domain.usecase.auth

import com.billioapp.domain.repository.AuthRepository

class GoogleSignInUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idToken: String) = repository.signInWithGoogle(idToken)
}
