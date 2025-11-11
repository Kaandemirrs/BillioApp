package com.billioapp.data.repository

import com.billioapp.core.network.NetworkErrorMapper
import com.billioapp.core.network.readableMessage
import com.billioapp.data.mapper.NotificationsMapper
import com.billioapp.data.remote.api.NotificationsApi
import com.billioapp.domain.model.NotificationList
import com.billioapp.domain.repository.NotificationsRepository
import com.billioapp.domain.util.Result
import io.github.aakira.napier.Napier

class NotificationsRepositoryImpl(
    private val api: NotificationsApi,
    private val mapper: NotificationsMapper
) : NotificationsRepository {

    override suspend fun getNotifications(): Result<NotificationList> = execute {
        Napier.i(tag = "NotificationsRepository", message = "Çağrı yapılıyor: GET /api/v1/notifications")
        val resp = api.getNotifications()
        Napier.i(tag = "NotificationsRepository", message = "Çağrı tamamlandı: GET /api/v1/notifications, success=${resp.success}")
        if (resp.success && resp.data != null) {
            val dto = resp.data
            val items = mapper.mapList(dto.notifications)
            NotificationList(
                notifications = items,
                unreadCount = dto.unreadCount
            )
        } else {
            val msg = resp.error?.message ?: "Bilinmeyen API hatası"
            throw IllegalStateException(msg)
        }
    }

    private suspend fun <T> execute(block: suspend () -> T): Result<T> =
        try {
            Result.Success(block())
        } catch (t: Throwable) {
            val error = NetworkErrorMapper.fromThrowable(t)
            Napier.e(tag = "NotificationsRepository", message = "Hata alındı: ${error.readableMessage()}")
            Result.Error(error.readableMessage(), t)
        }
}