package com.billioapp.domain.usecase.auth

import com.billioapp.domain.repository.AuthRepository
import com.billioapp.domain.util.Result

class ForgotPasswordUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank() || !emailRegex.matches(trimmedEmail)) {
            return Result.Error("Lütfen geçerli bir email adresi girin")
        }
        return authRepository.sendPasswordResetEmail(trimmedEmail)
    }

    private companion object {
        val emailRegex =
            Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    }
}
