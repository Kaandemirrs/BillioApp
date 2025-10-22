package com.billioapp.features.auth.presentation.login

import androidx.lifecycle.viewModelScope
import com.billioapp.core.mvi.BaseViewModel
import com.billioapp.domain.usecase.auth.CheckEmailVerifiedUseCase
import com.billioapp.domain.usecase.auth.GetCurrentUserUseCase
import com.billioapp.domain.usecase.auth.LoginUseCase
import com.billioapp.domain.usecase.auth.ReloadUserUseCase
import com.billioapp.domain.usecase.auth.SendVerificationEmailUseCase
import com.billioapp.domain.usecase.auth.GoogleSignInUseCase
import com.billioapp.domain.usecase.auth.AppleSignInUseCase
import com.billioapp.domain.util.Result
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val reloadUserUseCase: ReloadUserUseCase,
    private val checkEmailVerifiedUseCase: CheckEmailVerifiedUseCase,
    private val sendVerificationEmailUseCase: SendVerificationEmailUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val appleSignInUseCase: AppleSignInUseCase
) : BaseViewModel<LoginState, LoginEvent, LoginEffect>(
    initialState = LoginState()
) {

    init {
        viewModelScope.launch {
            when (val result = getCurrentUserUseCase()) {
                is Result.Success -> {
                    val user = result.data
                    if (user != null) {
                        when (val verified = checkEmailVerifiedUseCase()) {
                            is Result.Success -> {
                                if (verified.data) {
                                    setEffect(LoginEffect.NavigateToMain)
                                } else {
                                    setEffect(LoginEffect.NavigateToEmailVerification)
                                }
                            }
                            is Result.Error -> {
                                // ignore at startup
                            }
                        }
                    }
                }
                is Result.Error -> {
                    // Session kontrolünde oluşan hatalar kullanıcıya gösterilmez.
                }
            }
        }
    }

    override fun handleEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> onEmailChanged(event.email)
            is LoginEvent.PasswordChanged -> onPasswordChanged(event.password)
            LoginEvent.TogglePasswordVisibility -> togglePasswordVisibility()
            LoginEvent.LoginClicked -> attemptLogin()
            LoginEvent.GoogleLoginClicked -> requestGoogleSignIn()
            is LoginEvent.GoogleIdTokenReceived -> signInWithGoogle(event.idToken)
            is LoginEvent.GoogleSignInFailed -> handleSocialFailure(LoginOrigin.GOOGLE, event.message)
            LoginEvent.AppleLoginClicked -> requestAppleSignIn()
            is LoginEvent.AppleIdTokenReceived -> signInWithApple(event.idToken, event.nonce)
            is LoginEvent.AppleSignInFailed -> handleSocialFailure(LoginOrigin.APPLE, event.message)
            LoginEvent.CreateAccountClicked -> setEffect(LoginEffect.NavigateToSignup)
            LoginEvent.ClearError -> setState(currentState.copy(errorMessage = null))
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
                errorMessage = null
            )
        )
    }

    private fun togglePasswordVisibility() {
        setState(currentState.copy(isPasswordVisible = !currentState.isPasswordVisible))
    }

    private fun attemptLogin() {
        if (isBusy()) return

        val state = currentState
        if (state.email.isBlank() || state.password.isBlank()) {
            setState(state.copy(errorMessage = "Email ve şifre boş olamaz"))
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

        viewModelScope.launch {
            updateLoading(LoginOrigin.EMAIL, true, null)
            when (val result = loginUseCase(state.email.trim(), state.password)) {
                is Result.Success -> {
                    handlePostLogin(LoginOrigin.EMAIL)
                }
                is Result.Error -> {
                    updateLoading(LoginOrigin.EMAIL, false, result.message)
                }
            }
        }
    }

    private suspend fun handlePostLogin(origin: LoginOrigin) {
        when (val reloadResult = reloadUserUseCase()) {
            is Result.Error -> {
                updateLoading(origin, false, reloadResult.message)
                setEffect(LoginEffect.ShowError(reloadResult.message))
                return
            }
            else -> Unit
        }
        when (val verifiedResult = checkEmailVerifiedUseCase()) {
            is Result.Success -> {
                updateLoading(origin, false, null)
                if (verifiedResult.data) {
                    setEffect(LoginEffect.NavigateToMain)
                } else {
                    when (val resend = sendVerificationEmailUseCase()) {
                        is Result.Success -> {
                            setEffect(LoginEffect.NavigateToEmailVerification)
                        }
                        is Result.Error -> {
                            updateLoading(origin, false, resend.message)
                            setEffect(LoginEffect.ShowError(resend.message))
                            setEffect(LoginEffect.NavigateToEmailVerification)
                            return
                        }
                    }
                }
            }
            is Result.Error -> {
                updateLoading(origin, false, verifiedResult.message)
                setEffect(LoginEffect.ShowError(verifiedResult.message))
            }
        }
    }

    private fun requestGoogleSignIn() {
        if (isBusy()) return
        setState(currentState.copy(errorMessage = null))
        setEffect(LoginEffect.RequestGoogleSignIn)
    }

    private fun requestAppleSignIn() {
        if (isBusy()) return
        setState(currentState.copy(errorMessage = null))
        setEffect(LoginEffect.RequestAppleSignIn)
    }

    private fun signInWithGoogle(idToken: String) {
        if (idToken.isBlank()) {
            handleSocialFailure(LoginOrigin.GOOGLE, "Google kimlik belirteci alınamadı")
            return
        }
        if (currentState.isGoogleLoading) return
        if (isBusy()) return

        viewModelScope.launch {
            updateLoading(LoginOrigin.GOOGLE, true, null)
            when (val result = googleSignInUseCase(idToken)) {
                is Result.Success -> handlePostLogin(LoginOrigin.GOOGLE)
                is Result.Error -> {
                    updateLoading(LoginOrigin.GOOGLE, false, result.message)
                    setEffect(LoginEffect.ShowError(result.message))
                }
            }
        }
    }

    private fun signInWithApple(idToken: String, nonce: String) {
        if (idToken.isBlank() || nonce.isBlank()) {
            handleSocialFailure(LoginOrigin.APPLE, "Apple kimlik bilgileri alınamadı")
            return
        }
        if (currentState.isAppleLoading) return
        if (isBusy()) return

        viewModelScope.launch {
            updateLoading(LoginOrigin.APPLE, true, null)
            when (val result = appleSignInUseCase(idToken, nonce)) {
                is Result.Success -> handlePostLogin(LoginOrigin.APPLE)
                is Result.Error -> {
                    updateLoading(LoginOrigin.APPLE, false, result.message)
                    setEffect(LoginEffect.ShowError(result.message))
                }
            }
        }
    }

    private fun handleSocialFailure(origin: LoginOrigin, message: String?) {
        val error = message ?: when (origin) {
            LoginOrigin.GOOGLE -> "Google ile giriş başarısız oldu"
            LoginOrigin.APPLE -> "Apple ile giriş başarısız oldu"
            LoginOrigin.EMAIL -> "Giriş başarısız oldu"
        }
        updateLoading(origin, false, error)
        setEffect(LoginEffect.ShowError(error))
    }

    private fun updateLoading(origin: LoginOrigin, isLoading: Boolean, errorMessage: String?) {
        val newState = when (origin) {
            LoginOrigin.EMAIL -> currentState.copy(isLoading = isLoading, errorMessage = errorMessage)
            LoginOrigin.GOOGLE -> currentState.copy(isGoogleLoading = isLoading, errorMessage = errorMessage)
            LoginOrigin.APPLE -> currentState.copy(isAppleLoading = isLoading, errorMessage = errorMessage)
        }
        setState(newState)
    }

    private fun isBusy(): Boolean =
        currentState.isLoading || currentState.isGoogleLoading || currentState.isAppleLoading

    private enum class LoginOrigin { EMAIL, GOOGLE, APPLE }

    private companion object {
        private const val MIN_PASSWORD_LENGTH = 6
        private val emailRegex =
            Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    }
}