package com.billioapp.features.profile.presentation

import androidx.lifecycle.viewModelScope
import com.billioapp.core.mvi.BaseViewModel
import com.billioapp.domain.usecase.auth.LogoutUseCase
import com.billioapp.domain.util.Result
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel<ProfileState, ProfileEvent, ProfileEffect>(
    initialState = ProfileState()
) {

    override fun handleEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.LogoutClicked -> logout()
            ProfileEvent.EditProfileClicked -> showComingSoon()
        }
    }

    private fun logout() {
        if (currentState.isLoggingOut) return
        setState(currentState.copy(isLoggingOut = true, error = null))
        viewModelScope.launch {
            when (val result = logoutUseCase()) {
                is Result.Success -> {
                    setState(currentState.copy(isLoggingOut = false))
                    setEffect(ProfileEffect.NavigateToLogin)
                }
                is Result.Error -> {
                    setState(currentState.copy(isLoggingOut = false, error = result.message))
                    setEffect(ProfileEffect.ShowError(result.message.ifBlank { "Çıkış yapılırken bir hata oluştu" }))
                }
            }
        }
    }

    private fun showComingSoon() {
        setEffect(ProfileEffect.ShowSnackbar("Profil düzenleme yakında! Henüz kullanıma hazır değil."))
    }
}