package com.billioapp.domain.model

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val createdAt: String?,
    val read: Boolean
)