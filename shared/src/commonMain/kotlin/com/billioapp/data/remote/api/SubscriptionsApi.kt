package com.billioapp.data.remote.api

import com.billioapp.data.remote.dto.common.BaseResponseDto
import com.billioapp.data.remote.dto.common.ErrorDto
import com.billioapp.data.remote.dto.subscription.SubscriptionListResponseDto
import com.billioapp.data.remote.dto.subscription.AddSubscriptionRequestDto
import com.billioapp.data.remote.dto.subscription.SubscriptionDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.parameters
import io.github.aakira.napier.Napier
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.builtins.serializer

class SubscriptionsApi(
    private val httpClient: HttpClient
) {
    suspend fun getSubscriptions(
        limit: Int = 20,
        page: Int = 1,
        category: String? = null,
        isActive: Boolean? = null,
        sortBy: String? = null,
        order: String? = null,
    ): BaseResponseDto<SubscriptionListResponseDto> {
        Napier.i(tag = "SubscriptionsApi", message = "Çağrı yapılıyor: GET /api/v1/subscriptions")
        val response = httpClient.get("/api/v1/subscriptions") {
            url.parameters.append("limit", limit.toString())
            url.parameters.append("page", page.toString())
            category?.let { url.parameters.append("category", it) }
            isActive?.let { url.parameters.append("is_active", it.toString()) }
            sortBy?.let { url.parameters.append("sort_by", it) }
            order?.let { url.parameters.append("order", it) }
        }
        Napier.i(tag = "SubscriptionsApi", message = "Yanıt alındı: GET /api/v1/subscriptions -> ${response.status}")
        return response.body()
    }

    suspend fun addSubscription(body: AddSubscriptionRequestDto): BaseResponseDto<SubscriptionDto> {
        Napier.i(tag = "SubscriptionsApi", message = "Çağrı yapılıyor: POST /api/v1/subscriptions")
        val response: HttpResponse = httpClient.post("/api/v1/subscriptions") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        Napier.i(tag = "SubscriptionsApi", message = "Yanıt alındı: POST /api/v1/subscriptions -> ${response.status}")
        val raw = response.bodyAsText()
        Napier.i(tag = "SubscriptionsApi", message = "Ham yanıt: POST /api/v1/subscriptions -> $raw")

        val json = Json { ignoreUnknownKeys = true; isLenient = true }
        return try {
            val element = json.parseToJsonElement(raw)
            if (element is JsonObject && element.containsKey("success")) {
                json.decodeFromString(BaseResponseDto.serializer(SubscriptionDto.serializer()), raw)
            } else if (element is JsonObject && element.containsKey("data")) {
                val dataEl = element["data"]!!
                val dto = json.decodeFromJsonElement(SubscriptionDto.serializer(), dataEl)
                BaseResponseDto(success = true, data = dto, error = null)
            } else {
                val dto = json.decodeFromString(SubscriptionDto.serializer(), raw)
                BaseResponseDto(success = true, data = dto, error = null)
            }
        } catch (e: Exception) {
            Napier.e(tag = "SubscriptionsApi", message = "Parse error POST /api/v1/subscriptions: ${e.message}. Raw: $raw")
            if (response.status == HttpStatusCode.BadRequest || response.status.value in 400..499) {
                BaseResponseDto(success = false, data = null, error = ErrorDto(message = raw))
            } else {
                BaseResponseDto(success = false, data = null, error = ErrorDto(message = e.message))
            }
        }
    }
}