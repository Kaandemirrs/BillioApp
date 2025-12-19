package com.billioapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.google.firebase.FirebaseApp
import com.billioapp.di.appModules

import com.billioapp.BuildConfig
import com.billioapp.di.appModules
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.LogLevel

import com.revenuecat.purchases.kmp.configure
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin


class BillioApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Napier logging early
        Napier.base(DebugAntilog())
        Napier.i(tag = "BillioApplication", message = "Napier initialized for Android")

        FirebaseApp.initializeApp(this)

        // Ensure Koin is started so services can inject shared dependencies
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidContext(this@BillioApplication)
                modules(appModules)
            }
        }

        try {
            Purchases.logLevel = LogLevel.DEBUG
            val apiKey = BuildConfig.RC_API_KEY
            if (apiKey.isNullOrBlank()) {
                Napier.w(tag = "RevenueCat", message = "RC_API_KEY boş. Lütfen BuildConfig’e Public API Key ekleyin (goog_... veya appl_...).")
            } else {
                Purchases.configure(apiKey)
                Napier.i(tag = "RevenueCat", message = "RevenueCat Purchases SDK konfigüre edildi")
            }
        } catch (t: Throwable) {
            Napier.e(tag = "RevenueCat", message = "RevenueCat konfigürasyonu başarısız: ${t.message}", throwable = t)
        }

        // Create notification channel for Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "billio_default_channel"
            val name = "Billio Notifications"
            val descriptionText = "General notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
