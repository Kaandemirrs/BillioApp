package com.billioapp.data.remote.dto.subscription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionListResponseDto(
    @SerialName("subscriptions") val subscriptions: List<SubscriptionDto> = emptyList(),
    @SerialName("summary") val summary: SubscriptionSummaryDto? = null,
    @SerialName("pagination") val pagination: PaginationDto? = null
)

@Serializable
data class SubscriptionDto(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String,
    @SerialName("amount") val amount: Double,
    @SerialName("currency") val currency: String? = null,
    @SerialName("category") val category: String? = null,
    @SerialName("is_active") val isActive: Boolean = true,
    @SerialName("color") val color: String? = null,
    @SerialName("predefined_bills") val predefinedBills: PredefinedBillDto? = null
)

@Serializable
data class PredefinedBillDto(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("amount") val amount: Double? = null,
    @SerialName("currency") val currency: String? = null,
    @SerialName("primary_color") val primaryColor: String? = null
)

@Serializable
data class SubscriptionSummaryDto(
    @SerialName("totalAmount") val totalAmount: Double? = null,
    @SerialName("currency") val currency: String? = null
)

@Serializable
data class PaginationDto(
    @SerialName("page") val page: Int = 1,
    @SerialName("limit") val limit: Int = 20,
    @SerialName("total") val total: Int? = null
)