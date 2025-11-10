package com.billioapp.data.remote.dto.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("read") val read: Boolean = false
)