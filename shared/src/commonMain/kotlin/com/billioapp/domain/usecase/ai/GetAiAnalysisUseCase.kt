package com.billioapp.domain.usecase.ai

import com.billioapp.domain.model.ai.AiAnalysisReport
import com.billioapp.domain.model.subscriptions.Subscription
import com.billioapp.domain.repository.AiRepository
import com.billioapp.domain.util.Result

class GetAiAnalysisUseCase(
    private val repository: AiRepository
) {
    suspend operator fun invoke(subscriptions: List<Subscription>): Result<AiAnalysisReport> {
        if (subscriptions.isEmpty()) {
            return Result.Error("Analiz için en az bir abonelik gerekli")
        }
        return repository.getAnalysis(subscriptions)
    }
}