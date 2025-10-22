package com.billioapp.features.auth.presentation.register

import androidx.lifecycle.viewModelScope
import com.billioapp.core.mvi.BaseViewModel
import com.billioapp.domain.usecase.auth.RegisterUseCase
import com.billioapp.domain.usecase.auth.SendVerificationEmailUseCase
import com.billioapp.domain.util.Result
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val sendVerificationEmailUseCase: SendVerificationEmailUseCase
) : BaseViewModel<RegisterState, RegisterEvent, RegisterEffect>(
    initialState = RegisterState()
) {

    override fun handleEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.EmailChanged -> onEmailChanged(event.email)
            is RegisterEvent.PasswordChanged -> onPasswordChanged(event.password)
            is RegisterEvent.ConfirmPasswordChanged -> onConfirmPasswordChanged(event.confirmPassword)
            RegisterEvent.TogglePasswordVisibility -> togglePasswordVisibility()
            RegisterEvent.ToggleConfirmPasswordVisibility -> toggleConfirmPasswordVisibility()
            RegisterEvent.RegisterClicked -> attemptRegister()
            RegisterEvent.NavigateToLogin -> setEffect(RegisterEffect.NavigateBackToLogin)
            RegisterEvent.ClearError -> setState(currentState.copy(errorMessage = null))
        }
    }

    private fun onEmailChanged(email: String) {
        setState(
            currentState.copy(
                email = email,
                isEmailValid = emailRegex.matches(email.trim()),
                errorMessage = null
            )
        )
    }

    private fun onPasswordChanged(password: String) {
        setState(
            currentState.copy(
                password = password,
                isPasswordValid = password.length >= MIN_PASSWORD_LENGTH,
                isConfirmPasswordValid = password == currentState.confirmPassword,
                errorMessage = null
            )
        )
    }

    private fun onConfirmPasswordChanged(confirmPassword: String) {
        setState(
            currentState.copy(
                confirmPassword = confirmPassword,
                isConfirmPasswordValid = confirmPassword == currentState.password,
                errorMessage = null
            )
        )
    }

    private fun togglePasswordVisibility() {
        setState(currentState.copy(isPasswordVisible = !currentState.isPasswordVisible))
    }

    private fun toggleConfirmPasswordVisibility() {
        setState(currentState.copy(isConfirmPasswordVisible = !currentState.isConfirmPasswordVisible))
    }

    private fun attemptRegister() {
        val state = currentState

        if (state.email.isBlank() || state.password.isBlank() || state.confirmPassword.isBlank()) {
            setState(state.copy(errorMessage = "Tüm alanları doldurun"))
            return
        }

        if (!state.isEmailValid) {
            setState(state.copy(errorMessage = "Lütfen geçerli bir email adresi girin"))
            return
        }

        if (!state.isPasswordValid) {
            setState(state.copy(errorMessage = "Şifre en az $MIN_PASSWORD_LENGTH karakter olmalı"))
            return
        }

        if (!state.isConfirmPasswordValid) {
            setState(state.copy(errorMessage = "Şifreler eşleşmiyor"))
            return
        }

        viewModelScope.launch {
            setState(currentState.copy(isLoading = true, errorMessage = null))
            when (val result = registerUseCase(state.email.trim(), state.password)) {
                is Result.Success -> {
                    val verificationResult = sendVerificationEmailUseCase()
                    setState(
                        currentState.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    )
                    when (verificationResult) {
                        is Result.Success -> setEffect(RegisterEffect.NavigateToEmailVerification)
                        is Result.Error -> {
                            setState(currentState.copy(errorMessage = verificationResult.message))
                            setEffect(RegisterEffect.ShowError(verificationResult.message))
                            setEffect(RegisterEffect.NavigateToEmailVerification)
                        }
                    }
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

    private companion object {
        private const val MIN_PASSWORD_LENGTH = 6
        private val emailRegex =
            Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    }
}
