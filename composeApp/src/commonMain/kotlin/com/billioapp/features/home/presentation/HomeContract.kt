package com.billioapp.features.home.presentation

import com.billioapp.core.mvi.UiEffect
import com.billioapp.core.mvi.UiEvent
import com.billioapp.core.mvi.UiState

data class HomeState(
    val isLoggingOut: Boolean = false
) : UiState

sealed class HomeEvent : UiEvent {
    object Logout : HomeEvent()
}

sealed class HomeEffect : UiEffect {
    object NavigateToLogin : HomeEffect()
    data class ShowError(val message: String) : HomeEffect()
}
