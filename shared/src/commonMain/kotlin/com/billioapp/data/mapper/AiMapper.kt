package com.billioapp.data.mapper

import com.billioapp.domain.model.ai.AiAnalysisReport
import com.billioapp.domain.model.ai.AiPriceSuggestion
import com.billioapp.shared.data.remote.ai.dto.AnalyzeSubscriptionsResponseDto
import com.billioapp.shared.data.remote.ai.dto.GetPriceResponseDto

class AiMapper {
    fun mapPrice(dto: GetPriceResponseDto): AiPriceSuggestion = AiPriceSuggestion(
        priceAnalysisText = dto.priceAnalysisText,
        serviceName = dto.serviceName,
        searchPerformed = dto.searchPerformed,
        sourcesFound = dto.sourcesFound
    )

    fun mapAnalysis(dto: AnalyzeSubscriptionsResponseDto): AiAnalysisReport = AiAnalysisReport(
        analysisText = dto.analysisText,
        totalSubscriptions = dto.totalSubscriptions,
        activeSubscriptions = dto.activeSubscriptions,
        monthlyTotal = dto.monthlyTotal,
        yearlyTotal = dto.yearlyTotal
    )
}