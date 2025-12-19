package com.billioapp.data.remote.dto.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SyncPaymentRequestDto(
    @SerialName("is_premium") val isPremium: Boolean,
    @SerialName("platform") val platform: String = "android",
    @SerialName("source") val source: String = "revenuecat"
)

@Serializable
data class SyncPaymentResponseDto(
    @SerialName("synced") val synced: Boolean = true
)

