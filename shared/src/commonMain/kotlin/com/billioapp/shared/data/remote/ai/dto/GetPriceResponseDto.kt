package com.billioapp.shared.data.remote.ai.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetPriceResponseDto(
    @SerialName("price_analysis_text") val priceAnalysisText: String? = null,
    @SerialName("suggested_price") val suggestedPrice: Double? = null, // Geri uyumluluk için
    @SerialName("service_name") val serviceName: String,
    @SerialName("search_performed") val searchPerformed: Boolean,
    @SerialName("sources_found") val sourcesFound: Int
)