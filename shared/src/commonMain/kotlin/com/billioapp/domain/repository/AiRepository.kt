package com.billioapp.domain.repository

import com.billioapp.domain.model.ai.AiAnalysisReport
import com.billioapp.domain.model.ai.AiPriceSuggestion
import com.billioapp.domain.model.subscriptions.Subscription
import com.billioapp.domain.util.Result

interface AiRepository {
    suspend fun getPriceSuggestion(serviceName: String): Result<AiPriceSuggestion>
    suspend fun getAnalysis(subscriptions: List<Subscription>): Result<AiAnalysisReport>
}