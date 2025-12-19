package com.billioapp.features.premium.presentation

import androidx.lifecycle.viewModelScope
import com.billioapp.core.mvi.BaseViewModel
import com.billioapp.domain.repository.PaymentRepository
import kotlinx.coroutines.launch
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.models.Offering
import com.revenuecat.purchases.kmp.models.Package
import com.revenuecat.purchases.kmp.models.StoreProduct
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import com.revenuecat.purchases.kmp.models.PurchasesError

data class PaywallState(
    val isLoading: Boolean = false,
    val priceText: String = "₺—",
    val error: String? = null
) : com.billioapp.core.mvi.UiState

sealed class PaywallEvent : com.billioapp.core.mvi.UiEvent {
    object LoadOffering : PaywallEvent()
    object PurchaseClicked : PaywallEvent()
    object RestoreClicked : PaywallEvent()
}

sealed class PaywallEffect : com.billioapp.core.mvi.UiEffect {
    object NavigateToHome : PaywallEffect()
    data class ShowError(val message: String) : PaywallEffect()
    object ShowSuccess : PaywallEffect()
}

class PaywallViewModel(
    private val paymentRepository: PaymentRepository
) : BaseViewModel<PaywallState, PaywallEvent, PaywallEffect>(
    initialState = PaywallState()
) {
    override fun handleEvent(event: PaywallEvent) {
        when (event) {
            PaywallEvent.LoadOffering -> loadOffering()
            PaywallEvent.PurchaseClicked -> purchase()
            PaywallEvent.RestoreClicked -> restore()
        }
    }

    private fun loadOffering() {
        if (currentState.isLoading) return
        viewModelScope.launch {
            setState(currentState.copy(isLoading = true, error = null))
            runCatching {
                suspendCancellableCoroutine<com.revenuecat.purchases.kmp.models.Offerings> { cont ->
                    Purchases.sharedInstance.getOfferings(
                        onError = { error: PurchasesError ->
                            cont.resumeWithException(IllegalStateException(error.message))
                        },
                        onSuccess = { offerings ->
                            cont.resume(offerings)
                        }
                    )
                }
            }
                .onSuccess { offerings ->
                    val current: Offering? = offerings.current
                    val pkg: Package? = current?.monthly ?: current?.annual ?: current?.availablePackages?.firstOrNull()
                    val product: StoreProduct? = pkg?.storeProduct
                    val price = product?.price?.formatted ?: "₺—"
                    setState(currentState.copy(isLoading = false, priceText = price))
                }
                .onFailure { t ->
                    setState(currentState.copy(isLoading = false, error = t.message ?: "Teklif yüklenemedi"))
                    setEffect(PaywallEffect.ShowError(currentState.error ?: "Teklif yüklenemedi"))
                }
        }
    }

    private fun purchase() {
        if (currentState.isLoading) return
        viewModelScope.launch {
            setState(currentState.copy(isLoading = true, error = null))
            paymentRepository.purchasePremium()
                .onSuccess {
                    setState(currentState.copy(isLoading = false))
                    setEffect(PaywallEffect.ShowSuccess)
                    setEffect(PaywallEffect.NavigateToHome)
                }
                .onFailure { t ->
                    setState(currentState.copy(isLoading = false, error = t.message))
                    setEffect(PaywallEffect.ShowError(t.message ?: "Satın alma başarısız"))
                }
        }
    }

    private fun restore() {
        if (currentState.isLoading) return
        viewModelScope.launch {
            setState(currentState.copy(isLoading = true, error = null))
            paymentRepository.restorePurchases()
                .onSuccess {
                    setState(currentState.copy(isLoading = false))
                    setEffect(PaywallEffect.ShowSuccess)
                    setEffect(PaywallEffect.NavigateToHome)
                }
                .onFailure { t ->
                    setState(currentState.copy(isLoading = false, error = t.message))
                    setEffect(PaywallEffect.ShowError(t.message ?: "Geri yükleme başarısız"))
                }
        }
    }
}
