package com.billioapp.domain.repository

import com.billioapp.domain.model.services.Service
import com.billioapp.domain.model.services.ServicePlan

interface ServicesRepository {
    suspend fun searchServices(query: String): List<Service>
    suspend fun getServicePlans(serviceId: String): List<ServicePlan>
}