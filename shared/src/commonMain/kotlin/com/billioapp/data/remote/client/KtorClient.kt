package com.billioapp.data.remote.client

import dev.gitlive.firebase.auth.FirebaseAuth
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.json
import io.github.aakira.napier.Napier

fun provideHttpClient(
    apiConfig: ApiConfig,
): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                }
            )
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.d(tag = "Ktor", message = message)
                }
            }
            level = LogLevel.BODY
        }
        defaultRequest {
            // Base URL
            url.takeFrom(
                URLBuilder(apiConfig.baseUrl)
            )
        }
    }
}