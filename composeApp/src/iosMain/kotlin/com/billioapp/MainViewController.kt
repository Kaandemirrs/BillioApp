package com.billioapp

import androidx.compose.ui.window.ComposeUIViewController
import com.billioapp.app.App

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}