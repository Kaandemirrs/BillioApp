package com.billioapp.data.remote.client

import kotlinx.serialization.Serializable

@Serializable
data class ApiConfig(
    val baseUrl: String
)