package com.billioapp.domain.model.subscriptions

data class Subscription(
    val id: String,
    val name: String,
    val amount: Double,
    val currency: String,
    val category: String,
    val isActive: Boolean,
    val color: String? = null,
    val predefinedBills: PredefinedBill? = null
)

data class PredefinedBill(
    val id: String? = null,
    val name: String? = null,
    val amount: Double? = null,
    val currency: String? = null,
    val primaryColor: String? = null
)

data class SubscriptionsResponse(
    val subscriptions: List<Subscription>,
    val totalAmount: Double,
    val currency: String,
    val page: Int,
    val limit: Int,
    val total: Int?
)