package com.billioapp.features.home.presentation

import com.billioapp.core.mvi.UiEffect
import com.billioapp.core.mvi.UiEvent
import com.billioapp.core.mvi.UiState

data class AddSubscriptionState(
    val name: String = "",
    val amount: String = "",
    val isAiLoading: Boolean = false,
    val aiSource: String? = null,
    val aiError: String? = null
) : UiState

sealed class AddSubscriptionEvent : UiEvent {
    data class NameChanged(val value: String) : AddSubscriptionEvent()
    data class AmountChanged(val value: String) : AddSubscriptionEvent()
    object AskAiForPrice : AddSubscriptionEvent()
    object ClearAiError : AddSubscriptionEvent()
}

sealed class AddSubscriptionEffect : UiEffect {
    data class ShowMessage(val message: String) : AddSubscriptionEffect()
}
