package com.billioapp.data.remote.dto.subscriptions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionsResponseDto(
    @SerialName("data") val data: SubscriptionsDataDto
)

@Serializable
data class SubscriptionsDataDto(
    @SerialName("subscriptions") val subscriptions: List<SubscriptionDto> = emptyList(),
    @SerialName("summary") val summary: SubscriptionsSummaryDto? = null,
    @SerialName("pagination") val pagination: PaginationDto? = null
)

@Serializable
data class SubscriptionDto(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String,
    @SerialName("amount") val amount: Double,
    @SerialName("currency") val currency: String? = null,
    @SerialName("category") val category: String? = null,
    @SerialName("is_active") val isActive: Boolean = true
)

@Serializable
data class SubscriptionsSummaryDto(
    @SerialName("totalAmount") val totalAmount: Double? = null,
    @SerialName("currency") val currency: String? = null
)

@Serializable
data class PaginationDto(
    @SerialName("page") val page: Int = 1,
    @SerialName("limit") val limit: Int = 20,
    @SerialName("total") val total: Int? = null
)