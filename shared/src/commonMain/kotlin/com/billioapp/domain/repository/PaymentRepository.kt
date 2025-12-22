package com.billioapp.domain.repository

interface PaymentRepository {
    suspend fun purchasePremium(): Result<Unit>
    suspend fun restorePurchases(): Result<Unit>
    suspend fun isPro(): Result<Boolean>
    suspend fun getCustomerInfo(): Result<com.revenuecat.purchases.kmp.models.CustomerInfo>
    suspend fun isUserPremium(): Result<Boolean>
}
