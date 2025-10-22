package com.billioapp.features.auth.presentation.forgotpassword

import com.billioapp.core.mvi.UiEffect
import com.billioapp.core.mvi.UiEvent
import com.billioapp.core.mvi.UiState

data class ForgotPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val emailSent: Boolean = false,
    val errorMessage: String? = null
) : UiState

sealed class ForgotPasswordEvent : UiEvent {
    data class EmailChanged(val email: String) : ForgotPasswordEvent()
    object SendResetEmail : ForgotPasswordEvent()
    object BackToLogin : ForgotPasswordEvent()
}

sealed class ForgotPasswordEffect : UiEffect {
    data class NavigateBack(val resetStack: Boolean) : ForgotPasswordEffect()
    data class ShowSnackbar(val message: String) : ForgotPasswordEffect()
}
