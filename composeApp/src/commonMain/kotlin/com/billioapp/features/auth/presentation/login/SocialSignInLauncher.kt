package com.billioapp.features.auth.presentation.login

import androidx.compose.runtime.Composable

interface SocialSignInLauncher {
    fun launchGoogleSignIn()
    fun launchAppleSignIn()
}

@Composable
expect fun rememberSocialSignInLauncher(
    onGoogleSuccess: (String) -> Unit,
    onGoogleError: (String?) -> Unit,
    onAppleSuccess: (idToken: String, nonce: String) -> Unit,
    onAppleError: (String?) -> Unit
): SocialSignInLauncher
