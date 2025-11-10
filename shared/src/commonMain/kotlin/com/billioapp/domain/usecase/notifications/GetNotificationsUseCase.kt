package com.billioapp.domain.usecase.notifications

import com.billioapp.domain.model.Notification
import com.billioapp.domain.repository.NotificationsRepository
import com.billioapp.domain.util.Result

class GetNotificationsUseCase(
    private val repository: NotificationsRepository
) {
    suspend operator fun invoke(): Result<List<Notification>> = repository.getNotifications()
}