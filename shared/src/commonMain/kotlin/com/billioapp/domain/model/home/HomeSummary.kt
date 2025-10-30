package com.billioapp.domain.model.home

data class CategorySpend(
    val name: String,
    val amount: Double,
    val colorHex: Long?
)

data class HomeSummary(
    val totalAmount: Double,
    val currency: String,
    val categories: List<CategorySpend>,
    val monthlyLimit: Double?
)