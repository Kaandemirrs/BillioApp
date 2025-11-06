package com.billioapp.features.ai.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import billioapp.composeapp.generated.resources.Res
import billioapp.composeapp.generated.resources.aiekrani
import billioapp.composeapp.generated.resources.ampl
import org.jetbrains.compose.resources.painterResource
import com.billioapp.core.theme.getBalooFontFamily
import com.billioapp.features.home.presentation.HomeColors
import com.billioapp.features.home.presentation.HomeSpacing
import com.billioapp.features.home.presentation.HomeSampleModels
import com.billioapp.features.home.presentation.components.BottomNavBar
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.billioapp.core.navigation.ProfileRoute
import com.billioapp.core.navigation.HomeRoute
import com.billioapp.core.navigation.AiRoute

@Composable
fun AiScreen() {
    val navigator = LocalNavigator.currentOrThrow
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
            Spacer(modifier = Modifier.height(32.dp))

            // Top center icon
            Image(
                painter = painterResource(Res.drawable.aiekrani),
                contentDescription = null,
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Page title (white Baloo)
            Text(
                text = "AI Tasarruf Danışmanı",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = getBalooFontFamily(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Warning rectangle with left small icon and Baloo text
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = HomeColors.Card)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ampl),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )

                    Spacer(modifier = Modifier.size(12.dp))

                    Text(
                        text = "Aboneliklerinizi analiz ederek tasarruf etmenize yardımcı olabilirim.",
                        color = HomeColors.TextPrimary,
                        fontSize = 16.sp,
                        fontFamily = getBalooFontFamily(),
                        textAlign = TextAlign.Start
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // İçerik yüksekliğine göre butonu aşağıya iten boşluk (auto layout mantığı)
            Spacer(modifier = Modifier.weight(1f))

            // Bluish button 4CE2D5 with white text (Baloo)
            Button(
                onClick = { navigator.push(com.billioapp.core.navigation.AiAnalysisRoute()) },
                shape = RoundedCornerShape(36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CE2D5),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Tüm Faturalarımı Analiz Et",
                    fontSize = 18.sp,
                    fontFamily = getBalooFontFamily(),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Powered by Gemini (small text)
            Text(
                text = "Power by GEMİNİ",
                color = HomeColors.TextSecondary,
                fontSize = 12.sp,
                fontFamily = getBalooFontFamily(),
                textAlign = TextAlign.Center
            )

            // BottomNavBar comes from Scaffold; do not re-add here
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}