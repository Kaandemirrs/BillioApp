package com.billioapp.data.mapper

import com.billioapp.domain.model.ai.AiAnalysisReport
import com.billioapp.domain.model.ai.AiPriceSuggestion
import com.billioapp.shared.data.remote.ai.dto.AnalyzeSubscriptionsResponseDto
import com.billioapp.shared.data.remote.ai.dto.GetPriceResponseDto

class AiMapper {
    fun mapPrice(dto: GetPriceResponseDto): AiPriceSuggestion {
        val text = when {
            !dto.priceAnalysisText.isNullOrBlank() -> dto.priceAnalysisText
            dto.suggestedPrice != null -> "AI Önerisi: ${formatPrice(dto.suggestedPrice)} TRY"
            else -> "Fiyat bulunamadı"
        }

        return AiPriceSuggestion(
            priceAnalysisText = text,
            serviceName = dto.serviceName,
            searchPerformed = dto.searchPerformed,
            sourcesFound = dto.sourcesFound
        )
    }

    private fun formatPrice(value: Double): String {
        // KMP uyumlu iki ondalık formatlama: %.2f yerine manuel biçimleme
        // Yuvarlama: en yakın iki ondalık basamağa
        val scaled = kotlin.math.round(value * 100.0).toLong()
        val whole = scaled / 100
        val fraction = kotlin.math.abs((scaled % 100).toInt())
        return "$whole.${fraction.toString().padStart(2, '0')}"
    }

    fun mapAnalysis(dto: AnalyzeSubscriptionsResponseDto): AiAnalysisReport = AiAnalysisReport(
        analysisText = dto.analysisText,
        totalSubscriptions = dto.totalSubscriptions,
        activeSubscriptions = dto.activeSubscriptions,
        monthlyTotal = dto.monthlyTotal,
        yearlyTotal = dto.yearlyTotal
    )
}