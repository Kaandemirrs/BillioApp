package com.billioapp.features.notifications.presentation

import com.billioapp.core.mvi.UiEffect
import com.billioapp.core.mvi.UiEvent
import com.billioapp.core.mvi.UiState
import com.billioapp.domain.model.Notification

data class NotificationListState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val error: String? = null
) : UiState

sealed interface NotificationListEvent : UiEvent {
    data object Load : NotificationListEvent
    data object Refresh : NotificationListEvent
}

sealed interface NotificationListEffect : UiEffect {
    data class ShowError(val message: String) : NotificationListEffect
}