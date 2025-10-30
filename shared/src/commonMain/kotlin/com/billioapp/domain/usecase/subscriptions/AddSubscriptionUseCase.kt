package com.billioapp.domain.usecase.subscriptions

import com.billioapp.domain.model.subscriptions.AddSubscriptionRequest
import com.billioapp.domain.model.subscriptions.Subscription
import com.billioapp.domain.repository.SubscriptionsRepository
import com.billioapp.domain.util.Result

class AddSubscriptionUseCase(
    private val repository: SubscriptionsRepository
)
{
    suspend operator fun invoke(request: AddSubscriptionRequest): Result<Subscription> =
        repository.addSubscription(request)
}