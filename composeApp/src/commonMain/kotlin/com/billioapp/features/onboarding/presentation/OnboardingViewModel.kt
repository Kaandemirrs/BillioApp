package com.billioapp.features.onboarding.presentation

import com.billioapp.core.mvi.BaseViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class OnboardingViewModel : BaseViewModel<OnboardingState, OnboardingEvent, OnboardingEffect>(
    initialState = OnboardingState()
) {

    override fun handleEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.NextPage -> {
                if (currentState.canProceed && !currentState.isLastPage) {
                    setState(currentState.copy(currentPage = currentState.currentPage + 1))
                } else if (currentState.isLastPage) {
                    handleEvent(OnboardingEvent.CompleteOnboarding)
                }
            }
            
            is OnboardingEvent.PreviousPage -> {
                if (currentState.currentPage > 0) {
                    setState(currentState.copy(currentPage = currentState.currentPage - 1))
                }
            }
            
            is OnboardingEvent.GoToPage -> {
                if (event.page in 0 until currentState.totalPages) {
                    setState(currentState.copy(currentPage = event.page))
                }
            }
            
            is OnboardingEvent.SelectLanguage -> {
                setState(currentState.copy(selectedLanguage = event.language))
            }
            
            is OnboardingEvent.SelectCurrency -> {
                setState(currentState.copy(selectedCurrency = event.currency))
            }
            
            is OnboardingEvent.CompleteOnboarding -> {
                viewModelScope.launch {
                    setState(currentState.copy(isLoading = true))
                    try {
                        // Here you can save onboarding completion status
                        // saveOnboardingCompleted()
                        setState(currentState.copy(isCompleted = true, isLoading = false))
                        setEffect(OnboardingEffect.NavigateToMain)
                    } catch (e: Exception) {
                        setState(currentState.copy(isLoading = false))
                        setEffect(OnboardingEffect.ShowError("Onboarding tamamlanırken bir hata oluştu"))
                    }
                }
            }
            
            is OnboardingEvent.SkipOnboarding -> {
                setEffect(OnboardingEffect.NavigateToMain)
            }
        }
    }
}