package com.billioapp.data.remote.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterDeviceRequestDto(
    @SerialName("fcm_token") val fcmToken: String
)