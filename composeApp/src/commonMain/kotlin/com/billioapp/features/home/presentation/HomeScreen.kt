package com.billioapp.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import org.koin.compose.koinInject
import com.billioapp.core.navigation.ProfileRoute

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
                onItemSelected = { item ->
                    when (item.id) {
                        "profile" -> navigator.push(ProfileRoute())
                        // Future: handle other ids (home/tracker) if needed
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
                InfoCardSection(model = HomeSampleModels.infoCard)
                BillListSection(
                    bills = state.bills,
                    subscriptions = state.subscriptions,
                    modifier = Modifier.padding(top = 36.dp, bottom = 12.dp),
                    onDeleteClicked = { id -> viewModel.onEvent(HomeEvent.OnDeleteClicked(id)) }
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
                    onSave = { data -> viewModel.onEvent(HomeEvent.OnSaveClicked(data)) }
                )
            }

            // MonthlyLimitSheet kaldırıldı
        }
    }
}
