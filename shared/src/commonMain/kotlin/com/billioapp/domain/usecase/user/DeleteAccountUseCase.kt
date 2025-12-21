package com.billioapp.domain.usecase.user

import com.billioapp.domain.repository.UserRepository
import com.billioapp.domain.util.Result

class DeleteAccountUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<Unit> = repository.deleteAccount()
}
