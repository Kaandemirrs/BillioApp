package com.billioapp.di

import com.billioapp.data.mapper.UserMapper
import com.billioapp.data.repository.AuthRepositoryImpl
import com.billioapp.domain.repository.AuthRepository
import com.billioapp.domain.usecase.auth.CheckEmailVerifiedUseCase
import com.billioapp.domain.usecase.auth.ForgotPasswordUseCase
import com.billioapp.domain.usecase.auth.GetCurrentUserUseCase
import com.billioapp.domain.usecase.auth.LoginUseCase
import com.billioapp.domain.usecase.auth.LogoutUseCase
import com.billioapp.domain.usecase.auth.RegisterUseCase
import com.billioapp.domain.usecase.auth.ReloadUserUseCase
import com.billioapp.domain.usecase.auth.SendVerificationEmailUseCase
import com.billioapp.domain.usecase.auth.GoogleSignInUseCase
import com.billioapp.domain.usecase.auth.AppleSignInUseCase
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import org.koin.dsl.module

val authModule = module {
    single { Firebase.auth }
    single { UserMapper() }
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }
    factory { ForgotPasswordUseCase(get()) }
    factory { SendVerificationEmailUseCase(get()) }
    factory { CheckEmailVerifiedUseCase(get()) }
    factory { ReloadUserUseCase(get()) }
    factory { GoogleSignInUseCase(get()) }
    factory { AppleSignInUseCase(get()) }
}
