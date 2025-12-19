package com.billioapp.di

import com.billioapp.data.remote.api.PaymentApi
import com.billioapp.data.remote.api.PaymentApiImpl
import com.billioapp.data.repository.PaymentRepositoryImpl
import com.billioapp.domain.repository.PaymentRepository
import org.koin.dsl.module

val paymentModule = module {
    single<PaymentApi> { PaymentApiImpl(get()) }
    single<PaymentRepository> { PaymentRepositoryImpl(get()) }
}

