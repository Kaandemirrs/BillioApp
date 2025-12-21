package com.billioapp.di

import com.billioapp.data.remote.client.ApiConfig
import com.billioapp.data.remote.client.provideHttpClient
import com.billioapp.data.remote.api.UserApi
import com.billioapp.data.remote.api.UserApiImpl
import dev.gitlive.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val homeModule = module {
    // Base URL: KMP uyumlu sabit tanım (env okunması common’da desteklenmiyor)
    single { ApiConfig(baseUrl = "https://billio-backend.onrender.com") }

    // TokenProvider (Firebase)
    single<com.billioapp.data.remote.auth.TokenProvider> { com.billioapp.data.remote.auth.FirebaseTokenProvider(get()) }

    // HttpClient provider (Bearer Auth ile)
    single { provideHttpClient(get<ApiConfig>(), get<com.billioapp.data.remote.auth.TokenProvider>()) }

    // API ve mapper
    single { com.billioapp.data.remote.api.HomeApi(get()) }
    single { com.billioapp.data.mapper.HomeMapper() }

    // Subscriptions API ve mapper/repo/usecase
    single { com.billioapp.data.remote.api.SubscriptionsApi(get()) }
    single { com.billioapp.data.mapper.SubscriptionsMapper() }
    single<com.billioapp.domain.repository.SubscriptionsRepository> { com.billioapp.data.repository.SubscriptionsRepositoryImpl(get(), get()) }

    // Repository
    single<com.billioapp.domain.repository.HomeRepository> { com.billioapp.data.repository.HomeRepositoryImpl(get(), get()) }

    // Use case’ler
    factory { com.billioapp.domain.usecase.home.GetHomeSummaryUseCase(get()) }
    factory { com.billioapp.domain.usecase.home.GetMonthlyLimitUseCase(get()) }

    // User API, Repository ve UseCase
    single<UserApi> { UserApiImpl(get()) }
    single<com.billioapp.domain.repository.UserRepository> { com.billioapp.data.repository.UserRepositoryImpl(get()) }
    factory { com.billioapp.domain.usecase.user.RegisterDeviceUseCase(get()) }
    factory { com.billioapp.domain.usecase.user.DeleteAccountUseCase(get()) }
    factory { com.billioapp.domain.usecase.home.UpdateMonthlyLimitUseCase(get()) }
    factory { com.billioapp.domain.usecase.subscriptions.GetSubscriptionsUseCase(get()) }
    factory { com.billioapp.domain.usecase.subscriptions.AddSubscriptionUseCase(get()) }
    factory { com.billioapp.domain.usecase.subscriptions.DeleteSubscriptionUseCase(get()) }
}
