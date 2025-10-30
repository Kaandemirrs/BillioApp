package com.billioapp.data.repository

import com.billioapp.core.network.NetworkErrorMapper
import com.billioapp.core.network.readableMessage
import com.billioapp.data.mapper.SubscriptionsMapper
import com.billioapp.data.remote.api.SubscriptionsApi
import com.billioapp.data.remote.dto.subscription.AddSubscriptionRequestDto
import com.billioapp.domain.model.subscriptions.SubscriptionsResponse
import com.billioapp.domain.model.subscriptions.AddSubscriptionRequest
import com.billioapp.domain.model.subscriptions.Subscription
import com.billioapp.domain.repository.SubscriptionsRepository
import com.billioapp.domain.util.Result
import io.github.aakira.napier.Napier
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText

class SubscriptionsRepositoryImpl(
    private val api: SubscriptionsApi,
    private val mapper: SubscriptionsMapper
) : SubscriptionsRepository {

    override suspend fun getSubscriptions(
        limit: Int,
        page: Int,
        category: String?,
        isActive: Boolean?,
        sortBy: String?,
        order: String?,
    ): Result<SubscriptionsResponse> = execute {
        Napier.i(tag = "SubscriptionsRepository", message = "Çağrı yapılıyor: GET /api/v1/subscriptions")
        val resp = api.getSubscriptions(limit, page, category, isActive, sortBy, order)
        Napier.i(tag = "SubscriptionsRepository", message = "Çağrı tamamlandı: GET /api/v1/subscriptions, success=${resp.success}")
        if (resp.success && resp.data != null) {
            Napier.d(tag = "SubscriptionsRepo", message = "API'den ${resp.data?.subscriptions?.size ?: 0} adet DTO alındı")
            mapper.mapResponse(resp.data)
        } else {
            val msg = resp.error?.message ?: "Bilinmeyen API hatası"
            throw IllegalStateException(msg)
        }
    }

    override suspend fun addSubscription(request: AddSubscriptionRequest): Result<Subscription> = execute {
        Napier.i(tag = "SubscriptionsRepository", message = "Çağrı yapılıyor: POST /api/v1/subscriptions")
        val dto = AddSubscriptionRequestDto(
            name = request.name,
            category = request.category,
            amount = request.amount,
            currency = request.currency,
            billingCycle = request.billingCycle,
            startDate = request.startDate,
            billingDay = request.billingDay
        )
        val resp = api.addSubscription(dto)
        Napier.i(tag = "SubscriptionsRepository", message = "Çağrı tamamlandı: POST /api/v1/subscriptions, success=${resp.success}")
        if (resp.success && resp.data != null) {
            mapper.mapItem(resp.data)
        } else {
            val msg = resp.error?.message ?: "Bilinmeyen API hatası"
            throw IllegalStateException(msg)
        }
    }

    private suspend fun <T> execute(block: suspend () -> T): Result<T> =
        try {
            Result.Success(block())
        } catch (t: Throwable) {
            // 4xx hatalarında ham gövdeyi logla (backend neden reddetti?)
            if (t is ClientRequestException) {
                val status = t.response.status
                val rawBody = try { t.response.bodyAsText() } catch (e: Exception) { "Hata gövdesi okunamadı: ${e.message}" }
                Napier.e(tag = "SubscriptionsRepository", message = "ClientRequestException ${status.value} ${status.description}. Body: $rawBody")
            }
            val error = NetworkErrorMapper.fromThrowable(t)
            Napier.e(tag = "SubscriptionsRepository", message = "Hata alındı: ${error.readableMessage()}")
            Result.Error(error.readableMessage(), t)
        }
}