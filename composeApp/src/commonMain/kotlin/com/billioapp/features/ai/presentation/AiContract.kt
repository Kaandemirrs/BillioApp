package com.billioapp.features.ai.presentation

import com.billioapp.core.mvi.UiEffect
import com.billioapp.core.mvi.UiEvent
import com.billioapp.core.mvi.UiState
import com.billioapp.domain.model.ai.AiAnalysisReport

data class AiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val analysis: AiAnalysisReport? = null
) : UiState

sealed class AiViewEvent : UiEvent {
    object OnAnalyzeClicked : AiViewEvent()
}

sealed class AiViewEffect : UiEffect {
    data class ShowError(val message: String) : AiViewEffect()
}