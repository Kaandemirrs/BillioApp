package com.billioapp.domain.usecase.subscriptions

import com.billioapp.domain.model.subscriptions.SubscriptionsResponse
import com.billioapp.domain.repository.SubscriptionsRepository
import com.billioapp.domain.util.Result

class GetSubscriptionsUseCase(
    private val repository: SubscriptionsRepository
) {
    suspend operator fun invoke(
        limit: Int = 20,
        page: Int = 1,
        category: String? = null,
        isActive: Boolean? = null,
        sortBy: String? = null,
        order: String? = null,
    ): Result<SubscriptionsResponse> = repository.getSubscriptions(limit, page, category, isActive, sortBy, order)
}