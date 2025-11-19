package com.billioapp.domain.usecase.services

import com.billioapp.domain.model.services.Service
import com.billioapp.domain.repository.ServicesRepository

class SearchServicesUseCase(
    private val repository: ServicesRepository
) {
    suspend operator fun invoke(query: String): Result<List<Service>> = runCatching {
        repository.searchServices(query)
    }
}