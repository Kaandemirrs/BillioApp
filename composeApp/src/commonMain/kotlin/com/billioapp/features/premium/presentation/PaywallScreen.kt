package com.billioapp.features.premium.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.billioapp.core.theme.getBalooFontFamily
import com.billioapp.features.home.presentation.HomeColors
import com.billioapp.features.home.presentation.HomeSpacing
import org.koin.compose.koinInject
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.billioapp.core.navigation.HomeRoute

@Composable
fun PaywallScreen(
    viewModel: PaywallViewModel = koinInject()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(Unit) {
        viewModel.onEvent(PaywallEvent.LoadOffering)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PaywallEffect.ShowError -> { /* TODO: Snackbar */ }
                PaywallEffect.ShowSuccess -> { /* TODO: Dialog */ }
                PaywallEffect.NavigateToHome -> navigator.replaceAll(HomeRoute())
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = HomeSpacing.ScreenPadding, vertical = HomeSpacing.SectionSpacing),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Billio Premium'a Geçin",
            fontFamily = getBalooFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            color = HomeColors.Primary
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("✓ Sınırsız AI Analiz", style = MaterialTheme.typography.bodyLarge.copy(fontFamily = getBalooFontFamily()))
            Text("✓ Reklamsız", style = MaterialTheme.typography.bodyLarge.copy(fontFamily = getBalooFontFamily()))
            Text("✓ Akıllı Fiyat Takibi", style = MaterialTheme.typography.bodyLarge.copy(fontFamily = getBalooFontFamily()))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Aylık ${state.priceText}",
            style = MaterialTheme.typography.titleMedium.copy(fontFamily = getBalooFontFamily()),
            fontWeight = FontWeight.Bold,
            color = HomeColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.onEvent(PaywallEvent.PurchaseClicked) },
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = HomeColors.Primary)
            } else {
                Text("Hemen Başla")
            }
        }

        Button(
            onClick = { viewModel.onEvent(PaywallEvent.RestoreClicked) }
        ) {
            Text("Satın Alımı Geri Yükle")
        }
    }
}

