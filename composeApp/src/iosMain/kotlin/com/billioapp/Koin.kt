package com.billioapp

import com.billioapp.di.appModules
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

private var koinApp: KoinApplication? = null

fun initKoin(appDeclaration: KoinAppDeclaration = {}): Koin {
    val launchedApp = koinApp ?: startKoin {
        appDeclaration()
        modules(appModules)
    }.also { koinApp = it }
    return launchedApp.koin
}
