package com.example.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SubscriptionState(
    val isPro: Boolean = false,
    val planName: String = "NEXORA Gratuito",
    val expiryDate: String = "Vitalício",
    val monthlyPrice: String = "R$ 39,90",
    val annualPrice: String = "R$ 349,00",
    val aiGenerationsUsed: Int = 14,
    val aiGenerationsLimit: Int = 50,
    val isPendingPayment: Boolean = false,
    val pendingPixCode: String? = null
)

class SubscriptionRepository {
    private val _subscription = MutableStateFlow(SubscriptionState())
    val subscription: StateFlow<SubscriptionState> = _subscription.asStateFlow()

    fun generatePixPayment(isAnnual: Boolean): String {
        val amount = if (isAnnual) "349.00" else "39.90"
        // Standard BR Code Pix format mockup conforming to Central Bank specifications
        val pixPayload = "00020126580014br.gov.bcb.pix0136nexora-pagbank-${System.currentTimeMillis()}520400005303986540${amount}5802BR5916NEXORA PLATAFORMA6009SAO PAULO62070503***6304"
        _subscription.value = _subscription.value.copy(
            isPendingPayment = true,
            pendingPixCode = pixPayload
        )
        return pixPayload
    }

    fun confirmPaymentDevSimulation() {
        _subscription.value = _subscription.value.copy(
            isPro = true,
            planName = "NEXORA Pro (Ativo)",
            expiryDate = "24/11/2026",
            aiGenerationsLimit = 9999,
            isPendingPayment = false,
            pendingPixCode = null
        )
    }

    fun cancelSubscription() {
        _subscription.value = SubscriptionState()
    }
}
