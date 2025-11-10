package com.billioapp.domain.usecase.user

import com.billioapp.domain.repository.UserRepository
import com.billioapp.domain.util.Result

class RegisterDeviceUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(token: String): Result<Unit> = repository.registerDevice(token)
}