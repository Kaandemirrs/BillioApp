package com.billioapp.features.auth.presentation.emailverification

import androidx.lifecycle.viewModelScope
import com.billioapp.core.mvi.BaseViewModel
import com.billioapp.domain.usecase.auth.CheckEmailVerifiedUseCase
import com.billioapp.domain.usecase.auth.GetCurrentUserUseCase
import com.billioapp.domain.usecase.auth.LogoutUseCase
import com.billioapp.domain.usecase.auth.ReloadUserUseCase
import com.billioapp.domain.usecase.auth.SendVerificationEmailUseCase
import com.billioapp.domain.util.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EmailVerificationViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val checkEmailVerifiedUseCase: CheckEmailVerifiedUseCase,
    private val reloadUserUseCase: ReloadUserUseCase,
    private val sendVerificationEmailUseCase: SendVerificationEmailUseCase,
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel<EmailVerificationState, EmailVerificationEvent, EmailVerificationEffect>(
    initialState = EmailVerificationState()
) {

    init {
        loadUserEmail()
    }

    private fun loadUserEmail() {
        viewModelScope.launch {
            when (val result = getCurrentUserUseCase()) {
                is Result.Success -> {
                    val email = result.data?.email
                    if (email.isNullOrBlank()) {
                        setEffect(EmailVerificationEffect.NavigateToLogin)
                    } else {
                        setState(currentState.copy(userEmail = email))
                    }
                }
                is Result.Error -> {
                    setEffect(EmailVerificationEffect.NavigateToLogin)
                }
            }
        }
    }

    override fun handleEvent(event: EmailVerificationEvent) {
        when (event) {
            EmailVerificationEvent.CheckVerification -> checkVerification()
            EmailVerificationEvent.ResendEmail -> resendEmail()
            EmailVerificationEvent.Logout -> logout()
        }
    }

    private fun checkVerification() {
        if (currentState.isCheckingVerification) return

        viewModelScope.launch {
            setState(
                currentState.copy(
                    isCheckingVerification = true,
                    errorMessage = null,
                    resendSuccess = false
                )
            )
            when (val reloadResult = reloadUserUseCase()) {
                is Result.Error -> {
                    setState(
                        currentState.copy(
                            isCheckingVerification = false,
                            errorMessage = reloadResult.message
                        )
                    )
                    return@launch
                }
                else -> Unit
            }
            delay(500)

            when (val result = checkEmailVerifiedUseCase()) {
                is Result.Success -> {
                    if (result.data) {
                        setState(currentState.copy(isCheckingVerification = false))
                        setEffect(EmailVerificationEffect.NavigateToHome)
                    } else {
                        setState(
                            currentState.copy(
                                isCheckingVerification = false,
                                errorMessage = "Email adresiniz henüz doğrulanmadı. Lütfen gelen kutunuzu kontrol edin."
                            )
                        )
                    }
                }
                is Result.Error -> {
                    setState(
                        currentState.copy(
                            isCheckingVerification = false,
                            errorMessage = result.message
                        )
                    )
                }
            }
        }
    }

    private fun resendEmail() {
        if (currentState.isResendingEmail) return

        viewModelScope.launch {
            setState(
                currentState.copy(
                    isResendingEmail = true,
                    errorMessage = null,
                    resendSuccess = false
                )
            )

            when (val result = sendVerificationEmailUseCase()) {
                is Result.Success -> {
                    setState(
                        currentState.copy(
                            isResendingEmail = false,
                            resendSuccess = true
                        )
                    )
                    setEffect(
                        EmailVerificationEffect.ShowSnackbar(
                            "Doğrulama email'i gönderildi. Lütfen gelen kutunuzu kontrol edin."
                        )
                    )
                }
                is Result.Error -> {
                    setState(
                        currentState.copy(
                            isResendingEmail = false,
                            errorMessage = result.message
                        )
                    )
                    setEffect(EmailVerificationEffect.ShowSnackbar(result.message))
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            when (val result = logoutUseCase()) {
                is Result.Success -> setEffect(EmailVerificationEffect.NavigateToLogin)
                is Result.Error -> setEffect(EmailVerificationEffect.ShowSnackbar(result.message))
            }
        }
    }
}
