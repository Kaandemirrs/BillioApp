package com.billioapp.features.home.presentation

import androidx.lifecycle.viewModelScope
import com.billioapp.core.mvi.BaseViewModel
import com.billioapp.domain.usecase.auth.LogoutUseCase
import com.billioapp.domain.usecase.home.GetHomeSummaryUseCase
import com.billioapp.domain.usecase.home.GetMonthlyLimitUseCase
import com.billioapp.domain.usecase.home.UpdateMonthlyLimitUseCase
import com.billioapp.domain.usecase.subscriptions.GetSubscriptionsUseCase
import com.billioapp.domain.usecase.subscriptions.AddSubscriptionUseCase
import com.billioapp.domain.usecase.subscriptions.DeleteSubscriptionUseCase
import com.billioapp.domain.usecase.ai.GetAiPriceSuggestionUseCase
import com.billioapp.domain.util.Result
import com.billioapp.domain.model.home.MonthlyLimit
import com.billioapp.domain.model.home.CategorySpend
import com.billioapp.domain.model.home.HomeSummary
import com.billioapp.domain.model.subscriptions.AddSubscriptionRequest
import billioapp.composeapp.generated.resources.Res
import billioapp.composeapp.generated.resources.ic_logo
import billioapp.composeapp.generated.resources.onboarding_illustration
import billioapp.composeapp.generated.resources.ic_notification
import billioapp.composeapp.generated.resources.ampl
import io.github.aakira.napier.Napier

import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import com.billioapp.features.home.presentation.components.AddBillData
import com.billioapp.features.home.presentation.components.BillingCycle
import androidx.compose.ui.graphics.Color


