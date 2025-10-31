package com.billioapp.features.profile.presentation

import com.billioapp.core.mvi.UiEffect
import com.billioapp.core.mvi.UiEvent
import com.billioapp.core.mvi.UiState

data class ProfileState(
    val isLoggingOut: Boolean = false,
    val error: String? = null
) : UiState

sealed class ProfileEvent : UiEvent {
    object LogoutClicked : ProfileEvent()
    object EditProfileClicked : ProfileEvent()
}

sealed class ProfileEffect : UiEffect {
    object NavigateToLogin : ProfileEffect()
    data class ShowError(val message: String) : ProfileEffect()
    data class ShowSnackbar(val message: String) : ProfileEffect()
}