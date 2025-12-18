package com.billioapp.data.repository

import com.billioapp.data.mapper.ServicesMapper
import com.billioapp.data.remote.api.ServicesApi
import com.billioapp.domain.model.services.Service
import com.billioapp.domain.model.services.ServicePlan
import com.billioapp.domain.repository.ServicesRepository

class ServicesRepositoryImpl(
    private val api: ServicesApi,
    private val mapper: ServicesMapper
) : ServicesRepository {
    override suspend fun searchServices(query: String): List<Service> {
        return mapper.mapServices(api.searchServices(query))
    }

    override suspend fun getServicePlans(serviceId: String): List<ServicePlan> {
        return mapper.mapPlans(api.getServicePlans(serviceId))
    }
}

