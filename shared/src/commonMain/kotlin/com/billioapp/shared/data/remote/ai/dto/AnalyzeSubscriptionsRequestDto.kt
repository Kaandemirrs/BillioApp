package com.billioapp.shared.data.remote.ai.dto

import com.billioapp.data.remote.dto.subscription.SubscriptionDto
import kotlinx.serialization.Serializable

@Serializable
data class AnalyzeSubscriptionsRequestDto(
    val subscriptions: List<SubscriptionDto>
)