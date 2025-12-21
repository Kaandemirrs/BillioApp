package com.billioapp.data.remote.api

import com.billioapp.data.remote.dto.user.RegisterDeviceRequestDto
import com.billioapp.data.remote.dto.user.DeleteAccountRequestDto
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.delete
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface UserApi {
    suspend fun registerDevice(body: RegisterDeviceRequestDto): HttpResponse
    suspend fun deleteAccount(body: DeleteAccountRequestDto): HttpResponse
}

class UserApiImpl(
    private val client: HttpClient
) : UserApi {
    override suspend fun registerDevice(body: RegisterDeviceRequestDto): HttpResponse {
        Napier.i(tag = "UserApi", message = "POST /api/v1/user/register-device çağrılıyor")
        val response: HttpResponse = client.post("/api/v1/user/register-device") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        Napier.i(tag = "UserApi", message = "Yanıt: ${response.status}")
        return response
    }

    override suspend fun deleteAccount(body: DeleteAccountRequestDto): HttpResponse {
        Napier.i(tag = "UserApi", message = "DELETE /api/v1/user/account çağrılıyor")
        val response: HttpResponse = client.delete("/api/v1/user/account") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        Napier.i(tag = "UserApi", message = "Yanıt: ${response.status}")
        return response
    }
}
