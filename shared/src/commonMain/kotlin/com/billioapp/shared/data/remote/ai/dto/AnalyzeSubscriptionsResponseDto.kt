package com.billioapp.shared.data.remote.ai.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnalyzeSubscriptionsResponseDto(
    @SerialName("analysis_text") val analysisText: String,
    @SerialName("total_subscriptions") val totalSubscriptions: Int,
    @SerialName("active_subscriptions") val activeSubscriptions: Int,
    @SerialName("monthly_total") val monthlyTotal: Double,
    @SerialName("yearly_total") val yearlyTotal: Double
)