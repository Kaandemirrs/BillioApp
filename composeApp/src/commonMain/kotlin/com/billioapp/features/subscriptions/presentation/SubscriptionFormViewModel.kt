package com.billioapp.features.subscriptions.presentation

import androidx.lifecycle.viewModelScope
import com.billioapp.core.mvi.BaseViewModel
import com.billioapp.core.mvi.UiEffect
import com.billioapp.core.mvi.UiEvent
import com.billioapp.core.mvi.UiState
import com.billioapp.domain.model.services.Service
import com.billioapp.domain.model.services.ServicePlan
import com.billioapp.domain.usecase.services.GetServicePlansUseCase
import com.billioapp.domain.usecase.services.SearchServicesUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class SubscriptionFormState(
    val searchText: String = "",
    val searchResults: List<Service> = emptyList(),
    val selectedService: Service? = null,
    val availablePlans: List<ServicePlan> = emptyList()
) : UiState

sealed class SubscriptionFormEvent : UiEvent {
    data class OnSearchTextChanged(val text: String) : SubscriptionFormEvent()
    data class OnServiceSelected(val service: Service) : SubscriptionFormEvent()
}

sealed class SubscriptionFormEffect : UiEffect {
    data class ShowError(val message: String) : SubscriptionFormEffect()
}

class SubscriptionFormViewModel(
    private val searchServicesUseCase: SearchServicesUseCase,
    private val getServicePlansUseCase: GetServicePlansUseCase
) : BaseViewModel<SubscriptionFormState, SubscriptionFormEvent, SubscriptionFormEffect>(
    initialState = SubscriptionFormState()
) {

    private val searchQueryFlow = MutableStateFlow("")

    init {
        observeSearchText()
    }

    override fun handleEvent(event: SubscriptionFormEvent) {
        when (event) {
            is SubscriptionFormEvent.OnSearchTextChanged -> onSearchTextChanged(event.text)
            is SubscriptionFormEvent.OnServiceSelected -> onServiceSelected(event.service)
        }
    }

    private fun onSearchTextChanged(text: String) {
        if (text == currentState.searchText) return
        setState(currentState.copy(searchText = text))
        searchQueryFlow.value = text
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchText() {
        viewModelScope.launch {
            searchQueryFlow
                .distinctUntilChanged()
                .debounce(300)
                .filter { it.isNotBlank() }
                .onEach { query ->
                    searchServices(query)
                }
                .collect { }
        }
    }

    private suspend fun searchServices(query: String) {
        searchServicesUseCase(query)
            .onSuccess { services ->
                setState(currentState.copy(searchResults = services))
            }
            .onFailure { throwable ->
                setEffect(SubscriptionFormEffect.ShowError(throwable.message ?: "Arama başarısız"))
            }
    }

    private fun onServiceSelected(service: Service) {
        setState(currentState.copy(selectedService = service, availablePlans = emptyList()))
        viewModelScope.launch {
            getServicePlansUseCase(serviceId = service.id)
                .onSuccess { plans ->
                    setState(currentState.copy(availablePlans = plans))
                }
                .onFailure { throwable ->
                    setEffect(SubscriptionFormEffect.ShowError(throwable.message ?: "Planlar alınamadı"))
                }
        }
    }
}