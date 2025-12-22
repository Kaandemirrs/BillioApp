package com.billioapp.features.profile.presentation

import androidx.lifecycle.viewModelScope
import com.billioapp.core.mvi.BaseViewModel
import com.billioapp.domain.usecase.auth.LogoutUseCase
import com.billioapp.domain.usecase.user.DeleteAccountUseCase
import com.billioapp.domain.repository.PaymentRepository
import com.billioapp.domain.util.Result
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val paymentRepository: PaymentRepository
) : BaseViewModel<ProfileState, ProfileEvent, ProfileEffect>(
    initialState = ProfileState()
) {

    init {
        viewModelScope.launch {
            paymentRepository.isUserPremium()
                .onSuccess { isPremium ->
                    setState(currentState.copy(isPremium = isPremium))
                }
                .onFailure {
                    setState(currentState.copy(isPremium = false))
                }
            // Test ipucu: UI doğrulamak için aşağıdaki satırı aktif edebilirsin
             setState(currentState.copy(isPremium = false))
        }
    }

    override fun handleEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.LogoutClicked -> logout()
            ProfileEvent.EditProfileClicked -> showComingSoon()
            ProfileEvent.ConfirmDeleteAccount -> deleteAccount()
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

    private fun deleteAccount() {
        if (currentState.isDeletingAccount) return
        setState(currentState.copy(isDeletingAccount = true, error = null))
        viewModelScope.launch {
            when (val result = deleteAccountUseCase()) {
                is Result.Success -> {
                    when (val logout = logoutUseCase()) {
                        is Result.Success -> {
                            setState(currentState.copy(isDeletingAccount = false))
                            setEffect(ProfileEffect.NavigateToLogin)
                        }
                        is Result.Error -> {
                            setState(currentState.copy(isDeletingAccount = false))
                            setEffect(ProfileEffect.NavigateToLogin)
                        }
                    }
                }
                is Result.Error -> {
                    setState(currentState.copy(isDeletingAccount = false, error = result.message))
                    setEffect(ProfileEffect.ShowError(result.message.ifBlank { "Hesap silinirken bir hata oluştu" }))
                }
            }
        }
    }

    private fun showComingSoon() {
        setEffect(ProfileEffect.ShowSnackbar("Profil düzenleme yakında! Henüz kullanıma hazır değil."))
    }
}
