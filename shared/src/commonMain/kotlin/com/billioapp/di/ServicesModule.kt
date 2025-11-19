package com.billioapp.di

import com.billioapp.data.mapper.ServicesMapper
import com.billioapp.data.remote.api.ServicesApi
import com.billioapp.data.remote.api.ServicesApiImpl
import com.billioapp.data.repository.ServicesRepositoryImpl
import com.billioapp.domain.repository.ServicesRepository
import com.billioapp.domain.usecase.services.GetServicePlansUseCase
import com.billioapp.domain.usecase.services.SearchServicesUseCase
import org.koin.dsl.module

val servicesModule = module {
    // API
    single<ServicesApi> { ServicesApiImpl(get()) }

    // Mapper
    single { ServicesMapper() }

    // Repository
    single<ServicesRepository> { ServicesRepositoryImpl(get(), get()) }

    // Use cases
    factory { SearchServicesUseCase(get()) }
    factory { GetServicePlansUseCase(get()) }
}