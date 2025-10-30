package com.billioapp.data.remote.dto.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategorySpendDto(
    @SerialName("name") val name: String,
    @SerialName("amount") val amount: Double,
    @SerialName("colorHex") val colorHex: Long? = null
)

@Serializable
data class HomeSummaryDto(
    @SerialName("totalAmount") val totalAmount: Double = 0.0,
    @SerialName("currency") val currency: String = "TL",
    @SerialName("categories") val categories: List<CategorySpendDto> = emptyList(),
    @SerialName("monthlyLimit") val monthlyLimit: Double? = null
)

@Serializable
data class MonthlyLimitDto(
    @SerialName("amount") val amount: Double,
    @SerialName("currency") val currency: String
)