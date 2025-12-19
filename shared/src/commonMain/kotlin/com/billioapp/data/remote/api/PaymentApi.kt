package com.billioapp.data.remote.api

import com.billioapp.data.remote.dto.common.BaseResponseDto
import com.billioapp.data.remote.dto.payment.SyncPaymentRequestDto
import com.billioapp.data.remote.dto.payment.SyncPaymentResponseDto
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface PaymentApi {
    suspend fun syncPayment(body: SyncPaymentRequestDto): BaseResponseDto<SyncPaymentResponseDto>
}

class PaymentApiImpl(
    private val client: HttpClient
) : PaymentApi {
    override suspend fun syncPayment(body: SyncPaymentRequestDto): BaseResponseDto<SyncPaymentResponseDto> {
        Napier.i(tag = "PaymentApi", message = "POST /api/v1/payment/sync çağrılıyor")
        val response: HttpResponse = client.post("/api/v1/payment/sync") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        Napier.i(tag = "PaymentApi", message = "POST /api/v1/payment/sync yanıt: ${response.status}")
        return response.body()
    }
}

