package com.billioapp.features.onboarding.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.billioapp.core.theme.BillioAppTheme
import com.billioapp.features.onboarding.presentation.components.OnboardingSlide
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onNavigateToMain: () -> Unit
) {
    BillioAppTheme {
        val state by viewModel.state.collectAsStateWithLifecycle()
        val pagerState = rememberPagerState(pageCount = { state.totalPages })

        // Handle effects
        LaunchedEffect(viewModel) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is OnboardingEffect.NavigateToMain -> {
                        onNavigateToMain()
                    }
                    is OnboardingEffect.ShowError -> {
                        // Handle error - could show snackbar or dialog
                    }
                }
            }
        }

        // Sync pager with state
        LaunchedEffect(state.currentPage) {
            if (pagerState.currentPage != state.currentPage) {
                pagerState.animateScrollToPage(state.currentPage)
            }
        }

        // Sync state with pager
        LaunchedEffect(pagerState.currentPage) {
            if (state.currentPage != pagerState.currentPage) {
                viewModel.onEvent(OnboardingEvent.GoToPage(pagerState.currentPage))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFBDE9DE))
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0, 1, 2 -> {
                        OnboardingSlide(
                            slide = OnboardingSlides.slides[page],
                            onGetStartedClick = {
                                // Navigate to next page for slides 0-2
                                viewModel.onEvent(OnboardingEvent.NextPage)
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    3 -> {
                        OnboardingSlide(
                            slide = OnboardingSlides.slides[page],
                            onGetStartedClick = {
                                // Complete onboarding on the last slide (Kontrol Senin Elinde)
                                viewModel.onEvent(OnboardingEvent.CompleteOnboarding)
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // Remove navigation indicators since we only have slide pages now
        }
    }
}