package com.billioapp.features.auth.presentation.emailverification

import com.billioapp.core.mvi.UiEffect
import com.billioapp.core.mvi.UiEvent
import com.billioapp.core.mvi.UiState

data class EmailVerificationState(
    val userEmail: String = "",
    val isCheckingVerification: Boolean = false,
    val isResendingEmail: Boolean = false,
    val errorMessage: String? = null,
    val resendSuccess: Boolean = false
) : UiState

sealed class EmailVerificationEvent : UiEvent {
    object CheckVerification : EmailVerificationEvent()
    object ResendEmail : EmailVerificationEvent()
    object Logout : EmailVerificationEvent()
}

sealed class EmailVerificationEffect : UiEffect {
    object NavigateToHome : EmailVerificationEffect()
    object NavigateToLogin : EmailVerificationEffect()
    data class ShowSnackbar(val message: String) : EmailVerificationEffect()
}
