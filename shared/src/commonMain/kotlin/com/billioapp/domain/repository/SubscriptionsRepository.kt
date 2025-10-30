package com.billioapp.domain.repository

import com.billioapp.domain.model.subscriptions.SubscriptionsResponse
import com.billioapp.domain.model.subscriptions.Subscription
import com.billioapp.domain.util.Result

interface SubscriptionsRepository {
    suspend fun getSubscriptions(
        limit: Int = 20,
        page: Int = 1,
        category: String? = null,
        isActive: Boolean? = null,
        sortBy: String? = null,
        order: String? = null,
    ): Result<SubscriptionsResponse>

    suspend fun addSubscription(request: com.billioapp.domain.model.subscriptions.AddSubscriptionRequest): Result<Subscription>
}