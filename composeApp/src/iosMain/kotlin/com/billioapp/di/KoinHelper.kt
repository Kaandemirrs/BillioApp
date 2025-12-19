package com.billioapp.di


import org.koin.core.context.startKoin
import com.billioapp.di.appModules

fun initKoin() {
    startKoin {
        modules(appModules)
    }
}
