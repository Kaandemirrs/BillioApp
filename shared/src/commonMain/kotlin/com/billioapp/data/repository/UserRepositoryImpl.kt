package com.billioapp.data.repository

import com.billioapp.core.network.NetworkErrorMapper
import com.billioapp.core.network.readableMessage
import com.billioapp.data.remote.api.UserApi
import com.billioapp.data.remote.dto.user.RegisterDeviceRequestDto
import com.billioapp.data.remote.dto.user.DeleteAccountRequestDto
import com.billioapp.domain.repository.UserRepository
import com.billioapp.domain.util.Result
import io.github.aakira.napier.Napier
import io.ktor.client.plugins.ClientRequestException

class UserRepositoryImpl(
    private val api: UserApi
) : UserRepository {

    override suspend fun registerDevice(token: String): Result<Unit> = execute {
        Napier.i(tag = "UserRepository", message = "Cihaz kaydı gönderiliyor")
        val dto = RegisterDeviceRequestDto(fcmToken = token)
        val response = api.registerDevice(dto)
        if (response.status.value !in 200..299) {
            throw IllegalStateException("Cihaz kaydı başarısız: ${response.status}")
        }
        Unit
    }

    override suspend fun deleteAccount(): Result<Unit> = execute {
        Napier.i(tag = "UserRepository", message = "Hesap silme isteği gönderiliyor")
        val dto = DeleteAccountRequestDto(confirmation = "DELETE_MY_ACCOUNT")
        val response = api.deleteAccount(dto)
        if (response.status.value in 200..299) {
            Unit
        } else {
            throw ClientRequestException(response, "Hesap silme başarısız: ${response.status}")
        }
    }

    private suspend fun <T> execute(block: suspend () -> T): Result<T> =
        try {
            Result.Success(block())
        } catch (t: Throwable) {
            val error = NetworkErrorMapper.fromThrowable(t)
            Napier.e(tag = "UserRepository", message = "Hata: ${error.readableMessage()}")
            Result.Error(error.readableMessage(), t)
        }
}
