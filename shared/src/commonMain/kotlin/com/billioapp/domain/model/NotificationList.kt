package com.billioapp.domain.model

data class NotificationList(
    val notifications: List<Notification>,
    val unreadCount: Int
)