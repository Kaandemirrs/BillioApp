package com.billioapp.data.remote.api

import com.billioapp.data.remote.dto.common.BaseResponseDto
import com.billioapp.data.remote.dto.notification.NotificationListResponseDto
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class NotificationsApi(
    private val httpClient: HttpClient
) {
    suspend fun getNotifications(): BaseResponseDto<NotificationListResponseDto> {
        Napier.i(tag = "NotificationsApi", message = "Çağrı yapılıyor: GET /api/v1/notifications")
        val response = httpClient.get("/api/v1/notifications")
        Napier.i(tag = "NotificationsApi", message = "Yanıt alındı: GET /api/v1/notifications -> ${response.status}")
        return response.body()
    }
}