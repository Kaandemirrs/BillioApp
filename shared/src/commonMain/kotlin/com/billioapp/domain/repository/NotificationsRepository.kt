package com.billioapp.domain.repository

import com.billioapp.domain.model.NotificationList
import com.billioapp.domain.util.Result

interface NotificationsRepository {
    suspend fun getNotifications(): Result<NotificationList>
}