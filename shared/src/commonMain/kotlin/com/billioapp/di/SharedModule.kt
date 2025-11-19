package com.billioapp.di

import org.koin.core.module.Module

// Shared Koin modules aggregated for platforms
val sharedModules: List<Module> = listOf(
    authModule,
    homeModule,
    aiModule,
    notificationsModule,
    servicesModule
)
