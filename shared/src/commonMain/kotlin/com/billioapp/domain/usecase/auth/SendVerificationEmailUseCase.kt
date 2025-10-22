package com.billioapp.domain.usecase.auth

import com.billioapp.domain.repository.AuthRepository

class SendVerificationEmailUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.sendEmailVerification()
}
