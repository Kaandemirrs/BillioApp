package com.billioapp.domain.model.subscriptions

data class Subscription(
    val id: String,
    val name: String,
    val amount: Double,
    val currency: String,
    val category: String,
    val isActive: Boolean,
    val color: String? = null,
    val predefinedBills: PredefinedBill? = null,
    // AI analiz ve listeleme yanıtlarında gelen ek alanlar
    val billing_cycle: String? = null,
    val billing_day: Int? = null,
    val start_date: String? = null,
    val next_payment_date: String? = null,
    val logo_url: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
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