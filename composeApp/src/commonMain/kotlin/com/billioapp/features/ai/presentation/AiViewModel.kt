package com.billioapp.features.ai.presentation

import androidx.lifecycle.viewModelScope
import com.billioapp.core.mvi.BaseViewModel
import com.billioapp.domain.usecase.ai.GetAiAnalysisUseCase
import com.billioapp.domain.usecase.subscriptions.GetSubscriptionsUseCase
import com.billioapp.domain.util.Result
import kotlinx.coroutines.launch

class AiViewModel(
    private val getAiAnalysisUseCase: GetAiAnalysisUseCase,
    private val getSubscriptionsUseCase: GetSubscriptionsUseCase
) : BaseViewModel<AiState, AiViewEvent, AiViewEffect>(
    initialState = AiState()
) {

    override fun handleEvent(event: AiViewEvent) {
        when (event) {
            AiViewEvent.OnAnalyzeClicked -> analyzeAllBills()
        }
    }

    private fun analyzeAllBills() {
        if (currentState.isLoading) return
        viewModelScope.launch {
            setState(currentState.copy(isLoading = true, error = null))
            when (val subsResult = getSubscriptionsUseCase()) {
                is Result.Success -> {
                    val subscriptions = subsResult.data.subscriptions
                    when (val aiResult = getAiAnalysisUseCase(subscriptions)) {
                        is Result.Success -> {
                            setState(currentState.copy(isLoading = false, analysis = aiResult.data))
                        }
                        is Result.Error -> {
                            val msg = aiResult.message.ifBlank { "Analiz başarısız" }
                            setState(currentState.copy(isLoading = false, error = msg))
                            setEffect(AiViewEffect.ShowError(msg))
                        }
                    }
                }
                is Result.Error -> {
                    val msg = subsResult.message.ifBlank { "Abonelikler alınamadı" }
                    setState(currentState.copy(isLoading = false, error = msg))
                    setEffect(AiViewEffect.ShowError(msg))
                }
            }
        }
    }
}