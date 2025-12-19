package com.billioapp

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import com.billioapp.app.App

fun MainViewController(): UIViewController = ComposeUIViewController {
    App()
}