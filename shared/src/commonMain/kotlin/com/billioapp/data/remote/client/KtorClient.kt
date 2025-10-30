package com.billioapp.data.remote.client

import io.ktor.client.HttpClient

import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.HttpTimeout
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.json
import io.github.aakira.napier.Napier
import com.billioapp.data.remote.auth.TokenProvider
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer

fun provideHttpClient(
    apiConfig: ApiConfig,
    tokenProvider: TokenProvider,
): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                }
            )
        }
        // Timeout: Render cold start ve yavaş yanıtlar için artırıldı
        install(HttpTimeout) {
            connectTimeoutMillis = 20_000
            requestTimeoutMillis = 60_000
            socketTimeoutMillis = 60_000
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.d(tag = "Ktor", message = message)
                }
            }
            level = LogLevel.ALL
        }
        install(Auth) {
            bearer {
                sendWithoutRequest { true }
                loadTokens { tokenProvider.loadTokens() }
                refreshTokens { tokenProvider.refreshTokens(oldTokens) }
            }
        }
        defaultRequest {
            // Base URL
            url.takeFrom(
                URLBuilder(apiConfig.baseUrl)
            )
        }
    }
}