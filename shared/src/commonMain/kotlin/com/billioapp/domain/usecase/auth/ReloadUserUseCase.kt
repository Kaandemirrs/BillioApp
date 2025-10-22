package com.billioapp.domain.usecase.auth

import com.billioapp.domain.repository.AuthRepository

class ReloadUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.reloadUser()
}
