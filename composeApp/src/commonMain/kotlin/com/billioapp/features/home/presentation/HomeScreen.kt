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
import com.billioapp.features.home.presentation.components.AddBillSheet
import com.billioapp.features.home.presentation.components.MonthlyLimitSheet
import com.billioapp.features.home.presentation.components.BillListSection
import com.billioapp.features.home.presentation.components.BottomNavBar
import com.billioapp.features.home.presentation.components.HomeFab
import com.billioapp.features.home.presentation.components.HomeHeader
import com.billioapp.features.home.presentation.components.InfoCardSection
import com.billioapp.features.home.presentation.components.TrackerSection
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinInject(),
    onNavigateToLogin: () -> Unit = {},
    onFabClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    var isAddSheetOpen by remember { mutableStateOf(false) }
    var isLimitSheetOpen by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                HomeEffect.NavigateToLogin -> onNavigateToLogin()
                is HomeEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        containerColor = HomeColors.Background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = { HomeFab(onClick = { onFabClick(); isAddSheetOpen = true }) },
        bottomBar = { BottomNavBar(items = HomeSampleModels.bottomNav) }
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
                TrackerSection(model = HomeSampleModels.tracker, onLimitIconClick = { isLimitSheetOpen = true })
                InfoCardSection(model = HomeSampleModels.infoCard)
                BillListSection(bills = HomeSampleModels.bills)
                Spacer(modifier = Modifier.height(72.dp))
            }

            if (state.isLoggingOut) {
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
                    onSave = { /* TODO: bağlanınca VM veya data store'a ekleme yapılabilir */ }
                )
            }

            if (isLimitSheetOpen) {
                MonthlyLimitSheet(
                    onDismiss = { isLimitSheetOpen = false },
                    onSave = { value ->
                        // TODO: değeri ViewModel’e veya data store’a bağla
                        isLimitSheetOpen = false
                    }
                )
            }
        }
    }
}
