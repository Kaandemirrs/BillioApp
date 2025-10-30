package com.billioapp.data.remote.dto.subscription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddSubscriptionRequestDto(
    @SerialName("name") val name: String,
    @SerialName("category") val category: String,
    @SerialName("amount") val amount: Double,
    @SerialName("currency") val currency: String,
    @SerialName("billing_cycle") val billingCycle: String,
    @SerialName("start_date") val startDate: String,
    @SerialName("billing_day") val billingDay: Int? = null
)