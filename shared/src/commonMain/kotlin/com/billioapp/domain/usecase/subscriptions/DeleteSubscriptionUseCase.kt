package com.billioapp.domain.usecase.subscriptions

import com.billioapp.domain.repository.SubscriptionsRepository
import com.billioapp.domain.util.Result

class DeleteSubscriptionUseCase(
    private val repository: SubscriptionsRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> = repository.deleteSubscription(id)
}