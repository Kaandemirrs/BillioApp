package com.billioapp.domain.usecase.ai

import com.billioapp.domain.model.ai.AiPriceSuggestion
import com.billioapp.domain.repository.AiRepository
import com.billioapp.domain.util.Result

class GetAiPriceSuggestionUseCase(
    private val repository: AiRepository
) {
    suspend operator fun invoke(serviceName: String): Result<AiPriceSuggestion> {
        val name = serviceName.trim()
        if (name.isBlank()) {
            return Result.Error("Lütfen geçerli bir servis adı girin")
        }
        return repository.getPriceSuggestion(name)
    }
}