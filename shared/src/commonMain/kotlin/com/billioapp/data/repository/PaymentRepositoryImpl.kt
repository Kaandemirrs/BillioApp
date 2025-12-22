package com.billioapp.data.repository

import com.billioapp.data.remote.api.PaymentApi
import com.billioapp.data.remote.dto.payment.SyncPaymentRequestDto
import com.billioapp.domain.repository.PaymentRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.models.Package
import com.revenuecat.purchases.kmp.models.StoreProduct
import com.revenuecat.purchases.kmp.models.PurchasesError
import com.revenuecat.purchases.kmp.models.StoreTransaction
import com.revenuecat.purchases.kmp.models.CustomerInfo
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class PaymentRepositoryImpl(
    private val api: PaymentApi
) : PaymentRepository {

    override suspend fun purchasePremium(): Result<Unit> = runCatching {
        withContext(Dispatchers.Default) {
            val offerings = suspendCancellableCoroutine<com.revenuecat.purchases.kmp.models.Offerings> { cont ->
                Purchases.sharedInstance.getOfferings(
                    onError = { error: PurchasesError ->
                        cont.resumeWithException(IllegalStateException(error.message))
                    },
                    onSuccess = { off ->
                        cont.resume(off)
                    }
                )
            }
            val current = offerings.current ?: throw IllegalStateException("Geçerli Offering yok")
            val pkg: Package = current.availablePackages.firstOrNull()
                ?: throw IllegalStateException("Offering içinde uygun paket bulunamadı")
            suspendCancellableCoroutine<Unit> { cont ->
                Purchases.sharedInstance.purchase(
                    pkg,
                    onError = { error: PurchasesError, _: Boolean ->
                        cont.resumeWithException(IllegalStateException(error.message))
                    },
                    onSuccess = { _: StoreTransaction, _: CustomerInfo ->
                        cont.resume(Unit)
                    }
                )
            }
            Napier.i(tag = "PaymentRepository", message = "Purchase tamamlandı")
            api.syncPayment(SyncPaymentRequestDto(isPremium = true, platform = "android"))
            Unit
        }
    }

    override suspend fun restorePurchases(): Result<Unit> = runCatching {
        withContext(Dispatchers.Default) {
            suspendCancellableCoroutine<Unit> { cont ->
                Purchases.sharedInstance.restorePurchases(
                    onError = { error: PurchasesError ->
                        cont.resumeWithException(IllegalStateException(error.message))
                    },
                    onSuccess = {
                        cont.resume(Unit)
                    }
                )
            }
            Napier.i(tag = "PaymentRepository", message = "Restore tamamlandı")
            api.syncPayment(SyncPaymentRequestDto(isPremium = true, platform = "android"))
            Unit
        }
    }

    override suspend fun isPro(): Result<Boolean> = runCatching {
        withContext(Dispatchers.Default) {
            val info = suspendCancellableCoroutine<CustomerInfo> { cont ->
                Purchases.sharedInstance.getCustomerInfo(
                    onError = { error: PurchasesError ->
                        cont.resumeWithException(IllegalStateException(error.message))
                    },
                    onSuccess = { ci ->
                        cont.resume(ci)
                    }
                )
            }
            info.entitlements.active.containsKey("BillioApp Pro")
        }
    }

    override suspend fun getCustomerInfo(): Result<CustomerInfo> = runCatching {
        withContext(Dispatchers.Default) {
            suspendCancellableCoroutine<CustomerInfo> { cont ->
                Purchases.sharedInstance.getCustomerInfo(
                    onError = { error: PurchasesError ->
                        cont.resumeWithException(IllegalStateException(error.message))
                    },
                    onSuccess = { ci ->
                        cont.resume(ci)
                    }
                )
            }
        }
    }

    override suspend fun isUserPremium(): Result<Boolean> = runCatching {
        withContext(Dispatchers.Default) {
            val info = suspendCancellableCoroutine<CustomerInfo> { cont ->
                Purchases.sharedInstance.getCustomerInfo(
                    onError = { error: PurchasesError ->
                        cont.resumeWithException(IllegalStateException(error.message))
                    },
                    onSuccess = { ci ->
                        cont.resume(ci)
                    }
                )
            }
            val premiumEntitlement = info.entitlements.active["premium"]
            premiumEntitlement?.isActive == true
        }
    }
}
