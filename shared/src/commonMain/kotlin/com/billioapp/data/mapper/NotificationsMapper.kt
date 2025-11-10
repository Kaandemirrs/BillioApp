package com.billioapp.data.mapper

import com.billioapp.data.remote.dto.notification.NotificationDto
import com.billioapp.domain.model.Notification

class NotificationsMapper {
    fun mapList(dtos: List<NotificationDto>): List<Notification> =
        dtos.map { mapItem(it) }

    fun mapItem(dto: NotificationDto): Notification =
        Notification(
            id = dto.id,
            title = dto.title ?: "",
            message = dto.message ?: "",
            createdAt = dto.createdAt,
            read = dto.read
        )
}