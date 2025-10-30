package com.billioapp.domain.usecase.home

import com.billioapp.domain.model.home.MonthlyLimit
import com.billioapp.domain.repository.HomeRepository
import com.billioapp.domain.util.Result

class UpdateMonthlyLimitUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(amount: Double, currency: String): Result<MonthlyLimit> {
        if (amount <= 0) return Result.Error("Limit sıfırdan büyük olmalı")
        if (currency.isBlank()) return Result.Error("Geçerli bir para birimi girin")
        return repository.updateMonthlyLimit(amount, currency.trim())
    }
}