package com.billioapp.domain.usecase.auth

import com.billioapp.domain.repository.AuthRepository

class CheckEmailVerifiedUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.isEmailVerified()
}
