package com.billioapp.data.mapper

import com.billioapp.data.remote.dto.subscription.SubscriptionListResponseDto
import com.billioapp.data.remote.dto.subscription.SubscriptionDto
import com.billioapp.domain.model.subscriptions.Subscription
import com.billioapp.domain.model.subscriptions.SubscriptionsResponse
import io.github.aakira.napier.Napier

class SubscriptionsMapper {
    fun mapResponse(dto: SubscriptionListResponseDto): SubscriptionsResponse = SubscriptionsResponse(
        subscriptions = dto.subscriptions.map { s ->
            Napier.d(tag = "SubscriptionsMapper", message = "'${s.name}' DTO'su eşlendi")
            mapItem(s)
        },
        totalAmount = dto.summary?.totalAmount ?: 0.0,
        currency = dto.summary?.currency ?: "TL",
        page = dto.pagination?.page ?: 1,
        limit = dto.pagination?.limit ?: 20,
        total = dto.pagination?.total
    )

    fun mapItem(s: SubscriptionDto): Subscription = Subscription(
        id = s.id ?: "",
        name = s.name,
        amount = s.amount,
        currency = s.currency ?: "TL",
        category = s.category ?: "",
        isActive = s.isActive,
        color = s.color,
        predefinedBills = s.predefinedBills?.let { predefinedDto ->
            com.billioapp.domain.model.subscriptions.PredefinedBill(
                id = predefinedDto.id,
                name = predefinedDto.name,
                amount = predefinedDto.amount,
                currency = predefinedDto.currency,
                primaryColor = predefinedDto.primaryColor
            )
        }
    )
}