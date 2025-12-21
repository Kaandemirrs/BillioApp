package com.billioapp.domain.repository

import com.billioapp.domain.util.Result

interface UserRepository {
    suspend fun registerDevice(token: String): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
}
