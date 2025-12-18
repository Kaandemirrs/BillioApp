package com.billioapp.features.home.presentation

import androidx.lifecycle.viewModelScope
import com.billioapp.core.mvi.BaseViewModel
import com.billioapp.domain.usecase.ai.GetSmartPriceUseCase
import com.billioapp.domain.util.Result
import kotlinx.coroutines.launch

class AddSubscriptionViewModel(
    private val getSmartPriceUseCase: GetSmartPriceUseCase
) : BaseViewModel<AddSubscriptionState, AddSubscriptionEvent, AddSubscriptionEffect>(
    initialState = AddSubscriptionState()
) {
    override fun handleEvent(event: AddSubscriptionEvent) {
        when (event) {
            is AddSubscriptionEvent.NameChanged -> setState(currentState.copy(name = event.value, aiError = null))
            is AddSubscriptionEvent.AmountChanged -> setState(currentState.copy(amount = event.value, aiError = null))
            AddSubscriptionEvent.AskAiForPrice -> askAiForPrice()
            AddSubscriptionEvent.ClearAiError -> setState(currentState.copy(aiError = null))
        }
    }

    private fun askAiForPrice() {
        val name = currentState.name.trim()
        if (name.isBlank()) {
            setState(currentState.copy(aiError = "Önce isim girin"))
            return
        }
        if (currentState.isAiLoading) return

        viewModelScope.launch {
            setState(currentState.copy(isAiLoading = true, aiError = null))
            when (val result = getSmartPriceUseCase(name)) {
                is Result.Success -> {
                    val dto = result.data
                    val priceStr = dto.suggestedPrice?.let { formatAmountString(it) } ?: ""
                    val sourceStr = dto.source?.let { "Kaynak: $it" }
                    setState(
                        currentState.copy(
                            amount = priceStr,
                            aiSource = sourceStr,
                            aiError = null,
                            isAiLoading = false
                        )
                    )
                }
                is Result.Error -> {
                    setState(
                        currentState.copy(
                            aiError = result.message.ifBlank { "Fiyat önerisi alınamadı" },
                            isAiLoading = false
                        )
                    )
                }
            }
        }
    }

    private fun formatAmountString(value: Double): String {
        return if (value % 1.0 == 0.0) value.toInt().toString()
        else (kotlin.math.round(value * 100.0) / 100.0).toString()
    }
}
