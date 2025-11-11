package com.billioapp.data.remote.dto.notification

import com.billioapp.data.remote.dto.common.PaginationDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationListResponseDto(
    @SerialName("notifications") val notifications: List<NotificationDto> = emptyList(),
    @SerialName("unread_count") val unreadCount: Int = 0,
    @SerialName("pagination") val pagination: PaginationDto? = null
)