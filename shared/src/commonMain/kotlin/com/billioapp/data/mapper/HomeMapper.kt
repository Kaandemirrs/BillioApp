package com.billioapp.data.mapper

import com.billioapp.data.remote.dto.home.CategorySpendDto
import com.billioapp.data.remote.dto.home.HomeSummaryDto
import com.billioapp.data.remote.dto.home.MonthlyLimitDto
import com.billioapp.domain.model.home.CategorySpend
import com.billioapp.domain.model.home.HomeSummary
import com.billioapp.domain.model.home.MonthlyLimit

class HomeMapper {
    fun mapSummary(dto: HomeSummaryDto): HomeSummary = HomeSummary(
        totalAmount = dto.totalAmount,
        currency = dto.currency,
        categories = dto.categories.map(::mapCategory),
        monthlyLimit = dto.monthlyLimit
    )

    fun mapCategory(dto: CategorySpendDto): CategorySpend = CategorySpend(
        name = dto.name,
        amount = dto.amount,
        colorHex = dto.colorHex
    )

    fun mapLimit(dto: MonthlyLimitDto): MonthlyLimit = MonthlyLimit(
        amount = dto.amount,
        currency = dto.currency
    )
}