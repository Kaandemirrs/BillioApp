package com.billioapp.core.network

import io.ktor.client.plugins.*

import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.TimeoutCancellationException

object NetworkErrorMapper {
    fun fromThrowable(t: Throwable): NetworkError {
        return when (t) {
            is ClientRequestException -> mapStatus(t.response)
            is ServerResponseException -> mapStatus(t.response)
            is ResponseException -> mapStatus(t.response)
            is HttpRequestTimeoutException, is TimeoutCancellationException -> NetworkError.Timeout(t.message, t)
            is IOException -> NetworkError.NoInternet(t.message, t)
            else -> NetworkError.Unknown(t.message, t)
        }
    }

    private fun mapStatus(response: HttpResponse): NetworkError {
        val code = response.status.value
        val message = response.status.description
        return when (code) {
            HttpStatusCode.Unauthorized.value -> NetworkError.Unauthorized(message)
            HttpStatusCode.Forbidden.value -> NetworkError.Forbidden(message)
            HttpStatusCode.NotFound.value -> NetworkError.NotFound(message)
            HttpStatusCode.Conflict.value -> NetworkError.Conflict(message)
            HttpStatusCode.BadRequest.value -> NetworkError.BadRequest(message)
            in 500..599 -> NetworkError.ServerError(code, message)
            else -> NetworkError.Unknown("HTTP $code: $message")
        }
    }
}