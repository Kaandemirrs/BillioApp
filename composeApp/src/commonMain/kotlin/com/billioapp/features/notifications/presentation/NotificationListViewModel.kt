package com.billioapp.features.notifications.presentation

import androidx.lifecycle.viewModelScope
import com.billioapp.core.mvi.BaseViewModel
import com.billioapp.domain.usecase.notifications.GetNotificationsUseCase
import com.billioapp.domain.util.Result
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

class NotificationListViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase
) : BaseViewModel<NotificationListState, NotificationListEvent, NotificationListEffect>(
    initialState = NotificationListState()
) {

    override fun handleEvent(event: NotificationListEvent) {
        when (event) {
            NotificationListEvent.Load, NotificationListEvent.Refresh -> loadNotifications()
        }
    }

    private fun loadNotifications() {
        if (currentState.isLoading) return
        viewModelScope.launch {
            setState(currentState.copy(isLoading = true, error = null))
            when (val result = getNotificationsUseCase()) {
                is Result.Success -> {
                    setState(currentState.copy(isLoading = false, notifications = result.data))
                }
                is Result.Error -> {
                    val msg = result.message.ifBlank { "Bildirimler yüklenemedi" }
                    Napier.e(tag = "NotificationListViewModel", message = msg, throwable = result.throwable)
                    setState(currentState.copy(isLoading = false, error = msg))
                    setEffect(NotificationListEffect.ShowError(msg))
                }
            }
        }
    }
}