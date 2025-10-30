package com.billioapp.features.home.presentation

import com.billioapp.core.mvi.UiEffect
import com.billioapp.core.mvi.UiEvent
import com.billioapp.core.mvi.UiState
import com.billioapp.domain.model.home.HomeSummary
import com.billioapp.domain.model.home.MonthlyLimit
import com.billioapp.domain.model.subscriptions.Subscription

data class HomeState(
    val isLoggingOut: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val homeSummary: HomeSummary? = null,
    val monthlyLimit: MonthlyLimit? = null,
    val trackerModel: TrackerModel? = null,
    val bills: List<BillItemModel> = emptyList(),
    val subscriptions: List<Subscription> = emptyList(),
    val currency: String = "TL",
    val isUpdatingLimit: Boolean = false
) : UiState

sealed class HomeEvent : UiEvent {
    object Logout : HomeEvent()
    object LoadHomeData : HomeEvent()
    data class UpdateMonthlyLimit(val amount: Double) : HomeEvent()
    data class OnSaveClicked(val data: com.billioapp.features.home.presentation.components.AddBillData) : HomeEvent()
}

sealed class HomeEffect : UiEffect {
    object NavigateToLogin : HomeEffect()
    data class ShowError(val message: String) : HomeEffect()
    object SubscriptionAddedSuccessfully : HomeEffect()
}
