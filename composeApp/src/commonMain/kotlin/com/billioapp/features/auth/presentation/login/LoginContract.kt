package com.billioapp.features.auth.presentation.login

import com.billioapp.core.mvi.UiEvent
import com.billioapp.core.mvi.UiState
import com.billioapp.core.mvi.UiEffect

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isGoogleLoading: Boolean = false,
    val isAppleLoading: Boolean = false,
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val errorMessage: String? = null
) : UiState

sealed class LoginEvent : UiEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object TogglePasswordVisibility : LoginEvent()
    object LoginClicked : LoginEvent()
    object GoogleLoginClicked : LoginEvent()
    data class GoogleIdTokenReceived(val idToken: String) : LoginEvent()
    data class GoogleSignInFailed(val message: String? = null) : LoginEvent()
    object AppleLoginClicked : LoginEvent()
    data class AppleIdTokenReceived(val idToken: String, val nonce: String) : LoginEvent()
    data class AppleSignInFailed(val message: String? = null) : LoginEvent()
    object CreateAccountClicked : LoginEvent()
    object ClearError : LoginEvent()
}

sealed class LoginEffect : UiEffect {
    object NavigateToMain : LoginEffect()
    object NavigateToSignup : LoginEffect()
    object NavigateToEmailVerification : LoginEffect()
    object RequestGoogleSignIn : LoginEffect()
    object RequestAppleSignIn : LoginEffect()
    data class ShowError(val message: String) : LoginEffect()
}