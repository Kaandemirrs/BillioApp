package com.billioapp.features.home.presentation

import com.billioapp.core.mvi.UiEffect
import com.billioapp.core.mvi.UiEvent
import com.billioapp.core.mvi.UiState
import com.billioapp.domain.model.home.HomeSummary
import com.billioapp.domain.model.home.MonthlyLimit
import com.billioapp.domain.model.subscriptions.Subscription
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import kotlin.jvm.JvmInline

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
    val isUpdatingLimit: Boolean = false,
    val infoCardState: InfoCardState? = null,
    val smartUpdateState: SmartUpdateState? = null
) : UiState

sealed class HomeEvent : UiEvent {
    object Logout : HomeEvent()
    object LoadHomeData : HomeEvent()
    data class UpdateMonthlyLimit(val amount: Double) : HomeEvent()
    data class OnSaveClicked(val data: com.billioapp.features.home.presentation.components.AddBillData) : HomeEvent()
    data class OnDeleteClicked(val id: String) : HomeEvent()
    data class OnAiPriceSuggestClicked(val serviceName: String) : HomeEvent()
    data class OnCheckSmartPrice(val billId: String) : HomeEvent()
    object OnConfirmSmartUpdate : HomeEvent()
    object OnDismissSmartDialog : HomeEvent()
}

sealed class HomeEffect : UiEffect {
    object NavigateToLogin : HomeEffect()
    data class ShowError(val message: String) : HomeEffect()
    object SubscriptionAddedSuccessfully : HomeEffect()
    object SubscriptionDeletedSuccessfully : HomeEffect()
    data class ShowAiPriceSuggestion(val suggestion: com.billioapp.domain.model.ai.AiPriceSuggestion) : HomeEffect()
    object SmartPriceUpdated : HomeEffect()
}

data class InfoCardState(
    val text: String,
    val backgroundColor: Color,
    val iconRes: DrawableResource,
    val isVisible: Boolean = true
)

data class SmartUpdateState(
    val activeBillId: String? = null,
    val isLoading: Boolean = false,
    val foundPrice: Double? = null,
    val source: String? = null,
    val error: String? = null,
    val isDialogVisible: Boolean = false
)
