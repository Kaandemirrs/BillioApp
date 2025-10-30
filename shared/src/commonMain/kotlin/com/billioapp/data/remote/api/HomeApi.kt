package com.billioapp.data.remote.api

import com.billioapp.data.remote.dto.home.HomeSummaryDto
import com.billioapp.data.remote.dto.home.MonthlyLimitDto
import com.billioapp.data.remote.dto.home.CategorySpendDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.github.aakira.napier.Napier
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class HomeApi(
    private val httpClient: HttpClient
) {
    @Serializable
    private data class SummaryDtoTmp(
        @SerialName("totalAmount") val totalAmount: Double? = null,
        @SerialName("currency") val currency: String? = null
    )

    suspend fun getHomeSummary(): HomeSummaryDto {
        Napier.i(tag = "HomeApi", message = "Çağrı yapılıyor: GET /home/summary")
        val response: HttpResponse = httpClient.get("/home/summary")
        Napier.i(tag = "HomeApi", message = "Yanıt alındı: GET /home/summary -> ${response.status}")

        // 204 No Content veya tamamen boş gövde durumunda güvenli varsayılanlar ile dön
        if (response.status == HttpStatusCode.NoContent) {
            Napier.i(tag = "HomeApi", message = "Boş yanıt: 204 No Content /home/summary")
            return HomeSummaryDto()
        }

        val text = response.bodyAsText()
        if (text.isBlank()) {
            Napier.i(tag = "HomeApi", message = "Boş gövde: /home/summary")
            return HomeSummaryDto()
        }

        val json = Json { ignoreUnknownKeys = true; isLenient = true }
        return try {
            val element = json.parseToJsonElement(text)
            if (element is JsonObject && element.containsKey("data")) {
                // Zarflı (envelope) yanıt: { "data": { ... } }
                element["data"]?.let { dataEl ->
                    json.decodeFromJsonElement(HomeSummaryDto.serializer(), dataEl)
                } ?: HomeSummaryDto()
            } else {
                // Zarfsız yanıt: doğrudan DTO
                json.decodeFromString(HomeSummaryDto.serializer(), text)
            }
        } catch (e: Exception) {
            Napier.e(tag = "HomeApi", message = "Parse error /home/summary: ${e.message}")
            // Parse edilemeyen durumda güvenli varsayılanlar ile dön
            HomeSummaryDto()
        }
    }

    suspend fun getMonthlyLimit(): MonthlyLimitDto? {
        Napier.i(tag = "HomeApi", message = "Çağrı yapılıyor: GET /home/monthly-limit")
        val response: HttpResponse = httpClient.get("/home/monthly-limit")
        Napier.i(tag = "HomeApi", message = "Yanıt alındı: GET /home/monthly-limit -> ${response.status}")
        if (response.status == HttpStatusCode.NoContent) {
            Napier.i(tag = "HomeApi", message = "Boş yanıt: 204 No Content /home/monthly-limit")
            return null
        }
        val text = response.bodyAsText()
        if (text.isBlank()) {
            Napier.i(tag = "HomeApi", message = "Boş gövde: /home/monthly-limit")
            return null
        }
        val json = Json { ignoreUnknownKeys = true; isLenient = true }
        return try {
            val element = json.parseToJsonElement(text)
            if (element is JsonObject && element.containsKey("data")) {
                element["data"]?.let { dataEl ->
                    json.decodeFromJsonElement(MonthlyLimitDto.serializer(), dataEl)
                }
            } else {
                json.decodeFromString(MonthlyLimitDto.serializer(), text)
            }
        } catch (e: Exception) {
            Napier.e(tag = "HomeApi", message = "Parse error /home/monthly-limit: ${e.message}")
            throw e
        }
    }

    suspend fun updateMonthlyLimit(amount: Double, currency: String): MonthlyLimitDto {
        Napier.i(tag = "HomeApi", message = "Çağrı yapılıyor: PUT /home/monthly-limit")
        val response: HttpResponse = httpClient.put("/home/monthly-limit") {
            setBody(MonthlyLimitDto(amount = amount, currency = currency))
        }
        Napier.i(tag = "HomeApi", message = "Yanıt alındı: PUT /home/monthly-limit -> ${response.status}")
        val text = response.bodyAsText()
        Napier.i(tag = "HomeApi", message = "Ham yanıt: PUT /home/monthly-limit -> $text")
        val json = Json { ignoreUnknownKeys = true; isLenient = true }
        return try {
            val element = json.parseToJsonElement(text)
            if (element is JsonObject && element.containsKey("data")) {
                val dataEl = element["data"]!!
                json.decodeFromJsonElement(MonthlyLimitDto.serializer(), dataEl)
            } else {
                json.decodeFromString(MonthlyLimitDto.serializer(), text)
            }
        } catch (e: Exception) {
            Napier.e(tag = "HomeApi", message = "Parse error PUT /home/monthly-limit: ${e.message}")
            throw e
        }
    }
}