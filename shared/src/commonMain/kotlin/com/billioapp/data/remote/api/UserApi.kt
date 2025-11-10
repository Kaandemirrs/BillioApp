package com.billioapp.data.remote.api

import com.billioapp.data.remote.dto.user.RegisterDeviceRequestDto
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface UserApi {
    suspend fun registerDevice(body: RegisterDeviceRequestDto): HttpResponse
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
}