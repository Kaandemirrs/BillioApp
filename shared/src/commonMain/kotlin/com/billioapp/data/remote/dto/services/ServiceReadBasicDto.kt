package com.billioapp.data.remote.dto.services

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceReadBasicDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("logo_url") val logoUrl: String? = null,
    @SerialName("category") val category: String? = null,
    @SerialName("description") val description: String? = null
)

