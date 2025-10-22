package com.billioapp

import android.app.Application
import com.google.firebase.FirebaseApp

class BillioApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
