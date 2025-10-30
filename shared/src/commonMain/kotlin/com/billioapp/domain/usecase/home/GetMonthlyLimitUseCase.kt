package com.billioapp.domain.usecase.home

import com.billioapp.domain.model.home.MonthlyLimit
import com.billioapp.domain.repository.HomeRepository
import com.billioapp.domain.util.Result

class GetMonthlyLimitUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<MonthlyLimit?> = repository.getMonthlyLimit()
}