package com.billioapp.domain.usecase.ai

import com.billioapp.domain.model.ai.FinancialAnalysis
import com.billioapp.domain.repository.AiRepository
import com.billioapp.domain.util.Result

class GetFinancialAnalysisUseCase(
    private val repository: AiRepository
) {
    suspend operator fun invoke(): Result<FinancialAnalysis> = repository.getFinancialAnalysis()
}