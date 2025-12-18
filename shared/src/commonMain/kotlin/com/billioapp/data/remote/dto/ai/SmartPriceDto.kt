package com.billioapp.data.remote.dto.ai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SmartPriceRequestDto(
    @SerialName("service_name") val serviceName: String,
    @SerialName("plan_name") val planName: String = "Standart Plan"
)

@Serializable
data class SmartPriceResponseDto(
    @SerialName("suggested_price") val suggestedPrice: Double?,
    @SerialName("currency") val currency: String,
    @SerialName("source") val source: String?,
    @SerialName("confidence") val confidence: String
)

