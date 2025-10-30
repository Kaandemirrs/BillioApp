package com.billioapp.domain.model.subscriptions

data class AddSubscriptionRequest(
    val name: String,
    val category: String,
    val amount: Double,
    val currency: String,
    val billingCycle: String,
    val startDate: String,
    val billingDay: Int?,
    val color: String? = null
)