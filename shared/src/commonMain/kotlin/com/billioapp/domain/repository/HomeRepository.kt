package com.billioapp.domain.repository

import com.billioapp.domain.model.home.HomeSummary
import com.billioapp.domain.model.home.MonthlyLimit
import com.billioapp.domain.util.Result

interface HomeRepository {
    suspend fun getHomeSummary(): Result<HomeSummary>
    suspend fun getMonthlyLimit(): Result<MonthlyLimit?>
    suspend fun updateMonthlyLimit(amount: Double, currency: String): Result<MonthlyLimit>
}