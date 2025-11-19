package com.billioapp.data.remote.dto.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponseDto<T>(
    @SerialName("success") val success: Boolean,
    @SerialName("message") val message: String? = null,
    @SerialName("data") val data: T? = null,
    @SerialName("error") val error: ErrorDto? = null
)

@Serializable
data class ErrorDto(
    @SerialName("code") val code: String? = null,
    @SerialName("message") val message: String? = null
)