class HomeViewModel(
    private val getHomeSummaryUseCase: GetHomeSummaryUseCase,
    private val getMonthlyLimitUseCase: GetMonthlyLimitUseCase,
    private val updateMonthlyLimitUseCase: UpdateMonthlyLimitUseCase,
    private val getSubscriptionsUseCase: GetSubscriptionsUseCase,
    private val addSubscriptionUseCase: AddSubscriptionUseCase,
    private val deleteSubscriptionUseCase: DeleteSubscriptionUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getAiPriceSuggestionUseCase: GetAiPriceSuggestionUseCase
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>(
    initialState = HomeState()
) {

    override fun handleEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.Logout -> logout()
            HomeEvent.LoadHomeData -> loadHomeData()
            is HomeEvent.UpdateMonthlyLimit -> updateMonthlyLimit(event.amount)
            is HomeEvent.OnSaveClicked -> onSaveClicked(event.data)
            is HomeEvent.OnDeleteClicked -> onDeleteClicked(event.id)
            is HomeEvent.OnAiPriceSuggestClicked -> onAiPriceSuggestClicked(event.serviceName)
        }
    }

    private fun loadHomeData() {
        if (currentState.isLoading) return
        viewModelScope.launch {
            println("HomeViewModel: Starting to load subscriptions...")
            setState(currentState.copy(isLoading = true, error = null))

            when (val result = getSubscriptionsUseCase(limit = 20, page = 1, isActive = true)) {
                is Result.Success -> {
                    val subscriptions = result.data.subscriptions

                    // Build bill list UI models
                    val billsList = subscriptions.map { s ->
                        val colorStr = s.predefinedBills?.primaryColor ?: s.color
                        val primaryHex = parseColorHexLong(colorStr) ?: 0xFF607D8BL
                        BillItemModel(
                            id = s.id,
                            name = s.name,
                            amountText = formatAmount(s.amount, s.currency),
                            leadingColorHex = 0xFFFFEB3B,
                            primaryColorHex = primaryHex,
                            trailingColorHex = 0xFFFF5252,
                            iconRes = Res.drawable.ic_logo
                        )
                    }

                    Napier.d(tag = "HomeViewModel", message = "UseCase'den ${subscriptions.size} adet işlenmiş fatura geldi")

                    // Tracker slices: each subscription becomes a slice
                    val trackerCategories = subscriptions.map { s ->
                        val colorStr = s.predefinedBills?.primaryColor ?: s.color
                        val colorHexLong = parseColorHexLong(colorStr) ?: 0xFF607D8BL
                        TrackerCategory(
                            name = s.name,
                            amount = s.amount,
                            colorHex = colorHexLong
                        )
                    }

                    val totalAmount = subscriptions.sumOf { it.amount }
                    val currency = result.data.currency

                    val trackerModel = TrackerModel(
                        totalAmount = totalAmount,
                        currency = currency,
                        categories = trackerCategories
                    )

                    // Optionally fetch monthly limit if missing
                    var monthlyLimitValue: MonthlyLimit? = currentState.monthlyLimit
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
                            bills = billsList,
                            subscriptions = subscriptions,
                            trackerModel = trackerModel,
                            currency = currency,
                            monthlyLimit = monthlyLimitValue
                        )
                    )

                    // Update dynamic info card after data loads
                    updateInfoCardState()
                }
                is Result.Error -> {
                    val msg = result.message.ifBlank { "Abonelikler yüklenemedi" }
                    println("HomeViewModel ERROR: Subscriptions loading failed: $msg")
                    println("HomeViewModel ERROR: Error details: ${result.throwable?.message}")
                    Napier.e(tag = "HomeViewModel", message = "Abonelikler yüklenemedi: $msg")
                    setState(currentState.copy(isLoading = false, error = msg))
                    setEffect(HomeEffect.ShowError(msg))
                }
            }
        }
    }

    private fun onAiPriceSuggestClicked(serviceName: String) {
        viewModelScope.launch {
            when (val result = getAiPriceSuggestionUseCase(serviceName)) {
                is Result.Success -> {
                    setEffect(HomeEffect.ShowAiPriceSuggestion(result.data))
                }
                is Result.Error -> {
                    val msg = result.message.ifBlank { "Fiyat önerisi alınamadı" }
                    Napier.e(tag = "HomeViewModel", message = "AI Fiyat Önerisi BAŞARISIZ: ${result.message}", throwable = result.throwable)
                    println("HomeViewModel ERROR (AI Fiyat): ${result.throwable?.message}")
                    setEffect(HomeEffect.ShowError(msg))
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
                billingDay = data.paymentDay,
                color = data.color
            )

            when (val result = addSubscriptionUseCase(request)) {
                is Result.Success -> {
                    val sub = result.data
                    val updatedSubscriptions = currentState.subscriptions + sub
                    val newColorStr = sub.predefinedBills?.primaryColor ?: sub.color
                    val newPrimaryHex = parseColorHexLong(newColorStr) ?: 0xFF607D8BL
                    val newBill = BillItemModel(
                        id = sub.id,
                        name = sub.name,
                        amountText = formatAmount(sub.amount, sub.currency),
                        leadingColorHex = 0xFFFFEB3B,
                        primaryColorHex = newPrimaryHex,
                        trailingColorHex = 0xFFFF5252,
                        iconRes = Res.drawable.ic_logo
                    )
                    val updatedBills = currentState.bills + newBill

                    // Recalculate tracker with the new subscription list
                    val trackerCategories = updatedSubscriptions.map { s ->
                        val colorStr = s.predefinedBills?.primaryColor ?: s.color
                        val colorHexLong = parseColorHexLong(colorStr) ?: 0xFF607D8BL
                        TrackerCategory(
                            name = s.name,
                            amount = s.amount,
                            colorHex = colorHexLong
                        )
                    }
                    val totalAmount = updatedSubscriptions.sumOf { it.amount }
                    val currency = currentState.currency
                    val trackerModel = TrackerModel(
                        totalAmount = totalAmount,
                        currency = currency,
                        categories = trackerCategories
                    )

                    setState(
                        currentState.copy(
                            isLoading = false,
                            subscriptions = updatedSubscriptions,
                            bills = updatedBills,
                            trackerModel = trackerModel
                        )
                    )
                    // Update dynamic info card after addition
                    updateInfoCardState()
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

    private fun onDeleteClicked(id: String) {
        viewModelScope.launch {
            // Silme sırasında global loading’e geçmeyelim; snackbar ile bildireceğiz
            when (val result = deleteSubscriptionUseCase(id)) {
                is Result.Success -> {
                    val updatedSubscriptions = currentState.subscriptions.filter { it.id != id }
                    val billsList = updatedSubscriptions.map { s ->
                        val colorStr = s.predefinedBills?.primaryColor ?: s.color
                        val primaryHex = parseColorHexLong(colorStr) ?: 0xFF607D8BL
                        BillItemModel(
                            id = s.id,
                            name = s.name,
                            amountText = formatAmount(s.amount, s.currency),
                            leadingColorHex = 0xFFFFEB3B,
                            primaryColorHex = primaryHex,
                            trailingColorHex = 0xFFFF5252,
                            iconRes = Res.drawable.ic_logo
                        )
                    }

                    // Recalculate tracker for updated list
                    val trackerCategories = updatedSubscriptions.map { s ->
                        val colorStr = s.predefinedBills?.primaryColor ?: s.color
                        val colorHexLong = parseColorHexLong(colorStr) ?: 0xFF607D8BL
                        TrackerCategory(
                            name = s.name,
                            amount = s.amount,
                            colorHex = colorHexLong
                        )
                    }
                    val totalAmount = updatedSubscriptions.sumOf { it.amount }
                    val currency = currentState.currency
                    val trackerModel = TrackerModel(
                        totalAmount = totalAmount,
                        currency = currency,
                        categories = trackerCategories
                    )
                    setState(
                        currentState.copy(
                            subscriptions = updatedSubscriptions,
                            bills = billsList,
                            trackerModel = trackerModel,
                            error = null
                        )
                    )
                    // Update dynamic info card after deletion
                    updateInfoCardState()
                    setEffect(HomeEffect.SubscriptionDeletedSuccessfully)
                }
                is Result.Error -> {
                    val msg = result.message.ifBlank { "Abonelik silinemedi" }
                    Napier.e(tag = "HomeViewModel", message = "Abonelik SİLİNEMEDİ: $msg", throwable = result.throwable)
                    setEffect(HomeEffect.ShowError(msg))
                }
            }
        }
    }

    private fun formatAmount(amount: Double, currency: String): String {
        return "${amount.roundToInt()} $currency"
    }

    // Hex string'i ("#RRGGBB" veya "#AARRGGBB") Long ARGB değere çevirir; geçersizse null döner
    private fun parseColorHexLong(hex: String?): Long? {
        try {
            if (hex.isNullOrBlank()) return null
            val s = hex.trim()
            if (!s.startsWith("#")) return null
            val h = s.drop(1)
            val value = h.toLong(16)
            return when (h.length) {
                6 -> 0xFF000000L or value // #RRGGBB -> ARGB
                8 -> value               // #AARRGGBB
                else -> null
            }
        } catch (e: Exception) {
            return null
        }
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

    // Dynamically updates info card based on subscriptions and tracker totals
    private fun updateInfoCardState() {
        val subscriptions = currentState.subscriptions
        val totalAmount = currentState.trackerModel?.totalAmount ?: subscriptions.sumOf { it.amount }
        val newInfoCardState = when {
            // Empty state
            subscriptions.isEmpty() -> InfoCardState(
                text = "Billio'ya hoşgeldin! İlk faturanı ekle ve takibe başla.",
                backgroundColor = Color(0xFFF0E68C),
                iconRes = Res.drawable.onboarding_illustration,
                isVisible = true
            )
            // High spend (AI teaser)
            totalAmount > 2000.0 -> InfoCardState(
                text = "Harcamaların 2000 TL'yi geçti! AI ile faturalarını analiz et ve tasarruf et.",
                backgroundColor = Color(0xFFDDA0DD),
                iconRes = Res.drawable.ampl,
                isVisible = true
            )
            // Normal spend
            totalAmount > 1000.0 -> InfoCardState(
                text = "Her şey kontrol altında. Normal kullanıcılar ortalama 1000-2000 TL arası fatura ödüyor.",
                backgroundColor = Color(0xFFADD8E6),
                iconRes = Res.drawable.ic_notification,
                isVisible = true
            )
            // Low spend or other
            else -> InfoCardState(
                text = "Harika gidiyorsun! Faturalarını takip etmek tasarrufun ilk adımıdır.",
                backgroundColor = Color(0xFF90EE90),
                iconRes = Res.drawable.ic_logo,
                isVisible = true
            )
        }
        setState(currentState.copy(infoCardState = newInfoCardState))
    }
}
