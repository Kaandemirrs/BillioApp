package com.billioapp.data.remote.api

import com.billioapp.data.remote.dto.services.ServicePlanReadBasicDto
import com.billioapp.data.remote.dto.services.ServiceReadBasicDto
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

interface ServicesApi {
    suspend fun searchServices(query: String): List<ServiceReadBasicDto>
    suspend fun getServicePlans(serviceId: String): List<ServicePlanReadBasicDto>
}

class ServicesApiImpl(
    private val client: HttpClient
) : ServicesApi {

    override suspend fun searchServices(query: String): List<ServiceReadBasicDto> {
        Napier.i(tag = "ServicesApi", message = "GET /api/v1/services?query=$query çağrılıyor")
        val response: HttpResponse = client.get("/api/v1/services") {
            parameter("query", query)
        }
        Napier.i(tag = "ServicesApi", message = "GET /api/v1/services yanıt: ${response.status}")
        return parseListResponse(response, ServiceReadBasicDto.serializer())
    }

    override suspend fun getServicePlans(serviceId: String): List<ServicePlanReadBasicDto> {
        Napier.i(tag = "ServicesApi", message = "GET /api/v1/services/$serviceId/plans çağrılıyor")
        val response: HttpResponse = client.get("/api/v1/services/$serviceId/plans")
        Napier.i(tag = "ServicesApi", message = "GET /api/v1/services/$serviceId/plans yanıt: ${response.status}")
        return parseListResponse(response, ServicePlanReadBasicDto.serializer())
    }

    private suspend fun <T> parseListResponse(response: HttpResponse, serializer: kotlinx.serialization.KSerializer<T>): List<T> {
        if (response.status == HttpStatusCode.NoContent) return emptyList()
        val raw = response.bodyAsText()
        val json = Json { ignoreUnknownKeys = true; isLenient = true }
        return try {
            val element = json.parseToJsonElement(raw)
            when (element) {
                is JsonArray -> json.decodeFromString(ListSerializer(serializer), raw)
                is JsonObject -> {
                    // data alanı liste ise
                    val dataEl = element["data"]
                    if (dataEl != null && dataEl is JsonArray) {
                        json.decodeFromJsonElement(ListSerializer(serializer), dataEl)
                    } else {
                        // Bazı API’lar { items: [...] } döndürebilir; esnek yaklaşım
                        val itemsEl = element["items"]
                        if (itemsEl != null && itemsEl is JsonArray) {
                            json.decodeFromJsonElement(ListSerializer(serializer), itemsEl)
                        } else {
                            // Son çare: tek öğe gibi davranıp listeye koy
                            listOf(json.decodeFromString(serializer, raw))
                        }
                    }
                }
                else -> emptyList()
            }
        } catch (e: Exception) {
            Napier.e(tag = "ServicesApi", message = "Parse error ${response.status}: ${e.message}. Raw: ${raw}")
            throw e
        }
    }
}

