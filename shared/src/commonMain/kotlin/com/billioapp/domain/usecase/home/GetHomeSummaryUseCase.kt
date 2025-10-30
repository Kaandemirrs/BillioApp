package com.billioapp.domain.usecase.home

import com.billioapp.domain.model.home.HomeSummary
import com.billioapp.domain.repository.HomeRepository
import com.billioapp.domain.util.Result

class GetHomeSummaryUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<HomeSummary> = repository.getHomeSummary()
}