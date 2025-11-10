package com.billioapp.di

import com.billioapp.features.auth.presentation.emailverification.EmailVerificationViewModel
import com.billioapp.features.auth.presentation.forgotpassword.ForgotPasswordViewModel
import com.billioapp.features.auth.presentation.login.LoginViewModel
import com.billioapp.features.auth.presentation.register.RegisterViewModel
import com.billioapp.features.home.presentation.HomeViewModel
import com.billioapp.features.onboarding.presentation.OnboardingViewModel
import com.billioapp.features.ai.presentation.AiViewModel
import com.billioapp.features.profile.presentation.ProfileViewModel
import com.billioapp.features.notifications.presentation.NotificationListViewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.core.module.dsl.factoryOf

private val presentationModule = module {
    factory { OnboardingViewModel() }
    factory { LoginViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    factory { RegisterViewModel(get(), get()) }
    factoryOf(::HomeViewModel)
    factory { ForgotPasswordViewModel(get()) }
    factory { EmailVerificationViewModel(get(), get(), get(), get(), get()) }
    factoryOf(::ProfileViewModel)
    factoryOf(::AiViewModel)
    factory { NotificationListViewModel(get()) }
}

val appModules: List<Module> = listOf(
    authModule,
    homeModule,
    aiModule,
    notificationsModule,
    presentationModule
)