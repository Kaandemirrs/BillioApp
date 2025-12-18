package com.billioapp.data.remote.api

import com.billioapp.data.remote.dto.common.BaseResponseDto
import com.billioapp.data.remote.dto.common.ErrorDto
import com.billioapp.data.remote.dto.ai.SmartPriceRequestDto
import com.billioapp.data.remote.dto.ai.SmartPriceResponseDto
import com.billioapp.shared.data.remote.ai.dto.AnalyzeSubscriptionsRequestDto
import com.billioapp.shared.data.remote.ai.dto.AnalyzeSubscriptionsResponseDto
import com.billioapp.shared.data.remote.ai.dto.GetPriceRequestDto
import com.billioapp.shared.data.remote.ai.dto.GetPriceResponseDto
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

/**
 * AI servisleri için API arayüzü.
 * Yanıtlar BaseResponseDto zarfı ile döner.
 */
interface AiApi {
    suspend fun getPriceSuggestion(body: GetPriceRequestDto): BaseResponseDto<GetPriceResponseDto>
    suspend fun getAnalysis(body: AnalyzeSubscriptionsRequestDto): BaseResponseDto<AnalyzeSubscriptionsResponseDto>
    suspend fun getFinancialAnalysis(): com.billioapp.data.remote.dto.analysis.AiAnalysisResponseDto
    suspend fun getSmartPrice(body: SmartPriceRequestDto): BaseResponseDto<SmartPriceResponseDto>
}

/**
 * Ktor tabanlı AiApi implementasyonu.
 */
class AiApiImpl(
    private val client: HttpClient
) : AiApi {

    override suspend fun getPriceSuggestion(body: GetPriceRequestDto): BaseResponseDto<GetPriceResponseDto> {
        Napier.i(tag = "AiApi", message = "POST /api/v1/ai/get-price çağrılıyor")
        val response: HttpResponse = client.post("/api/v1/ai/get-price") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        Napier.i(tag = "AiApi", message = "POST /api/v1/ai/get-price yanıt: ${response.status}")
        return parseBaseResponse(response)
    }

    override suspend fun getAnalysis(body: AnalyzeSubscriptionsRequestDto): BaseResponseDto<AnalyzeSubscriptionsResponseDto> {
        Napier.i(tag = "AiApi", message = "POST /api/v1/ai/analyze-subscriptions çağrılıyor")
        val response: HttpResponse = client.post("/api/v1/ai/analyze-subscriptions") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        Napier.i(tag = "AiApi", message = "POST /api/v1/ai/analyze-subscriptions yanıt: ${response.status}")
        return parseBaseResponse(response)
    }

    override suspend fun getFinancialAnalysis(): com.billioapp.data.remote.dto.analysis.AiAnalysisResponseDto {
        Napier.i(tag = "AiApi", message = "POST /api/v1/analysis çağrılıyor")
        val response: HttpResponse = client.post("/api/v1/analysis") {}
        Napier.i(tag = "AiApi", message = "POST /api/v1/analysis yanıt: ${response.status}")
        return try {
            // Hata Düzeltmesi: Dış Zarfı (BaseResponseDto) aç ve sadece 'data' alanını çek
            val responseBody = response.body<BaseResponseDto<com.billioapp.data.remote.dto.analysis.AiAnalysisResponseDto>>()

            if (responseBody.success && responseBody.data != null) {
                responseBody.data // Sadece rapor içeriğini döndür (AiAnalysisResponseDto)
            } else {
                val msg = responseBody.error?.message ?: responseBody.message ?: "AI raporu başarısız döndü."
                throw IllegalStateException(msg)
            }
        } catch (e: Exception) {
            Napier.e(tag = "AiApi", message = "Nihai Parse Hatası ${response.status}: ${e.message}", throwable = e)
            throw e
        }
    }

    override suspend fun getSmartPrice(body: SmartPriceRequestDto): BaseResponseDto<SmartPriceResponseDto> {
        Napier.i(tag = "AiApi", message = "POST /api/v1/ai/smart-price çağrılıyor")
        val response: HttpResponse = client.post("/api/v1/ai/smart-price") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        Napier.i(tag = "AiApi", message = "POST /api/v1/ai/smart-price yanıt: ${response.status}")
        return parseBaseResponse(response)
    }

    private suspend inline fun <reified T> parseBaseResponse(response: HttpResponse): BaseResponseDto<T> =
        try {
            response.body<BaseResponseDto<T>>()
        } catch (e: Exception) {
            val raw = try { response.bodyAsText() } catch (_: Exception) { null }
            Napier.e(tag = "AiApi", message = "Parse error ${response.status}: ${e.message}. Raw: ${raw ?: "(yok)"}")
            if (response.status == HttpStatusCode.BadRequest || response.status.value in 400..499) {
                BaseResponseDto(success = false, data = null, error = ErrorDto(message = raw ?: e.message))
            } else {
                BaseResponseDto(success = false, data = null, error = ErrorDto(message = e.message))
            }
        }
}
