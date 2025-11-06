package com.billioapp.domain.model.ai

data class AiPriceSuggestion(
    val priceAnalysisText: String?,
    val serviceName: String,
    val searchPerformed: Boolean,
    val sourcesFound: Int
)

data class AiAnalysisReport(
    val analysisText: String?,
    val totalSubscriptions: Int,
    val activeSubscriptions: Int,
    val monthlyTotal: Double,
    val yearlyTotal: Double
)