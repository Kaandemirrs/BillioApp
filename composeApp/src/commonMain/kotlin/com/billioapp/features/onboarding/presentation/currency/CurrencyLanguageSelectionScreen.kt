package com.billioapp.features.onboarding.presentation.currency

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import billioapp.composeapp.generated.resources.Res
import billioapp.composeapp.generated.resources.loginonboard
import org.jetbrains.compose.resources.painterResource

@Composable
fun CurrencyLanguageSelectionScreen(
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var selectedCurrency by remember { mutableStateOf("TRY") }
    var selectedLanguage by remember { mutableStateOf("TR") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFBDE9DE))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Image(
                painter = painterResource(Res.drawable.loginonboard),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Para Birimi",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CurrencyOption(
                    currency = "TRY",
                    isSelected = selectedCurrency == "TRY",
                    onSelect = { selectedCurrency = "TRY" }
                )
                CurrencyOption(
                    currency = "EUR",
                    isSelected = selectedCurrency == "EUR",
                    onSelect = { selectedCurrency = "EUR" }
                )
                CurrencyOption(
                    currency = "USD",
                    isSelected = selectedCurrency == "USD",
                    onSelect = { selectedCurrency = "USD" }
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Dil",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LanguageOption(
                    language = "TR",
                    isSelected = selectedLanguage == "TR",
                    onSelect = { selectedLanguage = "TR" }
                )
                LanguageOption(
                    language = "ENG",
                    isSelected = selectedLanguage == "ENG",
                    onSelect = { selectedLanguage = "ENG" }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.42f)
                ),
                shape = RoundedCornerShape(32.dp)
            ) {
                Text(
                    text = "Save",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun CurrencyOption(
    currency: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onSelect() }
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) Color.White else Color.White.copy(alpha = 0.3f)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFBDE9DE))
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = currency,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun LanguageOption(
    language: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onSelect() }
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) Color.White else Color.White.copy(alpha = 0.3f)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFBDE9DE))
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = language,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
