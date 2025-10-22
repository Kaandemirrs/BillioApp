package com.billioapp.core.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.billioapp.core.theme.BillioAppTheme

@Composable
fun BillioNavigationGraph() {
    BillioAppTheme {
        Navigator(screen = OnboardingRoute())
    }
}
