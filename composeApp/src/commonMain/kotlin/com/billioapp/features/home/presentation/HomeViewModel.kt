package com.billioapp.features.home.presentation

import androidx.lifecycle.viewModelScope
import com.billioapp.core.mvi.BaseViewModel
import com.billioapp.domain.usecase.auth.LogoutUseCase
import com.billioapp.domain.util.Result
import kotlinx.coroutines.launch

class HomeViewModel(
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>(
    initialState = HomeState()
) {

    override fun handleEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.Logout -> logout()
        }
    }

    private fun logout() {
        if (currentState.isLoggingOut) return

        viewModelScope.launch {
            setState(currentState.copy(isLoggingOut = true))
            when (val result = logoutUseCase()) {
                is Result.Success -> {
                    setState(currentState.copy(isLoggingOut = false))
                    setEffect(HomeEffect.NavigateToLogin)
                }
                is Result.Error -> {
                    setState(currentState.copy(isLoggingOut = false))
                    val message = result.message.ifBlank { "Çıkış yapılırken bir hata oluştu" }
                    setEffect(HomeEffect.ShowError(message))
                }
            }
        }
    }
}
