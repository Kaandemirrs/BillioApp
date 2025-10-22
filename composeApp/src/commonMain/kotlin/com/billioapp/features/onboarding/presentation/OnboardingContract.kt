package com.billioapp.features.onboarding.presentation

import androidx.compose.runtime.Immutable
import com.billioapp.core.mvi.UiState
import com.billioapp.core.mvi.UiEvent
import com.billioapp.core.mvi.UiEffect

@Immutable
data class OnboardingState(
    val currentPage: Int = 0,
    val isLoading: Boolean = false,
    val selectedLanguage: LanguageOption? = null,
    val selectedCurrency: CurrencyOption? = null,
    val isCompleted: Boolean = false
) : UiState {
    val totalPages: Int = 4
    val isLastPage: Boolean = currentPage == totalPages - 1
    val canProceed: Boolean = when (currentPage) {
        0 -> true // Welcome screen
        1 -> true // Smart spending screen
        2 -> true // Bill reminder screen
        3 -> true // Kontrol Senin Elinde - final page
        else -> false
    }
}

sealed class OnboardingEvent : UiEvent {
    object NextPage : OnboardingEvent()
    object PreviousPage : OnboardingEvent()
    data class GoToPage(val page: Int) : OnboardingEvent()
    data class SelectLanguage(val language: LanguageOption) : OnboardingEvent()
    data class SelectCurrency(val currency: CurrencyOption) : OnboardingEvent()
    object CompleteOnboarding : OnboardingEvent()
    object SkipOnboarding : OnboardingEvent()
}

sealed class OnboardingEffect : UiEffect {
    object NavigateToMain : OnboardingEffect()
    data class ShowError(val message: String) : OnboardingEffect()
}

data class OnboardingSlideData(
    val title: String,
    val description: String,
    val imageEmoji: String
)

object OnboardingSlides {
    val slides = listOf(
        OnboardingSlideData(
            title = "Billio",
            description = "",
            imageEmoji = "🎉"
        ),
        OnboardingSlideData(
            title = "Daha Akıllı Harca, Daha Çok Biriktir",
            description = "AI destekli önerilerle öğrenci indirimlerini, aile planlarını ve en uygun fırsatları keşfet. Cebinde daha çok kalsın!",
            imageEmoji = "🧠"
        ),
        OnboardingSlideData(
            title = "Faturalarını Unutma!",
            description = "Billio, tüm faturalarını tek yerde toplar ve zamanı gelince sana hatırlatır.",
            imageEmoji = "📋"
        ),
        OnboardingSlideData(
            title = "Kontrol Senin Elinde",
            description = "Sevimli kumbara dostun yanında. Şimdi fatura ve aboneliklerini birlikte yönetelim.",
            imageEmoji = "🎯"
        )
    )
}

data class LanguageOption(
    val code: String,
    val name: String,
    val flag: String
) {
    companion object {
        val availableLanguages = listOf(
            LanguageOption("tr", "Türkçe", "🇹🇷"),
            LanguageOption("en", "English", "🇺🇸"),
            LanguageOption("de", "Deutsch", "🇩🇪"),
            LanguageOption("fr", "Français", "🇫🇷")
        )
    }
}

data class CurrencyOption(
    val code: String,
    val name: String,
    val symbol: String
) {
    companion object {
        val availableCurrencies = listOf(
            CurrencyOption("TRY", "Türk Lirası", "₺"),
            CurrencyOption("USD", "US Dollar", "$"),
            CurrencyOption("EUR", "Euro", "€"),
            CurrencyOption("GBP", "British Pound", "£")
        )
    }
}