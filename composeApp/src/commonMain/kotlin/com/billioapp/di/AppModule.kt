package com.billioapp.di

import com.billioapp.features.auth.presentation.emailverification.EmailVerificationViewModel
import com.billioapp.features.auth.presentation.forgotpassword.ForgotPasswordViewModel
import com.billioapp.features.auth.presentation.login.LoginViewModel
import com.billioapp.features.auth.presentation.register.RegisterViewModel
import com.billioapp.features.home.presentation.HomeViewModel
import com.billioapp.features.onboarding.presentation.OnboardingViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentationModule = module {
    factory { OnboardingViewModel() }
    factory { LoginViewModel(get(), get(), get(), get(), get(), get(), get()) }
    factory { RegisterViewModel(get(), get()) }
    factory { HomeViewModel(get()) }
    factory { ForgotPasswordViewModel(get()) }
    factory { EmailVerificationViewModel(get(), get(), get(), get(), get()) }
}

val appModules: List<Module> = listOf(
    authModule,
    homeModule,
    presentationModule
)