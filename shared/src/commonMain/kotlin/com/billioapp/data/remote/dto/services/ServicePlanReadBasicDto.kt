package com.billioapp.data.remote.dto.services

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServicePlanReadBasicDto(
    @SerialName("id") val id: String,
    @SerialName("plan_name") val planName: String,
    @SerialName("cached_price") val cachedPrice: Double? = null,
    @SerialName("currency") val currency: String? = null,
    @SerialName("billing_cycle") val billingCycle: String? = null
)

