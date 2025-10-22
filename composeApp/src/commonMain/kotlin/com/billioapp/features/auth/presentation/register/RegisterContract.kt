package com.billioapp.features.auth.presentation.register

import com.billioapp.core.mvi.UiEffect
import com.billioapp.core.mvi.UiEvent
import com.billioapp.core.mvi.UiState

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val isConfirmPasswordValid: Boolean = true,
    val errorMessage: String? = null
) : UiState

sealed class RegisterEvent : UiEvent {
    data class EmailChanged(val email: String) : RegisterEvent()
    data class PasswordChanged(val password: String) : RegisterEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegisterEvent()
    object TogglePasswordVisibility : RegisterEvent()
    object ToggleConfirmPasswordVisibility : RegisterEvent()
    object RegisterClicked : RegisterEvent()
    object NavigateToLogin : RegisterEvent()
    object ClearError : RegisterEvent()
}

sealed class RegisterEffect : UiEffect {
    object NavigateToEmailVerification : RegisterEffect()
    object NavigateBackToLogin : RegisterEffect()
    data class ShowError(val message: String) : RegisterEffect()
}
