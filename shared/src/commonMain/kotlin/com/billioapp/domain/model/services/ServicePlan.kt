package com.billioapp.domain.model.services

data class ServicePlan(
    val id: String,
    val planName: String,
    val cachedPrice: Double?,
    val currency: String?,
    val billingCycle: String?
)