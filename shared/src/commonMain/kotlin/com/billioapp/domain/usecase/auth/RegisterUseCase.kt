package com.billioapp.domain.usecase.auth

import com.billioapp.domain.repository.AuthRepository

class RegisterUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        repository.register(email, password)
}
