package com.billioapp

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.billioapp.domain.usecase.user.RegisterDeviceUseCase
import com.billioapp.domain.util.Result
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext

class BillioMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title ?: message.data["title"] ?: "Bildirim"
        val body = message.notification?.body ?: message.data["message"] ?: ""

        val builder = NotificationCompat.Builder(this, "billio_default_channel")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    override fun onNewToken(token: String) {
        Napier.i(tag = "BillioMessagingService", message = "FCM token yenilendi")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val registerDeviceUseCase = GlobalContext.get().get<RegisterDeviceUseCase>()
                when (val res = registerDeviceUseCase(token)) {
                    is Result.Error -> Napier.e(tag = "BillioMessagingService", message = "Cihaz kaydı hatası: ${res.message}")
                    else -> Napier.i(tag = "BillioMessagingService", message = "Cihaz kaydı başarıyla gönderildi")
                }
            } catch (t: Throwable) {
                Napier.e(tag = "BillioMessagingService", message = "Cihaz kaydı yapılamadı", throwable = t)
            }
        }
    }
}