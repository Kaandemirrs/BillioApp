package com.billioapp.domain.usecase.services

import com.billioapp.domain.model.services.ServicePlan
import com.billioapp.domain.repository.ServicesRepository

class GetServicePlansUseCase(
    private val repository: ServicesRepository
) {
    suspend operator fun invoke(serviceId: String): Result<List<ServicePlan>> = runCatching {
        repository.getServicePlans(serviceId)
    }
}