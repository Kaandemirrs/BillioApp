package com.billioapp.data.remote.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteAccountRequestDto(
    @SerialName("confirmation") val confirmation: String
)
