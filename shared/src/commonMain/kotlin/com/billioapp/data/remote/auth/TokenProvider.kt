package com.billioapp.data.remote.auth

import io.ktor.client.plugins.auth.providers.BearerTokens

interface TokenProvider {
    fun loadTokens(): BearerTokens?
    suspend fun refreshTokens(old: BearerTokens?): BearerTokens?
    fun setToken(token: String)
}