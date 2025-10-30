package com.billioapp.domain.model.subscriptions

data class Subscription(
    val id: String,
    val name: String,
    val amount: Double,
    val currency: String,
    val category: String,
    val isActive: Boolean
)

data class SubscriptionsResponse(
    val subscriptions: List<Subscription>,
    val totalAmount: Double,
    val currency: String,
    val page: Int,
    val limit: Int,
    val total: Int?
)