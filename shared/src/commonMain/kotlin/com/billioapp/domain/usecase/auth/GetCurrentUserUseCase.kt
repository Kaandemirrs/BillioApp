package com.billioapp.domain.usecase.auth

import com.billioapp.domain.repository.AuthRepository

class GetCurrentUserUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.getCurrentUser()
}
