package com.billioapp.data.remote.dto.analysis

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AiAnalysisResponseDto(
    @SerialName("reportText") val reportText: String,
    @SerialName("searchQuery") val searchQuery: String? = null,
    @SerialName("sourcesFound") val sourcesFound: Int = 0,
    @SerialName("format") val format: String? = null
)