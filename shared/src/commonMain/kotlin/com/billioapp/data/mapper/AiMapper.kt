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

    private fun formatPrice(value: Double): String =
        kotlin.runCatching { String.format("%.2f", value) }.getOrElse { value.toString() }

    fun mapAnalysis(dto: AnalyzeSubscriptionsResponseDto): AiAnalysisReport = AiAnalysisReport(
        analysisText = dto.analysisText,
        totalSubscriptions = dto.totalSubscriptions,
        activeSubscriptions = dto.activeSubscriptions,
        monthlyTotal = dto.monthlyTotal,
        yearlyTotal = dto.yearlyTotal
    )
}