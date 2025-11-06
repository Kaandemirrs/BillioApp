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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import billioapp.composeapp.generated.resources.Res
import billioapp.composeapp.generated.resources.ampl
import com.billioapp.core.theme.getBalooFontFamily
import com.billioapp.features.home.presentation.HomeColors
import com.billioapp.features.home.presentation.HomeSpacing
import com.billioapp.features.home.presentation.HomeSampleModels
import com.billioapp.features.home.presentation.components.BottomNavBar
import org.jetbrains.compose.resources.painterResource
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

@Composable
fun AiPriceFinderScreen() {
    val navigator = LocalNavigator.currentOrThrow
    var subscriptionName by remember { mutableStateOf("") }
    var aiSuggestion by remember { mutableStateOf("") }

    Scaffold(
        containerColor = HomeColors.Background,
        bottomBar = {
            BottomNavBar(
                items = HomeSampleModels.bottomNav,
                selectedItemId = "tracker",
                onItemSelected = { item ->
                    when (item.id) {
                        "home" -> navigator.replaceAll(com.billioapp.core.navigation.HomeRoute())
                        "tracker" -> navigator.replaceAll(com.billioapp.core.navigation.AiRoute())
                        "profile" -> navigator.replaceAll(com.billioapp.core.navigation.ProfileRoute())
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = HomeSpacing.ScreenPadding, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top green title
            Text(
                text = "AI Fiyat Bulucu",
                color = HomeColors.Primary,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = getBalooFontFamily(),
                textAlign = TextAlign.Start
            )

            // Row: label + input + small analyze button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Abonelik İsmi giriniz!",
                    color = HomeColors.TextPrimary,
                    fontSize = 18.sp,
                    fontFamily = getBalooFontFamily()
                )
                Spacer(modifier = Modifier.weight(1f))
                OutlinedTextField(
                    value = subscriptionName,
                    onValueChange = { subscriptionName = it },
                    modifier = Modifier.fillMaxWidth(0.55f),
                    shape = RoundedCornerShape(36.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HomeColors.Primary,
                        unfocusedBorderColor = HomeColors.Primary,
                        cursorColor = HomeColors.Primary,
                        focusedTextColor = HomeColors.TextPrimary,
                        unfocusedTextColor = HomeColors.TextPrimary
                    )
                )
                Button(
                    onClick = {
                        // Placeholder suggestion generation
                        aiSuggestion = "Örnek: '${subscriptionName}' için aylık önerilen fiyat 89 TL."
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HomeColors.Primary),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(text = "Analiz Et", color = Color.White, fontFamily = getBalooFontFamily())
                }
            }

            // Row: label + large/tall input box (suggestion)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "AI Fiyat Önerisi",
                    color = HomeColors.TextPrimary,
                    fontSize = 18.sp,
                    fontFamily = getBalooFontFamily()
                )

                Card(
                    modifier = Modifier.fillMaxWidth().height(180.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = HomeColors.Card)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Text(
                            text = if (aiSuggestion.isBlank()) "Analiz sonucu burada görünecek" else aiSuggestion,
                            color = HomeColors.TextPrimary,
                            fontSize = 16.sp,
                            fontFamily = getBalooFontFamily()
                        )
                    }
                }
            }

            // Bottom: lamp icon + warning text
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.ampl),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "AI önerisidir, lütfen doğruluğunu kontrol edin.",
                    color = HomeColors.TextPrimary,
                    fontSize = 16.sp,
                    fontFamily = getBalooFontFamily()
                )
            }
        }
    }
}