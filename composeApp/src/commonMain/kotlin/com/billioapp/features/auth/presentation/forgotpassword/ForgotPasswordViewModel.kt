package com.billioapp.features.auth.presentation.forgotpassword

import androidx.lifecycle.viewModelScope
import com.billioapp.core.mvi.BaseViewModel
import com.billioapp.domain.usecase.auth.ForgotPasswordUseCase
import com.billioapp.domain.util.Result
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : BaseViewModel<ForgotPasswordState, ForgotPasswordEvent, ForgotPasswordEffect>(
    initialState = ForgotPasswordState()
) {

    override fun handleEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.EmailChanged -> onEmailChanged(event.email)
            ForgotPasswordEvent.SendResetEmail -> sendResetEmail()
            ForgotPasswordEvent.BackToLogin -> {
                setEffect(ForgotPasswordEffect.NavigateBack(currentState.emailSent))
            }
        }
    }

    private fun onEmailChanged(email: String) {
        setState(
            currentState.copy(
                email = email,
                errorMessage = null,
                emailSent = false
            )
        )
    }

    private fun sendResetEmail() {
        if (currentState.isLoading) return

        val trimmedEmail = currentState.email.trim()

        viewModelScope.launch {
            setState(currentState.copy(isLoading = true, errorMessage = null))
            when (val result = forgotPasswordUseCase(trimmedEmail)) {
                is Result.Success -> {
                    setState(
                        currentState.copy(
                            isLoading = false,
                            emailSent = true,
                            errorMessage = null,
                            email = trimmedEmail
                        )
                    )
                    setEffect(
                        ForgotPasswordEffect.ShowSnackbar(
                            "Şifre sıfırlama bağlantısı email adresinize gönderildi."
                        )
                    )
                }
                is Result.Error -> {
                    setState(
                        currentState.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    )
                }
            }
        }
    }
}
