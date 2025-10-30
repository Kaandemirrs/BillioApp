package com.billioapp.features.home.presentation

import androidx.lifecycle.viewModelScope
import com.billioapp.core.mvi.BaseViewModel
import com.billioapp.domain.usecase.auth.LogoutUseCase
import com.billioapp.domain.usecase.home.GetHomeSummaryUseCase
import com.billioapp.domain.usecase.home.GetMonthlyLimitUseCase
import com.billioapp.domain.usecase.home.UpdateMonthlyLimitUseCase
import com.billioapp.domain.usecase.subscriptions.GetSubscriptionsUseCase
import com.billioapp.domain.usecase.subscriptions.AddSubscriptionUseCase
import com.billioapp.domain.util.Result
import com.billioapp.domain.model.home.MonthlyLimit
import com.billioapp.domain.model.subscriptions.AddSubscriptionRequest
import billioapp.composeapp.generated.resources.Res
import billioapp.composeapp.generated.resources.ic_logo
import io.github.aakira.napier.Napier

import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import com.billioapp.features.home.presentation.components.AddBillData
import com.billioapp.features.home.presentation.components.BillingCycle


class HomeViewModel(
    private val getHomeSummaryUseCase: GetHomeSummaryUseCase,
    private val getMonthlyLimitUseCase: GetMonthlyLimitUseCase,
    private val updateMonthlyLimitUseCase: UpdateMonthlyLimitUseCase,
    private val getSubscriptionsUseCase: GetSubscriptionsUseCase,
    private val addSubscriptionUseCase: AddSubscriptionUseCase,
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>(
    initialState = HomeState()
) {

    override fun handleEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.Logout -> logout()
            HomeEvent.LoadHomeData -> loadHomeData()
            is HomeEvent.UpdateMonthlyLimit -> updateMonthlyLimit(event.amount)
            is HomeEvent.OnSaveClicked -> onSaveClicked(event.data)
        }
    }

    private fun loadHomeData() {
        if (currentState.isLoading) return
        // Subscriptions çağrısını paralel başlat
        viewModelScope.launch {
            val result = getSubscriptionsUseCase(limit = 20, page = 1, isActive = true)
            when (result) {
                is Result.Success -> {
                    val subscriptions = result.data.subscriptions
                    val billsList = subscriptions.map { s ->
                        BillItemModel(
                            name = s.name,
                            amountText = formatAmount(s.amount, s.currency),
                            leadingColorHex = 0xFFFFEB3B,
                            primaryColorHex = 0xFF69F0AE,
                            trailingColorHex = 0xFFFF5252,
                            iconRes = Res.drawable.ic_logo
                        )
                    }
                    Napier.d(tag = "HomeViewModel", message = "UseCase'den ${subscriptions.size} adet işlenmiş fatura geldi")
                    setState(currentState.copy(bills = billsList, subscriptions = subscriptions))
                }
                is Result.Error -> {
                    val msg = result.message.ifBlank { "Abonelikler yüklenemedi" }
                    println("HomeViewModel ERROR: Subscriptions loading failed: $msg")
                    println("HomeViewModel ERROR: Error details: ${result.throwable?.message}")
                    Napier.e(tag = "HomeViewModel", message = "Abonelikler yüklenemedi: $msg")
                    setState(currentState.copy(isLoading = false))
                }
            }
        }
        viewModelScope.launch {
            println("HomeViewModel: Starting to load home data...")
            setState(currentState.copy(isLoading = true, error = null))
            when (val summaryResult = getHomeSummaryUseCase()) {
                is Result.Success -> {
                    println("HomeViewModel: Home summary loaded successfully")
                    val summary = summaryResult.data
                    val tracker = TrackerModel(
                        totalAmount = summary.totalAmount,
                        currency = summary.currency,
                        categories = summary.categories.map { cat ->
                            TrackerCategory(
                                name = cat.name,
                                amount = cat.amount,
                                colorHex = cat.colorHex ?: 0xFF607D8B
                            )
                        }
                    )

                    var monthlyLimitValue: MonthlyLimit? = summary.monthlyLimit?.let { amount ->
                        MonthlyLimit(amount = amount, currency = summary.currency)
                    }
                    val currencyValue = summary.currency
                    if (monthlyLimitValue == null) {
                        when (val limitResult = getMonthlyLimitUseCase()) {
                            is Result.Success -> monthlyLimitValue = limitResult.data
                            is Result.Error -> { /* ignore, keep null */ }
                        }
                    }

                    setState(
                        currentState.copy(
                            isLoading = false,
                            error = null,
                            homeSummary = summary,
                            trackerModel = tracker,
                            currency = currencyValue,
                            monthlyLimit = monthlyLimitValue
                        )
                    )
                }
                is Result.Error -> {
                    println("HomeViewModel ERROR: Home data loading failed: ${summaryResult.message}")
                    println("HomeViewModel ERROR: Error details: ${summaryResult.throwable?.message}")
                    val errorMessage = summaryResult.message.ifBlank { "Veriler yüklenirken bir hata oluştu" }
                    setState(currentState.copy(isLoading = false, error = errorMessage))
                    setEffect(HomeEffect.ShowError(errorMessage))
                }
            }
        }
    }

    private fun onSaveClicked(data: AddBillData) {
        viewModelScope.launch {
            setState(currentState.copy(isLoading = true))
            val cycleStr = data.cycle.name.lowercase()
            val categoryFixed = data.category.lowercase()
            val currencyFixed = if (currentState.currency.equals("TL", ignoreCase = true)) "TRY" else currentState.currency
            val request = AddSubscriptionRequest(
                name = data.name,
                category = categoryFixed,
                amount = data.amount,
                currency = currencyFixed,
                billingCycle = cycleStr,
                startDate = "2025-01-01",
                billingDay = data.paymentDay
            )

            when (val result = addSubscriptionUseCase(request)) {
                is Result.Success -> {
                    val sub = result.data
                    val updatedSubscriptions = currentState.subscriptions + sub
                    // Çift eklemeyi önlemek için sadece subscriptions listesi güncelleniyor.
                    // Bills listesi burada değiştirilmez; gerekli ise ayrı bir türetme ile güncellenmelidir.
                    setState(currentState.copy(isLoading = false, subscriptions = updatedSubscriptions))
                    setEffect(HomeEffect.SubscriptionAddedSuccessfully)
                }
                is Result.Error -> {
                    val errorMessage = result.message.ifBlank { "Abonelik eklenemedi" }
                    // Detaylı hata logları
                    Napier.e(tag = "HomeViewModel", message = "Abonelik EKLENEMEDİ: $errorMessage", throwable = result.throwable)
                    println("HomeViewModel ERROR: Abonelik eklenemedi: ${result.throwable?.message}")
                    setState(currentState.copy(isLoading = false, error = errorMessage))
                    setEffect(HomeEffect.ShowError(errorMessage))
                }
            }
        }
    }

    private fun formatAmount(amount: Double, currency: String): String {
        return "${amount.roundToInt()} $currency"
    }

    private fun updateMonthlyLimit(amount: Double) {
        if (currentState.isUpdatingLimit) return
        viewModelScope.launch {
            setState(currentState.copy(isUpdatingLimit = true))
            val currency = currentState.currency
            when (val result = updateMonthlyLimitUseCase(amount, currency)) {
                is Result.Success -> {
                    setState(currentState.copy(isUpdatingLimit = false, monthlyLimit = result.data, error = null))
                }
                is Result.Error -> {
                    setState(currentState.copy(isUpdatingLimit = false, error = result.message))
                    val message = result.message.ifBlank { "Limit güncellenirken bir hata oluştu" }
                    setEffect(HomeEffect.ShowError(message))
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            when (val result = logoutUseCase()) {
                is Result.Success -> {
                    setEffect(HomeEffect.NavigateToLogin)
                }
                is Result.Error -> {
                    setEffect(HomeEffect.ShowError(result.message.ifBlank { "Çıkış yapılırken bir hata oluştu" }))
                }
            }
        }
    }
}
