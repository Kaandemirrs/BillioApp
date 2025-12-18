package com.billioapp.domain.usecase.ai

import com.billioapp.data.remote.dto.ai.SmartPriceResponseDto
import com.billioapp.domain.repository.AiRepository
import com.billioapp.domain.util.Result

class GetSmartPriceUseCase(private val repository: AiRepository) {
    suspend operator fun invoke(serviceName: String, planName: String = "Standart Plan"): Result<SmartPriceResponseDto> {
        return repository.getSmartPrice(serviceName, planName)
    }
}

