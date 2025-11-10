package com.billioapp

import androidx.compose.ui.window.ComposeUIViewController
import com.billioapp.app.App
import com.billioapp.domain.usecase.user.RegisterDeviceUseCase
import com.billioapp.domain.util.Result
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.messaging.messaging
import io.github.aakira.napier.Napier
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext
import platform.UserNotifications.*
import platform.UIKit.UIApplication

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
        // Request notification permission and register for remote notifications
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.requestAuthorization(options = UNAuthorizationOptionAlert or UNAuthorizationOptionBadge or UNAuthorizationOptionSound) { granted, error ->
            if (granted == true) {
                UIApplication.sharedApplication.registerForRemoteNotifications()
                // Best-effort: fetch FCM token and register device
                GlobalScope.launch {
                    try {
                        val token = Firebase.messaging.getToken()
                        if (!token.isNullOrBlank()) {
                            val registerDeviceUseCase = GlobalContext.get().get<RegisterDeviceUseCase>()
                            when (val res = registerDeviceUseCase(token)) {
                                is Result.Error -> Napier.e(tag = "iOS", message = "Cihaz kaydı hatası: ${res.message}")
                                else -> Napier.i(tag = "iOS", message = "Cihaz kaydı başarıyla gönderildi")
                            }
                        } else {
                            Napier.w(tag = "iOS", message = "FCM token boş; cihaz kaydı atlandı")
                        }
                    } catch (t: Throwable) {
                        Napier.e(tag = "iOS", message = "FCM token alma/kayıt hatası", throwable = t)
                    }
                }
            }
        }

        // Foreground FCM listener using GitLive; show local notification
        Firebase.messaging.onMessage { remoteMessage ->
            val title = remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: "Bildirim"
            val body = remoteMessage.notification?.body ?: remoteMessage.data["message"] ?: ""

            val content = UNMutableNotificationContent().apply {
                this.title = title
                this.body = body
            }
            val request = UNNotificationRequest.requestWithIdentifier(
                identifier = "billio_foreground_${platform.Foundation.NSDate().timeIntervalSince1970}",
                content = content,
                trigger = null
            )
            UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request) { _ -> }
        }
    }
) {
    App()
}