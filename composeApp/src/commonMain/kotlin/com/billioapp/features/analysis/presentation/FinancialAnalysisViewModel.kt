package com.billioapp.features.analysis.presentation

import androidx.lifecycle.viewModelScope
import com.billioapp.core.mvi.BaseViewModel
import com.billioapp.core.mvi.UiEffect
import com.billioapp.core.mvi.UiEvent
import com.billioapp.core.mvi.UiState
import com.billioapp.domain.model.ai.FinancialAnalysis
import com.billioapp.domain.usecase.ai.GetFinancialAnalysisUseCase
import com.billioapp.domain.util.Result
import kotlinx.coroutines.launch

data class FinancialAnalysisState(
    val reportText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val analysisResult: FinancialAnalysis? = null
) : UiState

sealed class FinancialAnalysisEvent : UiEvent {
    object Refresh : FinancialAnalysisEvent()
}

sealed class FinancialAnalysisEffect : UiEffect {
    data class ShowError(val message: String) : FinancialAnalysisEffect()
}

class FinancialAnalysisViewModel(
    private val getFinancialAnalysisUseCase: GetFinancialAnalysisUseCase
) : BaseViewModel<FinancialAnalysisState, FinancialAnalysisEvent, FinancialAnalysisEffect>(
    initialState = FinancialAnalysisState()
) {
    init {
        loadAnalysis()
    }

    override fun handleEvent(event: FinancialAnalysisEvent) {
        when (event) {
            FinancialAnalysisEvent.Refresh -> loadAnalysis()
        }
    }

    fun loadAnalysis() {
        if (currentState.isLoading) return
        setState(currentState.copy(isLoading = true, errorMessage = null))
        viewModelScope.launch {
            when (val result = getFinancialAnalysisUseCase()) {
                is Result.Success -> {
                    val analysis = result.data
                    setState(
                        currentState.copy(
                            isLoading = false,
                            reportText = analysis.reportText,
                            analysisResult = analysis,
                            errorMessage = null
                        )
                    )
                }
                is Result.Error -> {
                    val msg = result.message.ifBlank { "Analiz alınamadı" }
                    setState(currentState.copy(isLoading = false, errorMessage = msg))
                    setEffect(FinancialAnalysisEffect.ShowError(msg))
                }
            }
        }
    }
}
