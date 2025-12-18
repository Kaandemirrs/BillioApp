package com.billioapp.features.ai.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import billioapp.composeapp.generated.resources.Res
import billioapp.composeapp.generated.resources.aiekrani
import org.jetbrains.compose.resources.painterResource
import com.billioapp.core.theme.getBalooFontFamily
import com.billioapp.features.home.presentation.HomeColors
import com.billioapp.features.home.presentation.HomeSampleModels
import com.billioapp.features.home.presentation.components.BottomNavBar
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.billioapp.core.navigation.ProfileRoute
import com.billioapp.core.navigation.HomeRoute
import com.billioapp.core.navigation.AiRoute
import org.koin.compose.koinInject
import androidx.compose.runtime.collectAsState
import com.billioapp.features.analysis.presentation.FinancialAnalysisViewModel
import com.billioapp.features.analysis.presentation.FinancialAnalysisEffect
import androidx.compose.material3.CircularProgressIndicator

@Composable
fun AiAnalysisScreen() {
    val navigator = LocalNavigator.currentOrThrow
    val scrollState = rememberScrollState()
    val viewModel: FinancialAnalysisViewModel = koinInject()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is FinancialAnalysisEffect.ShowError -> {
                    // TODO: connect to a SnackbarHost if available
                    println("ANALYSIS ERROR: ${effect.message}")
                }
            }
        }
    }

    Scaffold(
        containerColor = HomeColors.Background,
        bottomBar = {
            BottomNavBar(
                items = HomeSampleModels.bottomNav,
                selectedItemId = "tracker",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Top icon (consistent with first screen)
            Image(
                painter = painterResource(Res.drawable.aiekrani),
                contentDescription = null,
                modifier = Modifier.size(160.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Page title (same font/style as first screen)
            Text(
                text = "AI Tasarruf Danışmanı",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = getBalooFontFamily(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Large, modern analysis card with long readable text
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = HomeColors.Card)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = state.reportText.ifBlank { "Analiz sonucu burada görünecek" },
                            color = HomeColors.TextPrimary,
                            fontSize = 16.sp,
                            fontFamily = getBalooFontFamily(),
                            textAlign = TextAlign.Start
                        )
                    }

                    if (state.isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = HomeColors.Primary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Smaller re-analyze button
            Button(
                onClick = { viewModel.loadAnalysis() },
                shape = RoundedCornerShape(36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CE2D5),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Yeniden Analiz Et",
                    fontSize = 16.sp,
                    fontFamily = getBalooFontFamily(),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Powered by Gemini
            Text(
                text = "Power by GEMİNİ",
                color = HomeColors.TextSecondary,
                fontSize = 12.sp,
                fontFamily = getBalooFontFamily(),
                textAlign = TextAlign.Center
            )
        }
    }
}
