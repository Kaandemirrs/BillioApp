package com.billioapp.data.repository

import com.billioapp.core.network.NetworkErrorMapper
import com.billioapp.core.network.readableMessage
import com.billioapp.data.mapper.AiMapper
import com.billioapp.data.remote.api.AiApi
import com.billioapp.data.remote.dto.ai.SmartPriceRequestDto
import com.billioapp.data.remote.dto.ai.SmartPriceResponseDto
import com.billioapp.data.remote.dto.subscription.PredefinedBillDto
import com.billioapp.data.remote.dto.subscription.SubscriptionDto
import com.billioapp.domain.model.ai.AiAnalysisReport
import com.billioapp.domain.model.ai.AiPriceSuggestion
import com.billioapp.domain.model.ai.FinancialAnalysis
import com.billioapp.domain.model.subscriptions.Subscription
import com.billioapp.domain.repository.AiRepository
import com.billioapp.domain.util.Result
import com.billioapp.shared.data.remote.ai.dto.AnalyzeSubscriptionsRequestDto
import com.billioapp.shared.data.remote.ai.dto.GetPriceRequestDto
import io.github.aakira.napier.Napier

class AiRepositoryImpl(
    private val api: AiApi,
    private val mapper: AiMapper
) : AiRepository {

    override suspend fun getPriceSuggestion(serviceName: String): Result<AiPriceSuggestion> = execute {
        Napier.i(tag = "AiRepository", message = "POST /api/v1/ai/get-price çağrılıyor")
        val request = GetPriceRequestDto(serviceName = serviceName)
        val resp = api.getPriceSuggestion(request)
        Napier.i(tag = "AiRepository", message = "POST /api/v1/ai/get-price tamamlandı success=${resp.success}")
        if (resp.success && resp.data != null) {
            mapper.mapPrice(resp.data)
        } else {
            val msg = resp.error?.message ?: "Bilinmeyen API hatası"
            throw IllegalStateException(msg)
        }
    }

    override suspend fun getAnalysis(subscriptions: List<Subscription>): Result<AiAnalysisReport> = execute {
        Napier.i(tag = "AiRepository", message = "POST /api/v1/ai/analyze-subscriptions çağrılıyor")
        val dtoList = subscriptions.map { s ->
            SubscriptionDto(
                id = s.id,
                name = s.name,
                amount = s.amount,
                currency = s.currency,
                category = s.category,
                isActive = s.isActive,
                color = s.color,
                // Domain Subscription artık bu alanları taşıyor; gerçek değerleri gönderiyoruz.
                billingCycle = s.billing_cycle,
                billingDay = s.billing_day,
                startDate = s.start_date,
                nextPaymentDate = s.next_payment_date,
                logoUrl = s.logo_url,
                createdAt = s.created_at,
                updatedAt = s.updated_at,
                predefinedBills = s.predefinedBills?.let { pb ->
                    PredefinedBillDto(
                        id = pb.id,
                        name = pb.name,
                        amount = pb.amount,
                        currency = pb.currency,
                        primaryColor = pb.primaryColor
                    )
                }
            )
        }
        val request = AnalyzeSubscriptionsRequestDto(subscriptions = dtoList)
        val resp = api.getAnalysis(request)
        Napier.i(tag = "AiRepository", message = "POST /api/v1/ai/analyze-subscriptions tamamlandı success=${resp.success}")
        if (resp.success && resp.data != null) {
            mapper.mapAnalysis(resp.data)
        } else {
            val msg = resp.error?.message ?: "Bilinmeyen API hatası"
            throw IllegalStateException(msg)
        }
    }

    override suspend fun getFinancialAnalysis(): Result<FinancialAnalysis> = execute {
        Napier.i(tag = "AiRepository", message = "POST /api/v1/analysis çağrılıyor")
        val dto = api.getFinancialAnalysis()
        Napier.i(tag = "AiRepository", message = "POST /api/v1/analysis tamamlandı")
        FinancialAnalysis(
            reportText = dto.reportText.orEmpty(),
            sourcesFound = dto.sourcesFound
        )
    }

    override suspend fun getSmartPrice(serviceName: String, planName: String): Result<SmartPriceResponseDto> = execute {
        Napier.i(tag = "AiRepository", message = "POST /api/v1/ai/smart-price çağrılıyor")
        val request = SmartPriceRequestDto(serviceName = serviceName, planName = planName)
        val resp = api.getSmartPrice(request)
        Napier.i(tag = "AiRepository", message = "POST /api/v1/ai/smart-price tamamlandı success=${resp.success}")
        if (resp.success && resp.data != null) {
            resp.data
        } else {
            val msg = resp.error?.message ?: "Bilinmeyen API hatası"
            throw IllegalStateException(msg)
        }
    }

    private suspend fun <T> execute(block: suspend () -> T): Result<T> =
        try {
            Result.Success(block())
        } catch (t: Throwable) {
            val error = NetworkErrorMapper.fromThrowable(t)
            Napier.e(tag = "AiRepository", message = "Hata alındı: ${error.readableMessage()}")
            Result.Error(error.readableMessage(), t)
        }
}
