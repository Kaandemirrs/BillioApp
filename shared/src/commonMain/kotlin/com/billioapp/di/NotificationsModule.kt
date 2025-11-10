package com.billioapp.di

import com.billioapp.data.mapper.NotificationsMapper
import com.billioapp.data.remote.api.NotificationsApi
import com.billioapp.data.repository.NotificationsRepositoryImpl
import com.billioapp.domain.repository.NotificationsRepository
import com.billioapp.domain.usecase.notifications.GetNotificationsUseCase
import org.koin.dsl.module

val notificationsModule = module {
    // API & Mapper
    single { NotificationsApi(get()) }
    single { NotificationsMapper() }

    // Repository
    single<NotificationsRepository> { NotificationsRepositoryImpl(get(), get()) }

    // Use case
    factory { GetNotificationsUseCase(get()) }
}