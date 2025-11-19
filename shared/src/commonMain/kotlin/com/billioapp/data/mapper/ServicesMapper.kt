package com.billioapp.data.mapper

import com.billioapp.data.remote.dto.services.ServicePlanReadBasicDto
import com.billioapp.data.remote.dto.services.ServiceReadBasicDto
import com.billioapp.domain.model.services.Service
import com.billioapp.domain.model.services.ServicePlan

class ServicesMapper {
    fun mapService(dto: ServiceReadBasicDto): Service = Service(
        id = dto.id,
        name = dto.name,
        logoUrl = dto.logoUrl,
        category = dto.category
    )

    fun mapServices(list: List<ServiceReadBasicDto>): List<Service> = list.map(::mapService)

    fun mapPlan(dto: ServicePlanReadBasicDto): ServicePlan = ServicePlan(
        id = dto.id,
        planName = dto.planName,
        cachedPrice = dto.cachedPrice,
        currency = dto.currency,
        billingCycle = dto.billingCycle
    )

    fun mapPlans(list: List<ServicePlanReadBasicDto>): List<ServicePlan> = list.map(::mapPlan)
}