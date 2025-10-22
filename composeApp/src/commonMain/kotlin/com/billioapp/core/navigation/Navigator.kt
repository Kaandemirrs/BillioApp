package com.billioapp.core.navigation

import cafe.adriel.voyager.navigator.Navigator

fun Navigator.goToLogin() {
    replaceAll(LoginRoute())
}

fun Navigator.goToHome() {
    replaceAll(HomeRoute())
}

fun Navigator.goToOnboarding() {
    replaceAll(OnboardingRoute())
}
