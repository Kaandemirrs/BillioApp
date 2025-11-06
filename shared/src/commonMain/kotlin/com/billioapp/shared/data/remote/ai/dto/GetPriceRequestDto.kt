package com.billioapp.shared.data.remote.ai.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetPriceRequestDto(
    @SerialName("service_name") val serviceName: String
)