package com.billioapp.di

import com.billioapp.data.mapper.AiMapper
import com.billioapp.data.remote.api.AiApi
import com.billioapp.data.remote.api.AiApiImpl
import com.billioapp.data.repository.AiRepositoryImpl
import com.billioapp.domain.repository.AiRepository
import com.billioapp.domain.usecase.ai.GetAiAnalysisUseCase
import com.billioapp.domain.usecase.ai.GetAiPriceSuggestionUseCase
import com.billioapp.domain.usecase.ai.GetFinancialAnalysisUseCase
import org.koin.dsl.module

val aiModule = module {
    // API
    single<AiApi> { AiApiImpl(get()) }

    // Mapper
    single { AiMapper() }

    // Repository
    single<AiRepository> { AiRepositoryImpl(get(), get()) }

    // Use cases
    factory { GetAiPriceSuggestionUseCase(get()) }
    factory { GetAiAnalysisUseCase(get()) }
    factory { GetFinancialAnalysisUseCase(get()) }
}