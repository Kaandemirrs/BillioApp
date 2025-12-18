package com.billioapp.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import billioapp.composeapp.generated.resources.Res
import billioapp.composeapp.generated.resources.bilgicardicon
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.billioapp.features.home.presentation.components.AddBillSheet
// MonthlyLimitSheet import kaldırıldı
import com.billioapp.features.home.presentation.components.BillListSection
import com.billioapp.features.home.presentation.components.BottomNavBar
import com.billioapp.features.home.presentation.components.HomeFab
import com.billioapp.features.home.presentation.components.HomeHeader
import com.billioapp.features.home.presentation.components.InfoCardSection
import com.billioapp.features.home.presentation.components.TrackerSection
import com.billioapp.features.home.presentation.components.AiPriceFinderSheet
import com.billioapp.features.home.presentation.components.SmartUpdateDialog
import org.koin.compose.koinInject
import com.billioapp.core.navigation.ProfileRoute
import com.billioapp.core.navigation.AiRoute
import com.billioapp.core.navigation.HomeRoute
import com.billioapp.core.navigation.AiPriceFinderRoute

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinInject(),
    onNavigateToLogin: () -> Unit = {},
    onFabClick: () -> Unit = {}
) {
    val navigator = LocalNavigator.currentOrThrow
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    var isAddSheetOpen by remember { mutableStateOf(false) }
    var isAiSheetOpen by remember { mutableStateOf(false) }
    var isSuggestionDialogOpen by remember { mutableStateOf(false) }
    var suggestionText by remember { mutableStateOf<String?>(null) }
    // var isLimitSheetOpen kaldırıldı

    LaunchedEffect(Unit) {
        viewModel.onEvent(HomeEvent.LoadHomeData)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                HomeEffect.NavigateToLogin -> onNavigateToLogin()
                is HomeEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                HomeEffect.SubscriptionAddedSuccessfully -> snackbarHostState.showSnackbar("Abonelik eklendi")
                HomeEffect.SubscriptionDeletedSuccessfully -> snackbarHostState.showSnackbar("Abonelik silindi")
                is HomeEffect.ShowAiPriceSuggestion -> {
                    suggestionText = effect.suggestion.priceAnalysisText ?: "Öneri bulunamadı"
                    isSuggestionDialogOpen = true
                }
                HomeEffect.SmartPriceUpdated -> snackbarHostState.showSnackbar("Fiyat güncellendi")
            }
        }
    }

    // limitText kaldırıldı (özellik yok)
    val trackerModel = state.trackerModel ?: TrackerModel(
        totalAmount = state.homeSummary?.totalAmount ?: 0.0,
        currency = state.homeSummary?.currency ?: "TL",
        categories = emptyList()
    )

    Scaffold(
        containerColor = HomeColors.Background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = { HomeFab(onClick = { onFabClick(); isAddSheetOpen = true }) },
        bottomBar = {
            BottomNavBar(
                items = HomeSampleModels.bottomNav,
                selectedItemId = "home",
                onItemSelected = { item ->
                    when (item.id) {
                        "home" -> navigator.replaceAll(HomeRoute())
                        "tracker" -> navigator.replaceAll(AiRoute())
                        "profile" -> navigator.replaceAll(ProfileRoute())
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(HomeColors.Background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(
                        horizontal = HomeSpacing.ScreenPadding,
                        vertical = HomeSpacing.SectionSpacing
                    ),
                verticalArrangement = Arrangement.spacedBy(HomeSpacing.SectionSpacing)
            ) {
                HomeHeader(userName = "Kaan")
                TrackerSection(
                    model = trackerModel,
                    subscriptions = state.subscriptions
                )
                val info = state.infoCardState
                if (info?.isVisible == true) {
                    InfoHintCard(state = info, modifier = Modifier.padding(top = 8.dp))
                }
                BillListSection(
                    bills = state.bills,
                    modifier = Modifier.padding(top = 36.dp, bottom = 12.dp),
                    onDeleteClicked = { id -> viewModel.onEvent(HomeEvent.OnDeleteClicked(id)) },
                    onCheckClicked = { bill -> viewModel.onEvent(HomeEvent.OnCheckSmartPrice(bill.id)) }
                )
                Spacer(modifier = Modifier.height(72.dp))
            }

            if (state.isLoggingOut || state.isLoading || state.isUpdatingLimit) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = HomeColors.Primary)
                }
            }

            if (isAddSheetOpen) {
                AddBillSheet(
                    onDismiss = { isAddSheetOpen = false },
                    onSave = { data -> viewModel.onEvent(HomeEvent.OnSaveClicked(data)) },
                    onAiPriceFinderRequested = { isAiSheetOpen = true }
                )
            }

            if (isAiSheetOpen) {
                AiPriceFinderSheet(
                    onDismiss = { isAiSheetOpen = false },
                    viewModel = viewModel
                )
            }

            if (isSuggestionDialogOpen) {
                AlertDialog(
                    onDismissRequest = { isSuggestionDialogOpen = false },
                    confirmButton = {
                        Button(onClick = { isSuggestionDialogOpen = false }) {
                            Text("Kapat")
                        }
                    },
                    title = { Text(text = "AI Fiyat Önerisi") },
                    text = { Text(text = suggestionText ?: "") }
                )
            }

            val smartState = state.smartUpdateState
            if (smartState?.isDialogVisible == true) {
                val activeId = smartState.activeBillId
                val sub = state.subscriptions.firstOrNull { it.id == activeId }
                val name = sub?.name ?: "Fatura"
                val currAmountText = if (sub != null) "${sub.amount.toInt()} ${sub.currency}" else ""
                val aiAmountText = smartState.foundPrice?.let { "${it} ${sub?.currency ?: "TL"}" } ?: ""
                val sourceText = smartState.source?.let { "Kaynak: $it" }
                SmartUpdateDialog(
                    visible = true,
                    title = "Fiyat Kontrolü: $name",
                    currentAmountText = currAmountText,
                    aiAmountText = aiAmountText,
                    sourceText = sourceText,
                    onDismiss = { viewModel.onEvent(HomeEvent.OnDismissSmartDialog) },
                    onConfirm = { viewModel.onEvent(HomeEvent.OnConfirmSmartUpdate) }
                )
            }

            // MonthlyLimitSheet kaldırıldı
        }
    }
}

@Composable
private fun InfoHintCard(state: InfoCardState, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val cardWidth = maxWidth
        val cardHeight = cardWidth * 0.2727f
        val cornerRadius = 42.dp
        val leftContentWidth = cardWidth * 0.2875f

        Row(
            modifier = Modifier
                .height(cardHeight)
                .clip(RoundedCornerShape(cornerRadius))
                .background(state.backgroundColor)
                .padding(horizontal = cardWidth * 0.028f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(leftContentWidth)
                    .fillMaxHeight()
            ) {
                Image(
                    painter = painterResource(Res.drawable.bilgicardicon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(leftContentWidth * 0.54f)
                        .align(Alignment.CenterStart)
                )
            }

            Text(
                text = state.text,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = cardWidth * 0.01f)
            )
        }
    }
}
