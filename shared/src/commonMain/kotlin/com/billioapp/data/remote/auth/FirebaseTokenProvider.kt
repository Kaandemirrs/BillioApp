package com.billioapp.data.remote.auth

import dev.gitlive.firebase.auth.FirebaseAuth
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlin.concurrent.Volatile

class FirebaseTokenProvider(
    private val firebaseAuth: FirebaseAuth
) : TokenProvider {

    @Volatile
    private var cachedIdToken: String? = null

    override fun loadTokens(): BearerTokens? =
        cachedIdToken?.let { BearerTokens(it, "") }

    override suspend fun refreshTokens(old: BearerTokens?): BearerTokens? {
        val user = firebaseAuth.currentUser ?: return null
        val token = user.getIdToken(false)
        cachedIdToken = token
        return token?.let { BearerTokens(it, "") }
    }

    override fun setToken(token: String) {
        cachedIdToken = token
    }
}