package com.billioapp.data.remote.api

import com.billioapp.data.remote.dto.common.BaseResponseDto
import com.billioapp.data.remote.dto.common.ErrorDto
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
        // YENİ KOD: SADECE ZARFI AÇ VE VERİYİ ÇEK (CRASH'TEN KURTULMAK İÇİN)
        val raw = try { response.bodyAsText() } catch (_: Exception) { "" }
        val json = Json { ignoreUnknownKeys = true; isLenient = true }
        val wrapper = json.decodeFromString(
            BaseResponseDto.serializer(com.billioapp.data.remote.dto.analysis.AiAnalysisResponseDto.serializer()),
            raw
        )

        if (wrapper.success && wrapper.data != null) {
            return wrapper.data
        } else {
            val msg = wrapper.error?.message ?: wrapper.message ?: "AI Raporu boş döndü."
            Napier.e(tag = "AiApi", message = "HATA: ${msg}")
            throw IllegalStateException(msg)
        }
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